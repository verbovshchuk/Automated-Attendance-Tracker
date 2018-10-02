//This communicates between the SheetsQuickStart and the html code
//modified by pavel verbovshchuk

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		response.sendRedirect("professorLogin.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
				
		//retrieve input and stores it at variables
		String studentID = request.getParameter("student_id");
		String inputKey = request.getParameter("key");
		String sheetsName = request.getParameter("section_name");
		
		
		String page = "";
		
	    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
 	    Date date = new Date();
 	    String today = dateFormat.format(date);

 	    //makes sure input is 9 characters and is numbers only
		if (!(studentID.matches("^[0-9]*$") && studentID.length() > 2) || studentID.length() != 9) 
		{
			page = "LoginError.jsp";
		} else {
			if (!SheetsQuickstart.validClass(sheetsName)) //makes sure that the course matches a worksheet
			{
				page = "LoginError.jsp";
			}else
			{
				
				if (SheetsQuickstart.searchForDate(sheetsName, today) == -1) //makes sure attendance has been started by professor today
				{
					page = "LoginError.jsp";
				} else{ 
					
			
					int sID = Integer.parseInt(studentID);
				
					boolean validCode = SheetsQuickstart.searchForCode(sheetsName, inputKey);
					int rowOfStudent = SheetsQuickstart.search(sheetsName, sID);
		
					//makes sure the code is valid, student is found, and student is within designated time slot
					if(!(validCode) || (rowOfStudent == -1) || !(SheetsQuickstart.inTime(sheetsName)))
					{
						page = "LoginError.jsp";
					}
					else //everything successful, proceed to take attendance
					{
						System.out.println("Valid student ID.");
						System.out.println("Valid input key.");
						SheetsGoogle.updateSheets(sheetsName, inputKey, studentID);
						page = "Success.jsp";
						System.out.println("Attendance for student successful.");
		
					}
				}
			}
		}
		
		//response.sendRedirect("loading.jsp");	
		response.sendRedirect(page);
	}

}
