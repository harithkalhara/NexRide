# NexRide Studio 03

A redesigned motorcycle and bicycle rental application for the NexRide OOP project. Spring Boot + Java, with JSON text-file storage. This release has a new ivory / navy / orange interface, an original N road-mark logo, a split photographic homepage, new rental browsing and detail pages, and redesigned customer/admin dashboards.

## Start on Windows — no Maven needed

1. Extract the entire ZIP into a new folder, for example `Desktop\NexRide-Studio`.
2. Double-click **START-NEXRIDE.bat**. Java 17 or newer must be installed.
3. Wait for the browser to open. The page headline is **Your island. Your pace.** The footer says **STUDIO / 03**.
4. To open administration, double-click **ADMIN-LOGIN.bat**.
5. Your first admin email and random password are in `data\admin-access.txt`. Change the password after login. Credentials are never displayed on a sign-in page.
6. To stop this server, double-click **STOP-NEXRIDE.bat**.

The launcher asks Windows for a free port. Do not keep typing 8080 or 8081. The correct address is opened automatically and saved to **OPEN-WEBSITE.txt**. Launcher revision 3.0.1 recovers a server from this exact folder even if its startup record is missing: it matches the absolute JAR/data paths or the recorded PID/token, finds that process’s listening port, and verifies Studio 03. A per-folder Windows mutex prevents simultaneous starts. The app has a separate session-cookie name from the older NexRide versions.

If Windows blocks the script, or PowerShell is unavailable, open a terminal inside this folder and run:

```powershell
java -jar .\NexRide.jar --server.port=0
```

Find `Tomcat started on port NUMBER` in the terminal. Open `http://localhost:NUMBER`. Admin is `http://localhost:NUMBER/admin`. Keep that terminal open. Stop with Ctrl+C.

## VS Code

Use **File → Open Folder** and select the extracted folder containing `NexRide.jar`. In the integrated PowerShell terminal run:

```powershell
.\START-NEXRIDE.bat
```

Do not use Live Server to run the full application: accounts, rentals, uploads and billing need the Java backend. Editing source files does not modify the prebuilt JAR. Rebuild after code changes using `mvn clean package`, then replace `NexRide.jar` with `target/nexride-reborn-3.0.0.jar`.

## Customer flows

- Browse motorcycles and bicycles; search city and dates, filter type and sort price.
- View bike specifications, stock/reference-photo labels, reviews and a live rental estimate.
- Register with email (including Gmail), phone and password. Sign in with email or phone plus password. This is not Google OAuth or SMS OTP.
- Request a rental; follow approval, invoice, pickup and return from the dashboard.
- Display LKR or USD using the administrator-published exchange rate.
- Pay an approved invoice through PayHere hosted Visa/Mastercard checkout when merchant settings are configured. Card details never enter NexRide.
- Offer community rides, request passenger seats and accept/decline requests for rides you host.
- Submit a review after a completed rental, raise support issues, and edit your profile/photo/password.

## Admin flows

- Separate `/admin` login with role checks.
- Overview metrics and rental request queue.
- Add, edit and delete/retire bikes and bicycles; upload or replace actual fleet photos with preview.
- Approve or decline requests, record pickup and return.
- Record cash only after it has actually been received; inspect invoices/payments.
- Manage passenger requests, customer account status and support replies.
- Update public contact email/phone/address/hours, social links, website logo, tagline and currency rate.
- Update administrator profile, profile photo and password.

## Storage and migration

`data/nexride-data.txt` holds application records. `data/uploads/` holds photos. A process lock prevents concurrent writers. Writes use an atomic temporary-file replacement. Keep one application instance per data folder.

The ZIP contains **no customer data or credentials**. A new folder creates a new account store. To preserve old records, stop both servers, back up the old `data` folder, and copy that whole folder into this release before its first start. Existing account credentials remain in effect. Do not delete old data until the new release has been verified.

## Deploy with Docker

The prebuilt JAR is included, so the Docker build does not need Maven. Docker must be installed separately.

```sh
docker compose up -d --build
```

Open `http://localhost:8095`; admin is `/admin`. Persistent data uses the `nexride-data` named volume. To retrieve the first-admin file locally:

```sh
docker compose cp nexride:/app/data/admin-access.txt ./admin-access.txt
```

See `docs/DEPLOYMENT.md` for public HTTPS hosting. This ZIP has not been published to a hosting account. It needs a Java/container host with a persistent disk; static-only hosting cannot run its backend.

## Tests

See `docs/TEST-REPORT.md` for actual results and limitations. Run backend tests with `mvn test`. To run DOM tests, install the optional development dependency with `npm install`, then `npm run test:ui`. Node/npm are not needed to run the application.

## Team ownership

| Member | Module |
|---|---|
| Kalhara | User management |
| Nipuna | Bike and bicycle management |
| Siyas | Rental request management |
| Thinal | Ride sharing |
| Murshid | Payment and billing |
| Galkotuwa | Feedback, reviews and issue reports |

For real payment onboarding, see `docs/ONLINE_PAYMENTS.md`. Stock photographs are reference imagery, not a representation of exact rental stock; admins can replace them with fleet photos. Source URLs and reference websites are recorded in `docs/ASSET_CREDITS.md` and `docs/DESIGN-REFERENCES.md`.

User sign in: double-click USER-LOGIN.bat. It starts or reuses this project and opens /login on its actual port. Customer login accepts registered email or phone and password. /#login and /signin also work. Admin login remains separate.

## Atlas 04 visual update
Emerald/porcelain light theme and forest dark theme; Space Grotesk headings and DM Sans body typography. Use the moon/sun button to switch; preference is saved on this browser. Photo hero, dashboards and login forms refreshed. Footer ATLAS / 04 identifies the new design.

Payment flow: Billing → Pay securely → billing details/currency → exact server amount review → hosted PayHere checkout. Returning from checkout does not itself mark an invoice paid. Refresh billing to retrieve provider-confirmed status. Unconfigured payments show support instructions; no simulated successful payments. Live use requires merchant settings described in docs/ONLINE_PAYMENTS.md.

Upgrade: stop the old server; back up data; replace application files while preserving data; launch USER-LOGIN.bat or START-NEXRIDE.bat. Maven is not needed to run the bundled JAR.
