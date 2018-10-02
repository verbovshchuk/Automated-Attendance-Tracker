# Automated-Attendance-Tracker
This application allows an admin to set a password on a Google sheets file, and then students can use a web portal to login. If the login is successful and the secret code set by admin is correct, then the application searches the Google sheets file for the student id/first or last name and then marks the student as present for the day. The login is only open for a 5-10 minute period, after which the application loops through the students that weren't marked present and marks them as absent. 

##How to run:
1. Add a new client to Google Sheets API. Authenticate and download client_secret.json. 
2. Load project into eclipse or other IDE.
3. Replace client_secret.json in resources directory with downloaded client_secret.json.
4. Create a google sheets file, and copy the url id of the file, replace that in the appropriate constant variable in the SheetsQuickStart.java file.
5. Deploy application on servlet and test on localhost.

