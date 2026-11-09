"""Run the actual packaged JAR with ports 8080/8081 occupied; no external packages."""
import json, re, socket, subprocess, tempfile, time, urllib.request, urllib.error, http.cookiejar
from pathlib import Path
root=Path(__file__).resolve().parents[1]
occupied=[]
for port in (8080,8081):
    s=socket.socket()
    try: s.bind(('127.0.0.1',port));s.listen();occupied.append(s)
    except OSError: s.close() # An existing listener also constitutes an occupied port.
with tempfile.TemporaryDirectory(prefix='nexride runtime space ') as folder:
    folder=Path(folder)
    with (folder/'server.log').open('w+') as logfile:
        process=subprocess.Popen(['java','-jar',str(root/'NexRide.jar'),'--server.port=0','--server.address=127.0.0.1','--nexride.data-dir='+str(folder/'data')],stdout=logfile,stderr=subprocess.STDOUT)
        try:
            port=None
            for _ in range(120):
                logfile.flush(); text=(folder/'server.log').read_text()
                match=re.search(r'Tomcat started on port (\d+)',text)
                if match and 'Started NexRideApplication' in text:
                    port=int(match.group(1));break
                if process.poll() is not None: raise AssertionError('JAR exited during startup: '+text)
                time.sleep(.25)
            assert port and port not in (8080,8081)
            base=f'http://127.0.0.1:{port}'
            def get(path):
                with urllib.request.urlopen(base+path,timeout=5) as res:
                    assert res.status==200
                    return res.read(),res.headers
            release=json.loads(get('/release.json')[0]);assert release['release']=='studio-3.0.0'
            for path in ['/','/login','/signin','/register','/admin','/styles.css?v=studio3','/app.js?v=studio3','/assets/nexride-mark.svg','/assets/rider.jpg','/assets/bicycle.jpg','/assets/sport.jpg']:
                data,headers=get(path);assert len(data)>100
            home=get('/')[0].decode();assert 'Your island. Your pace.' in home
            assert 'studio-3.0.0' in home
            session,headers=get('/api/session');assert 'NEXRIDE_STUDIO_SESSION=' in headers['Set-Cookie']
            for _ in range(30):
                public=json.loads(get('/api/public')[0])
                if public['bikes']:break
                time.sleep(.1)
            assert len(public['bikes'])>=2
            assert (folder/'data'/'admin-access.txt').exists()
            assert 'passwordHash' not in json.dumps(public)
            jar=http.cookiejar.CookieJar();client=urllib.request.build_opener(urllib.request.HTTPCookieProcessor(jar))
            client.open(base+'/api/session').read()
            def post(path,payload):
                request=urllib.request.Request(base+path,data=json.dumps(payload).encode(),headers={'Content-Type':'application/json'},method='POST')
                with client.open(request) as response:return json.load(response)
            post('/api/register',{'name':'Runtime Rider','email':'runtime@example.test','phone':'+94771112233','password':'RuntimeTest!123','country':'Sri Lanka'})
            assert post('/api/login',{'identifier':'runtime@example.test','password':'RuntimeTest!123'})['user']['role']=='CUSTOMER'
            assert post('/api/login',{'identifier':'+94771112233','password':'RuntimeTest!123'})['user']['role']=='CUSTOMER'
            try:
                post('/api/login',{'identifier':'runtime@example.test','password':'wrong-password'})
                raise AssertionError('Wrong password accepted')
            except urllib.error.HTTPError as e:assert e.code in (400,401,403)
            try:
                client.open(base+'/api/admin/summary')
                raise AssertionError('Customer entered admin area')
            except urllib.error.HTTPError as e:assert e.code==403
            print('PASS: registration, email login, phone login, wrong-password rejection and customer/admin isolation.')
            print('PASS: packaged JAR starts with 8080 and 8081 occupied, selects a new port, serves Studio 03 assets/admin/API, uses its own cookie and creates private admin setup. No browser opened.')
        finally:
            process.terminate()
            try:process.wait(timeout=10)
            except subprocess.TimeoutExpired:process.kill();process.wait()
for s in occupied:s.close()
