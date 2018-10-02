import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.ExtendedValue;
import com.google.api.services.sheets.v4.model.GridCoordinate;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.RowData;
import com.google.api.services.sheets.v4.model.UpdateCellsRequest;
import com.google.api.services.sheets.v4.model.ValueRange;

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

public class SheetsGoogle {

	public static void updateSheets(String sheetsName, String key, String sID) throws IOException
	{
	    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
 	    Date date = new Date();
 	    String today = dateFormat.format(date);
 	    
 	    String present = "Present";
    	
    	int rNum = -1;
        boolean correctCode;
       
        
        int pID = Integer.parseInt(sID); //makes the input id into an int for use with other methods        
        correctCode = SheetsQuickstart.searchForCode(sheetsName, key);
        
        rNum = SheetsQuickstart.search(sheetsName, pID);

    	int col = SheetsQuickstart.searchForDate(sheetsName, today);
    	SheetsQuickstart.changeValue(sheetsName,rNum, col, present);
	}
}
