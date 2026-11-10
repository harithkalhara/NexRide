param([switch]$Admin, [switch]$Stop, [switch]$UserLogin)
# Launcher revision 3.0.1: recover an existing process even when the state file is missing.
$ErrorActionPreference = 'Stop'
$root = $PSScriptRoot
Set-Location -LiteralPath $root
$statePath = Join-Path $root '.runtime.json'
$log = Join-Path $root 'nexride-startup.log'
$errLog = Join-Path $root 'nexride-errors.log'
$jar = Join-Path $root 'NexRide.jar'
$data = Join-Path $root 'data'
$release = 'studio-3.0.0'
$mutex = $null
$lockHeld = $false

function Find-ProjectServers {
    $found = @{}
    $saved = $null
    if (Test-Path -LiteralPath $statePath) {
        try { $saved = Get-Content -LiteralPath $statePath -Raw | ConvertFrom-Json } catch { }
    }
    $processes = @(Get-CimInstance Win32_Process -Filter "Name = 'java.exe' OR Name = 'javaw.exe'" -ErrorAction SilentlyContinue)
    foreach ($item in $processes) {
        if (-not $item.CommandLine) { continue }
        $command = $item.CommandLine
        $sameJar = $command -match ([regex]::Escape($jar) + '(?:"|\s|$)')
        $sameData = $command -match ('--nexride.data-dir="?' + [regex]::Escape($data) + '(?:"|\s|$)')
        $sameSavedProcess = $false
        if ($saved -and $saved.token -and $saved.processId -eq $item.ProcessId) {
            $sameSavedProcess = $command -match ('-Dnexride.launch=' + [regex]::Escape([string]$saved.token) + '(?:\s|$)')
        }
        # Exact absolute JAR + data paths, or an exact recorded PID + random token.
        # A generic java.exe / NexRide name is never enough to claim or stop a process.
        if (($sameJar -and $sameData) -or $sameSavedProcess) { $found[[string]$item.ProcessId] = $item }
    }
    return @($found.Values)
}
function Get-ReadyUrl([int]$serverId) {
    $listeners = @(Get-NetTCPConnection -OwningProcess $serverId -State Listen -ErrorAction SilentlyContinue)
    foreach ($listener in $listeners) {
        if ($listener.LocalAddress -notin @('127.0.0.1','0.0.0.0','::1','::')) { continue }
        $base = 'http://127.0.0.1:' + $listener.LocalPort
        try {
            $info = Invoke-RestMethod -Uri ($base + '/release.json') -TimeoutSec 2
            if ($info.release -eq $release) { return $base }
        } catch { }
    }
    return $null
}
function Save-Server($item, [string]$base) {
    $launchToken = ''
    if ($item.CommandLine -match '-Dnexride.launch=([a-fA-F0-9]{32})(?:\s|$)') { $launchToken = $Matches[1] }
    @{processId=[int]$item.ProcessId; token=$launchToken; url=$base; release=$release} | ConvertTo-Json | Set-Content -LiteralPath $statePath -Encoding UTF8
    @('NexRide Studio 03', "Website: $base", "Admin: $base/admin", 'Stop: double-click STOP-NEXRIDE.bat') | Set-Content -LiteralPath (Join-Path $root 'OPEN-WEBSITE.txt') -Encoding UTF8
}
function Open-NexRide([string]$base) {
    $url = $base
    if ($Admin) { $url += '/admin' }
    elseif ($UserLogin) { $url += '/login' }
    Write-Host "NexRide Studio 03 is ready: $url" -ForegroundColor Green
    Write-Host "Admin login details: $root\data\admin-access.txt"
    Write-Host 'Use STOP-NEXRIDE.bat when you are finished.'
    Start-Process $url
}
function Recover-Server {
    foreach ($item in @(Find-ProjectServers)) {
        $base = Get-ReadyUrl ([int]$item.ProcessId)
        if ($base) {
            Save-Server $item $base
            Write-Host 'Found the NexRide server already running from this project folder.' -ForegroundColor Green
            Open-NexRide $base
            return $true
        }
    }
    return $false
}
try {
    # Prevent two simultaneous clicks from starting two processes for one data folder.
    $hasher = [System.Security.Cryptography.SHA256]::Create()
    try { $folderHash = [BitConverter]::ToString($hasher.ComputeHash([Text.Encoding]::UTF8.GetBytes($root.ToLowerInvariant()))).Replace('-','') } finally { $hasher.Dispose() }
    $mutex = New-Object System.Threading.Mutex($false, ('Local\NexRideStudio_' + $folderHash))
    try { $lockHeld = $mutex.WaitOne(0) } catch [System.Threading.AbandonedMutexException] { $lockHeld = $true }
    if (-not $lockHeld) {
        Write-Host 'NexRide is already starting in another launcher. Please let that window finish.' -ForegroundColor Yellow
        exit 0
    }
    $existing = @(Find-ProjectServers)
    if ($Stop) {
        if ($existing.Count -eq 0) { Write-Host 'No server owned by this folder was found.'; exit 0 }
        foreach ($item in $existing) {
            Stop-Process -Id ([int]$item.ProcessId) -ErrorAction SilentlyContinue
            Wait-Process -Id ([int]$item.ProcessId) -Timeout 15 -ErrorAction SilentlyContinue
        }
        Remove-Item -LiteralPath $statePath -Force -ErrorAction SilentlyContinue
        Write-Host 'This project server has stopped. Your saved data is unchanged.' -ForegroundColor Green
        exit 0
    }
    if ($existing.Count -gt 0) {
        Write-Host 'Checking the NexRide server already running from this folder...' -ForegroundColor Cyan
        for ($attempt = 0; $attempt -lt 60; $attempt++) {
            if (Recover-Server) { exit 0 }
            if (@(Find-ProjectServers).Count -eq 0) { break }
            Start-Sleep -Seconds 1
        }
        if (@(Find-ProjectServers).Count -gt 0) {
            throw 'A server from this folder exists but is not responding. Use STOP-NEXRIDE.bat, then START-NEXRIDE.bat. Do not delete the data folder.'
        }
    }
    $java = Get-Command java -ErrorAction SilentlyContinue
    if (-not $java) { throw 'Java was not found. Install Java 17 or newer and reopen this file. Maven is not needed.' }
    if (-not (Test-Path -LiteralPath $jar)) { throw 'NexRide.jar is missing. Put these launcher files beside NexRide.jar in your extracted project folder.' }
    $token = [Guid]::NewGuid().ToString('N')
    Write-Host 'Starting NexRide Studio 03 on a free local port...' -ForegroundColor Cyan
    $arguments = @("-Dnexride.launch=$token", '-jar', ('"' + $jar + '"'), '--server.address=127.0.0.1', '--server.port=0', ('--nexride.data-dir="' + $data + '"'))
    $server = Start-Process -FilePath $java.Source -ArgumentList $arguments -WorkingDirectory $root -RedirectStandardOutput $log -RedirectStandardError $errLog -WindowStyle Hidden -PassThru
    # Record ownership immediately, rather than only after the browser opens.
    @{processId=$server.Id; token=$token; url=''; release=$release} | ConvertTo-Json | Set-Content -LiteralPath $statePath -Encoding UTF8
    for ($attempt = 0; $attempt -lt 90; $attempt++) {
        Start-Sleep -Seconds 1
        $server.Refresh()
        if ($server.HasExited) {
            if (Recover-Server) { exit 0 }
            throw 'The server stopped. Read nexride-startup.log and nexride-errors.log. If the data folder is locked by a manually started server, close that server with Ctrl+C first. Do not delete data.'
        }
        if (Recover-Server) { exit 0 }
    }
    throw 'Startup is taking longer than expected. Leave the server running and try START-NEXRIDE.bat again, or use STOP-NEXRIDE.bat to stop this project.'
} catch {
    Write-Host $_.Exception.Message -ForegroundColor Red
    if (Test-Path -LiteralPath $log) { Get-Content -LiteralPath $log -Tail 14 }
    exit 1
} finally {
    if ($lockHeld -and $mutex) { $mutex.ReleaseMutex() }
    if ($mutex) { $mutex.Dispose() }
}
