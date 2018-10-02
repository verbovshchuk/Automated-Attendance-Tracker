# Automated-Attendance-Tracker
## CSC 131 - Software Engineering
This application allows an admin to set a password on a Google sheets file, and then students can use a web portal to login. If the login is successful and the secret code set by admin is correct, then the application searches the Google sheets file for the student id/first or last name and then marks the student as present for the day. The login is only open for a 5-10 minute period, after which the application loops through the students that weren't marked present and marks them as absent. 

This application requires that the first row in the Sheets file contains the headers: Last Name, First Name, Username, and Student ID. Each of the rows would have the corresponding information for the student. The row after the last student entry will say "Secret_Code_For_The_Day". When the application is run, it will check the next available column header for today's date. If it is has not been initiated yet, then it will add the day's date and that will allow students to register following that. The admin needs to add the verification code in the "Secret_Code_For_The_Day" row, under the appropriate day.

## How to run:
1. Add a new client to Google Sheets API. Authenticate and download client_secret.json. 
2. Load project into eclipse or other IDE.
3. Replace client_secret.json in resources directory with downloaded client_secret.json.
4. Create a google sheets file, and copy the url id of the file, replace that in the appropriate constant variable in the SheetsQuickStart.java file.
5. Deploy application on servlet and test on localhost.

This application uses:
..* Apache tomcat
..* Java backend
..* Google Sheets API
..* HTML/CSS front end 

