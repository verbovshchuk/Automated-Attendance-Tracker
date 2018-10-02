//this contains all of the methods for the student login verification

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

public class SheetsQuickstart {
    /** Application name. */
    private static final String APPLICATION_NAME =
        "Google Sheets API Java Quickstart";
    
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
        InputStream in = SheetsQuickstart.class.getResourceAsStream("/client_secret.json");
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

    /*public static boolean sendReceipt(int row) throws IOException
    {
    	int colNumber = 0; //initiate counter for column number
    	
    	Sheets service = getSheetsService();
    	String range = "CSC 131!" + row + ":" + row; //specifies range of cells we will search, in our case the ID field is column D
    	ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
    	List<List<Object>> values = response.getValues();
    	
    	for (List column: values) //loops through values
    	{
    		for (Object p: column)
    		{
    			colNumber++; //increment column number as we move to the next row
    		}
    	}
    	return false; //if date is not found, return -1 because no such date value exists on the spreadsheet
    	
    }*/
    //returns the sheets ID of a sheet name
    public static Object getSheetId(String sheetName)throws IOException{
    	Sheets service = getSheetsService();
    	List<String> range= new ArrayList<String>();
    	range.add(sheetName);
    	Spreadsheet result = service.spreadsheets().get(spreadsheetId).setRanges(range).setFields("sheets.properties.sheetId").execute();
    	return result.getSheets().get(0).getProperties().get("sheetId");
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
    
    public static void changeValue(String sheetName, int sRow, int sColumn, String requestedValue) throws IOException
    {
    	int sheetID = (int) getSheetId(sheetName);
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
    
    //retrieves today's code
    public static boolean searchForCode(String sheetsName, String userCode) throws IOException
    {
    	String lastCode = "";
    	
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
    		for (Object p: column)
    		{
    			lastCode = (String)column.get(0);
    		}
    	}    	
    	if (userCode.equals(lastCode)) //correct code inputted by the user
    	{
    		return true;
    	} else {
    		return false;
    	}
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
    
    //verify time
    public static boolean inTime(String sheetsName) throws IOException
    {
    	String classStart = "";	  
 	    
    	Sheets service = getSheetsService();
    	String range = sheetsName + "!B:B"; //specifies range of cells we will search, in our case the ID field is column D
    	ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
    	List<List<Object>> values = response.getValues();
    
    	for (List column: values) //loops through values
    	{
    		for (Object p: column)
    		{
    			classStart = (String)column.get(0);
    		}
    	}  
    	long classTime = Long.parseLong(classStart);
    	long curTime = System.currentTimeMillis();
    	if (curTime <= (classTime+180000))
    	{
    		return true;
    	}
    	return false;
    }
    //this method runs everything, using java. used for testing before connecting to servlet
    public static void main(String[] args) throws IOException 
    {    
    	String sheetsName = "CSC 131";
    	
    	//grab today's date
 	    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
 	    Date date = new Date();
 	    String today = dateFormat.format(date);
 	    
 	    String present = "Present";
    	//initiate scanner and ask for user to input student id
    	Scanner scan = new Scanner(System.in);
    	
    	int rNum = -1;
        boolean correctCode;
        
    	do {
    		
        System.out.println("Enter your student id: ");
        
        String ID = scan.nextLine();
        
       //check to make sure that the input is all numbers and exactly 9 chars
        while ((!(ID.matches("^[0-9]*$") && ID.length() > 2)) || ID.length() != 9) 
        {
        	System.out.println("Try again.");
        	ID = scan.nextLine();
        }
        
        int sID = Integer.parseInt(ID); //makes the input id into an int for use with other methods
        
        System.out.println("Enter the secret code: ");
        String codee = scan.nextLine();
        
        correctCode = searchForCode("CSC 131", codee);
        
        rNum = search("CSC 131", sID);
        //boolean onTime = inTime();
        System.out.println(inTime("CSC 131"));
        if (rNum == -1 || !(correctCode) || !(inTime("CSC 131")))
        {
            System.out.println("Invalid Entry. Please try again: ");
        }
    	} while (rNum == -1 || !(correctCode) || !(inTime("CSC 131")));
    	int col = searchForDate("CSC 131", today);
    	changeValue(sheetsName,rNum, col, present);
    	System.out.println("Successfully updated attendance.");
    }
}