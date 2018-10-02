// This class handle's the professor's part of the application
// Modified by: Pavel Verbovshchuk, Haziq Saeed,Alina Gritsyuk, Sukhraj Atwal 
// Base version by Doan Nguyen


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import com.google.api.services.sheets.v4.Sheets;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Date;
import java.util.Scanner;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.lang.*;
import java.net.URL;

public class Instructor {
    /** Application name. */
    private static final String APPLICATION_NAME =
        "Professor's Class Attendance";
    
    private static String spreadsheetId = "1IHCQZPmgwdeQar1IBZC_3_1rzSc2ViAglN7yGnrB8DU";
    
    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
        System.getProperty("user.home"), ".credentials//sheets.googleapis.com-java-quickstart.json");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
        JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/sheets.googleapis.com-java-quickstart.json
     */
    private static final List<String> SCOPES =
        Arrays.asList( SheetsScopes.SPREADSHEETS );

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        // Place client_secret.json file location here
        InputStream in = new FileInputStream("/Users/pverbovshchuk/Documents/Fall 2016/CSC131/SheetsQuickstart/src/main/resources/client_secret.json");
            // SheetsQuickstart.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
            flow, new LocalServerReceiver()).authorize("user");
 /*       System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath()); */
        return credential;
    }

    /**
     * Build and return an authorized Sheets API client service.
     * @return an authorized Sheets API client service
     * @throws IOException
     */
    public static Sheets getSheetsService() throws IOException {
        Credential credential = authorize();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void changeValue(String sheetsName, int sRow, int sColumn, String requestedValue) throws IOException
    {
    	int sheetID = (int) getSheetId(sheetsName);

    	
        // Build a new authorized API client service.
        Sheets service = getSheetsService();
 
        // Create requests object
        List<Request> requests = new ArrayList<>();

        // Create values object
        List<CellData> values = new ArrayList<>();
        
     
        values.add(new CellData()
                .setUserEnteredValue(new ExtendedValue()
                        .setStringValue((requestedValue))));

        // Prepare request with proper row and column and its value
        requests.add(new Request()
                .setUpdateCells(new UpdateCellsRequest()
                        .setStart(new GridCoordinate()
                                .setSheetId(sheetID)
                                .setRowIndex(sRow)     // set the row to row 0 
                                .setColumnIndex(sColumn)) // set the new column 6 to value 9/12/2016 at row 0
                        .setRows(Arrays.asList(
                                new RowData().setValues(values)))
                        .setFields("userEnteredValue,userEnteredFormat.backgroundColor")));
        
         BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest()
     	        .setRequests(requests);
     	service.spreadsheets().batchUpdate(spreadsheetId, batchUpdateRequest)
     	        .execute();
    }
    
    public static boolean validClass(String inputClass)
    {
    	if (inputClass.equalsIgnoreCase("CSC 131-01") || inputClass.equalsIgnoreCase("CSC 131-02")||inputClass.equalsIgnoreCase("CSC 131-03")||inputClass.equalsIgnoreCase("CSC 131-04")||inputClass.equalsIgnoreCase("CSC 131-05"))
    	{
    		return true;
    	} else {
    		return false;
    	}
    }
    
    //checks whether input username matches professor's username at the end of column c
    public static boolean profUser(String sheetsName, String usernameInput) throws IOException
    {
    	String profUsername = "";
 	    
    	Sheets service = getSheetsService();
    	String range = sheetsName + "!C:C"; //specifies range of cells we will search, in our case the ID field is column D
    	ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
    	List<List<Object>> values = response.getValues();
    
    	for (List column: values) //loops through values
    	{
    		for (Object p: column)
    		{
    			profUsername = (String)column.get(0);
    		}
    	}    	
    	if (usernameInput.equals(profUsername)) //correct code inputted by the user
    	{
    		return true;
    	} else {
    		return false;
    	}	
    }
    
    //checks whether the professor's password input matches the one at the end of column D
    public static boolean profPass(String sheetsName, String passwordInput) throws IOException
    {
    	String profPassword = "";
 	    
    	Sheets service = getSheetsService();
    	String range = sheetsName + "!D:D"; //specifies range of cells we will search, in our case the ID field is column D
    	ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
    	List<List<Object>> values = response.getValues();
    
    	for (List column: values) //loops through values
    	{
    		for (Object p: column)
    		{
    			profPassword = (String)column.get(0);
    		}
    	}    	
    	if (passwordInput.equals(profPassword)) //correct code inputted by the user
    	{
    		return true;
    	} else {
    		return false;
    	}	
    }

    //precondition: assumes that date has already been created on the spreadsheet by professor
    public static int searchForEmptyDate(String sheetsName) throws IOException
    {
    	int colNumber = 0; //initiate counter for column number
    	
    	Sheets service = getSheetsService();
    	String range = sheetsName + "!1:1"; //specifies range of cells we will search, in our case the ID field is column D
    	ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
    	List<List<Object>> values = response.getValues();
    	
    	for (List column: values) //loops through values
    	{
    		for (Object p: column)
    		{
    			colNumber++; //increment column number as we move to the next row
    		}
    	}
    	return colNumber; //if date is not found, return -1 because no such date value exists on the spreadsheet
 
    }
    
    
    //searches the spreadsheet for a certain id value
    public static int search(String sheetsName, int input) throws IOException
    {   
    	String stringInput = Integer.toString(input); //convert int id input into string so that we can compare it to the spreadsheet value of id
    	int rowNumber = 0; // initiates row counter, first row is the header so it should never match 1
    	
    	Sheets service = getSheetsService();
    	String range = sheetsName + "!D:D"; //specifies range of cells we will search, in our case the ID field is column D
    	ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
    	List<List<Object>> values = response.getValues();
    	
    	for (List row: values) //loops through the rows
    	{
    		if (stringInput.equals(row.get(0))) // compares the String input value to the value in the id cell
    		{
    			return rowNumber; //if a match is found, the row number will be returned
    		}
    		rowNumber++; //increment row number as we move to the next row
    	}
    	return -1; //if id is not found, return -1 because no such row value exists on the spreadsheet
    }
    
    //precondition: assumes that date has already been created on the spreadsheet by professor
    public static int searchForDate(String sheetsName, String day) throws IOException
    {
    	int colNumber = 0; //initiate counter for column number
    	
    	Sheets service = getSheetsService();
    	String range = sheetsName + "!1:1"; //specifies range of cells we will search, in our case the ID field is column D
    	ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
    	List<List<Object>> values = response.getValues();
    	
    	for (List column: values) //loops through values
    	{
    		for (Object p: column)
    		{
       			if (day.equals(column.get(colNumber))) // compares the String input value to the value in the loop variable
    			{
    				return colNumber; //if a match is found, the column number will be returned
    			}
    			colNumber++; //increment column number as we move to the next row
    		}
    	}
    	return -1; //if date is not found, return -1 because no such date value exists on the spreadsheet
 
    }
    
    private static String getCharForNumber(int i) {
        return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
    }
    
    //fills all students with absent so they can then change attendance
    public static void fillAbsences(String sheetsName) throws IOException	
    {
    	int counterRow = 0;
    	
    	//grab today's date
 	    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
 	    Date date = new Date();
 	    String today = dateFormat.format(date);
 	    
 	    //search for the column number of the day
 	    int todayCol = searchForDate(sheetsName, today);
 	    
 	    String colLetter = getCharForNumber(todayCol + 1);
 	    
    	Sheets service = getSheetsService();
    	String range = sheetsName + "!"+ colLetter + ":" + colLetter; //specifies range of cells we will search, in our case the ID field is column D
    	ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
    	List<List<Object>> values = response.getValues();
    
    	for (List column: values) //loops through values
    	{
    		if ((counterRow != 0))
    		{
        		changeValue(sheetsName, counterRow, todayCol, "Absent");
    		}
    		counterRow++;
    	}    	
    }
    
    //method for the secret code
    public static void code(String sheetsName, int row, int dateCol, String secretCode) throws IOException
    {
    	changeValue(sheetsName, row, 0, "Secret_Code_For_The_Day");
    	changeValue(sheetsName, row, dateCol, secretCode);
    	changeValue(sheetsName, row+1, 1, String.valueOf(System.currentTimeMillis()));
    	

    }
    //returns next available row number, to add code to it
    public static int addCode(String sheetsName) throws IOException
    {
    	String stringInput = "Secret_Code_For_The_Day"; 
    	int counter = 0; // initiates row counter, first row is the header so it should never match 1
    	
    	Sheets service = getSheetsService();
    	String range = sheetsName + "!A:A"; //specifies range of cells we will search, in our case the ID field is column D
    	ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
    	List<List<Object>> values = response.getValues();
    	
    	for (List row: values) //loops through the rows
    	{
    		if (stringInput.equals(row.get(0))) // compares the String input value to the value in the id cell
    		{
    			return counter; //if a match is found, the row number will be returned
    		}
    		counter++; //increment row number as we move to the next row
    	}
    	return counter; //if id is not found, return -1 because no such row value exists on the spreadsheet
    }
    
    //get first worksheet in the spreadsheet
    public static Object setDefaultSheetId()throws IOException{
    	Sheets service = getSheetsService();
    	List<String> range= new ArrayList<String>();
    	//range.add(sheetName);
    	Spreadsheet result = service.spreadsheets().get(spreadsheetId).setRanges(range).setFields("sheets.properties.sheetId").execute();
    	int sheetsId = (Integer) result.getSheets().get(0).getProperties().get("sheetId");
    	System.out.println(result.getSheets().get(0).getProperties().get("sheetId"));
    	return result.getSheets().get(0).getProperties().get("sheetId");
    }
    //returns the id  number of the sheet converted from sheet name
    public static Object getSheetId(String sheetName)throws IOException{
    	Sheets service = getSheetsService();
    	List<String> range= new ArrayList<String>();
    	range.add(sheetName);
    	Spreadsheet result = service.spreadsheets().get(spreadsheetId).setRanges(range).setFields("sheets.properties.sheetId").execute();
    	//System.out.println(result.getSheets().get(0).getProperties().get("sheetId"));
    	return result.getSheets().get(0).getProperties().get("sheetId");
    	
    }
    //this method runs everything, used for testing before connecting to servlet
    public static void main(String[] args) throws IOException 
    {    
    	/*
    	//setDefaultSheetId();
    	String sheetsName = "CSC 131-03";
    	//System.out.println(getSheetId(sheetsName));
    	
    	
    	Scanner scan = new Scanner (System.in);
    	System.out.println("Hello Professor, would you like to take attendance today?");
    	String yesOrNo = scan.nextLine();
    	if (yesOrNo.equalsIgnoreCase("Yes"))
    	{
        	//grab today's date
     	    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
     	    Date date = new Date();
     	    String today = dateFormat.format(date);
     	    
     	    System.out.println("Enter the desired secret code:");
     	    String secretCode = scan.nextLine();
     	    
    		int emptyCol = searchForEmptyDate(sheetsName); //search for a column in the first row that is empty
    		changeValue( sheetsName, 0, emptyCol, today); //creates date on the spreadsheet
    		int codeRow = addCode(sheetsName);
    		code(sheetsName, codeRow, emptyCol, secretCode);
    		fillAbsences(sheetsName);
    		code(sheetsName, codeRow, emptyCol, secretCode);
    		System.out.println("Attendance successfully setup with code " + secretCode + ".");
    	}
    	else 
    	{
    		System.out.println("Attendance not taken, have a great day.");
    	}
    	
    	boolean repeat = true;
    	while(repeat)
    	{
	    		
	    	System.out.println("Enter Professor username: ");
	    	Scanner scan = new Scanner(System.in);
	    	String username = scan.nextLine();
	    	System.out.println("Enter professor password: ");
	    	String password = scan.nextLine();
	    	System.out.println("Username valid? : " + profUser(username));
	    	System.out.println("Password valid? : " + profPass(password));
	    	if (profUser(username) && profPass(password))
	    	{
	    		repeat = false;
	    	}
    	}*/
    	
    }
}
