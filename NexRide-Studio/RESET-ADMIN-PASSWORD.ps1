$ErrorActionPreference = 'Stop'
$root = $PSScriptRoot
$dataFile = Join-Path $root 'data\nexride-data.txt'
if (-not (Test-Path -LiteralPath $dataFile -PathType Leaf)) {
    Write-Host 'NexRide data file was not found beside this reset tool.' -ForegroundColor Yellow
    try {
        Add-Type -AssemblyName System.Windows.Forms
        $picker = New-Object System.Windows.Forms.OpenFileDialog
        $picker.Title = 'Select your NexRide data file (nexride-data.txt)'
        $picker.Filter = 'NexRide data file (nexride-data.txt)|nexride-data.txt|Text files (*.txt)|*.txt|All files (*.*)|*.*'
        $picker.CheckFileExists = $true
        if ($picker.ShowDialog() -ne [System.Windows.Forms.DialogResult]::OK) {
            Write-Host 'No file selected. No changes were made.' -ForegroundColor Red
            exit 1
        }
        $dataFile = $picker.FileName
    } catch {
        $chosen = Read-Host 'Enter the full path to data\nexride-data.txt'
        $dataFile = $chosen.Trim().Trim('"')
    }
}
if (-not (Test-Path -LiteralPath $dataFile -PathType Leaf)) {
    Write-Host 'Selected NexRide data file does not exist. No changes were made.' -ForegroundColor Red
    exit 1
}
$dataFolder = Split-Path -Parent $dataFile
$lockFile = Join-Path $dataFolder '.nexride.lock'
if (Test-Path -LiteralPath $lockFile) {
    try {
        $probe = [System.IO.File]::Open($lockFile, [System.IO.FileMode]::OpenOrCreate, [System.IO.FileAccess]::ReadWrite, [System.IO.FileShare]::None)
        $probe.Dispose()
    } catch {
        Write-Host 'NexRide is still running. Double-click STOP-NEXRIDE.bat, then run this reset tool again.' -ForegroundColor Yellow
        exit 1
    }
}
$email = Read-Host 'Admin email [admin@nexride.local]'
if ([string]::IsNullOrWhiteSpace($email)) { $email = 'admin@nexride.local' }
$email = $email.Trim().ToLowerInvariant()
$first = Read-Host 'New password (12 characters minimum)' -AsSecureString
$second = Read-Host 'Type the new password again' -AsSecureString
function Convert-SecureToPlain([Security.SecureString]$value) {
    $ptr = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($value)
    try { [Runtime.InteropServices.Marshal]::PtrToStringBSTR($ptr) }
    finally { [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($ptr) }
}
$password = Convert-SecureToPlain $first
$confirm = Convert-SecureToPlain $second
if ($password.Length -lt 12) { Write-Host 'Use at least 12 characters.' -ForegroundColor Red; exit 1 }
if ($password -cne $confirm) { Write-Host 'Passwords did not match. No changes were made.' -ForegroundColor Red; exit 1 }
Add-Type -TypeDefinition @'
using System;
using System.Security.Cryptography;
public static class NexRidePasswordReset {
  public static string Hash(string password) {
    byte[] salt = new byte[16];
    using (var rng = RandomNumberGenerator.Create()) rng.GetBytes(salt);
    byte[] derived;
    using (var pbkdf = new Rfc2898DeriveBytes(password, salt, 210000, HashAlgorithmName.SHA256)) derived = pbkdf.GetBytes(32);
    return "pbkdf2$210000$" + Convert.ToBase64String(salt) + "$" + Convert.ToBase64String(derived);
  }
}
'@
try {
    $json = [System.IO.File]::ReadAllText($dataFile, [System.Text.Encoding]::UTF8)
    $state = ConvertFrom-Json -InputObject $json
    $admins = @($state.users | Where-Object { $_.role -eq 'ADMIN' -and $_.email -eq $email })
    if ($admins.Count -ne 1) { throw 'Could not find exactly one administrator with that email. No data was changed.' }
    $admins[0].passwordHash = [NexRidePasswordReset]::Hash($password)
    $backup = "$dataFile.before-admin-password-reset-$(Get-Date -Format 'yyyyMMdd-HHmmss').bak"
    Copy-Item -LiteralPath $dataFile -Destination $backup
    $updated = ConvertTo-Json -InputObject $state -Depth 100
    $tempFile = "$dataFile.reset.tmp"
    [System.IO.File]::WriteAllText($tempFile, $updated, (New-Object System.Text.UTF8Encoding($false)))
    $replaceBackup = "$dataFile.reset-rollback.tmp"
    [System.IO.File]::Replace($tempFile, $dataFile, $replaceBackup)
    if (Test-Path -LiteralPath $replaceBackup) { Remove-Item -LiteralPath $replaceBackup -Force }
    Write-Host "Admin password reset. Sign in as $email at /admin" -ForegroundColor Green
    Write-Host "Existing data backup: $backup"
    Write-Host 'Start NexRide with START-NEXRIDE.bat, then open ADMIN-LOGIN.bat.'
} catch {
    Write-Host $_.Exception.Message -ForegroundColor Red
    Write-Host 'No reset was completed. If a backup was created, it is beside the data file.' -ForegroundColor Yellow
    exit 1
} finally {
    $password = $null; $confirm = $null
    $first.Dispose(); $second.Dispose()
}
