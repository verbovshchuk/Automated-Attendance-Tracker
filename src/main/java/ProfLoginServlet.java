

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
/**
 * Servlet implementation class ProfLoginServlet
 */
@WebServlet("/ProfLoginServlet")
public class ProfLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProfLoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		response.sendRedirect("index.jsp");

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String inputUsername = request.getParameter("professor_id");
		String inputPassword = request.getParameter("password");
		String inputKey = request.getParameter("addKey");
		String sheetCalled = request.getParameter("section_name");
		//String sheetCalled = "CSC 131-02";
		
		String page = "";
		if (!(Instructor.validClass(sheetCalled)))
		{
			page = "professorError.jsp";
			
		} else
		{
			
		
			if (!(Instructor.profUser(sheetCalled, inputUsername) || Instructor.profPass(sheetCalled, inputPassword)))
			{
				page = "professorError.jsp";
			} else
			{
				
	    	    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
	     	    Date date = new Date();
	     	    String today = dateFormat.format(date);
	     	    
	     	    if (SheetsQuickstart.searchForDate(sheetCalled, today) != -1)
	     	    {
	     	    	page = "alreadyTaken.jsp";
	     	    } else
	     	    {
	     	    	   	    
		    		int emptyCol = Instructor.searchForEmptyDate(sheetCalled); //search for a column in the first row that is empty
		    		Instructor.changeValue( sheetCalled, 0, emptyCol, today); //creates date on the spreadsheet
		    		int codeRow = Instructor.addCode(sheetCalled);
		    		Instructor.code(sheetCalled, codeRow, emptyCol, inputKey);
		    		Instructor.fillAbsences(sheetCalled);
		    		Instructor.code(sheetCalled, codeRow, emptyCol, inputKey);
		    		System.out.println("Attendance successfully setup with code " + inputKey + ".");
		    		page = "KeySuccess.jsp";
	     	    }
			}
		}
		response.sendRedirect(page);
		
		
	}

}
