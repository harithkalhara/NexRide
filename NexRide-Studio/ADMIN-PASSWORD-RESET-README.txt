Forgot the NexRide administrator password?

1. Extract this small reset kit. It can be in any folder.
2. Double-click STOP-NEXRIDE.bat. Wait for it to close.
3. Double-click RESET-ADMIN-PASSWORD.bat.
4. Press Enter to use admin@nexride.local, unless you changed the admin email.
5. Enter a new password twice. The characters stay hidden while typing. Use 12 or more characters.
6. Double-click START-NEXRIDE.bat, then ADMIN-LOGIN.bat.

The reset changes only the matching administrator password hash. Rental, bike, account and payment records stay in place. A timestamped backup of the data file is made before the change. The old password cannot be recovered; this sets a new one.

Keep the reset files private. Run only in your NexRide project folder. If your data folder is stored elsewhere, the reset tool opens a file picker. Select the existing data\nexride-data.txt file in your NexRide project. The picker must select the data file, not the launcher, ZIP, or folder.
