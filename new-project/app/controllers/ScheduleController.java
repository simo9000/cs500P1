package controllers;

import play.*;
import play.db.DB;
import play.mvc.*;


import utils.DBUtils;
import views.html.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScheduleController extends Controller {

    public static Result getAreaSchedule(String area){
    	String sqlString = "SELECT DISTINCT shiftDate as date \n" +
    			   "FROM tblscheduledToWork \n" +
    			   "WHERE tblscheduledToWork.areaName='" + area + "'" +
			   "ORDER BY shiftDate;";

	Connection conn = DB.getConnection();
    	String returnVal = "<table border=\"1\"> \r" +
    		               "\t<tr>\r" +
    	 				   "\t\t<th>Date</th>\r" +
    	 				   "\t\t<th>Scheduled To Work</th>\r" +
    	 				   "\t</tr>\r";

    	try {
    		Statement st = conn.createStatement();
    		ResultSet rs = st.executeQuery(sqlString);
    		while(rs.next()){
    			String date = rs.getString(1);
    			returnVal +="\t<tr>\r"+
    						"\t\t<td>" + date + "</td>" +
    						"\t\t<td>" + getScheduledEmployees(date,area) + "</td>";
    		}
    		rs.close();
    		st.close();
    	}
    	catch (SQLException e)
    	{
		printError(e);
    	}

        return ok(returnVal);
    }

    public static Result addNextShift(String area){
       
	String newDate = "";

	try { 
	        newDate = getNewDate(area);
		scheduleNewShift(newDate);
		scheduleAssignedEmployees(newDate, area);

		Connection conn = DB.getConnection();
		conn.close();
	}	
	catch (SQLException e){
		printError(e);
	}

        return ok();
    }

    private static String getNewDate(String area) throws SQLException {
        String sqlString = "SELECT Max(tblShift.date_) \n" +
                           "FROM tblShift \n" +
                           "    INNER JOIN tblscheduledToWork \n" +
                           "        ON tblShift.date_ = tblscheduledToWork.shiftDate \n" +
                           "WHERE tblscheduledToWork.areaName='" + area + "';";
        
	Connection conn = DB.getConnection();
        String lastDate = "";
        List<String> acceptableDays = new ArrayList<String>();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sqlString);
        rs.next();
        lastDate = rs.getString(1);
        if (lastDate == null) lastDate = "08/31/2015";
        rs.close();
        st.close();
        acceptableDays = availableWorkDays(area);
        String newDate = getNextDate(lastDate);
        while(!isDateValid(newDate, acceptableDays)) newDate = getNextDate(newDate);

        return newDate;
    }

    private static void scheduleNewShift(String date) throws SQLException {

            Connection conn = DB.getConnection();
	    
	    String sqlString = "SELECT * FROM tblShift WHERE date_='" + date + "';";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sqlString);
            if (!rs.next())
            {
                sqlString = "INSERT INTO tblShift(date_, isHoliday) VALUES ('" + date + "',false);";
                st.execute(sqlString);
            }
    }

    private static void scheduleAssignedEmployees(String date, String area) throws SQLException {

    	Connection conn = DB.getConnection();

        String sqlString = "SELECT EID FROM tblResourceAssignedTo WHERE areaName='" + area + "';";

        List<Integer> ids = new ArrayList<Integer>();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sqlString);
            while(rs.next())
                ids.add(rs.getInt(1));
            rs.close();
            

            for(int i =0; i < ids.size(); i++)
            {
                sqlString = "INSERT INTO tblScheduledToWork(EID, shiftDate, areaName) VALUES (" + ids.get(i) + ",'" + date + "','" + area + "')";
                st.execute(sqlString);
            }
            st.close();
    }

    private static String getScheduledEmployees(String shiftDate, String area){

    	Connection conn = DB.getConnection();

	String sqlString = "SELECT tblEmployee.Name \r" +
    					   "FROM tblEmployee \r" +
    					   "	INNER JOIN tblscheduledToWork \r" +
    					   "		ON tblEmployee.EID = tblscheduledToWork.EID \r" +
    					   "WHERE tblscheduledToWork.shiftDate='" + shiftDate + "' \r" +
    					   "AND tblscheduledToWork.areaName='" + area + "'\r";
    	
    	String returnVal = "<ul>\r";

    	try {
    		Statement st = conn.createStatement();
    		ResultSet rs = st.executeQuery(sqlString);
    		while(rs.next()) {
    			String employeeName = rs.getString(1);
    			returnVal += "\t<li>" + employeeName + "</li>\r";
    		}
    		rs.close();
    		st.close();
    	}
    	catch (SQLException e)
    	{
		printError(e);
    	}

    	returnVal += "</ul>\r";
    	return returnVal;
    }

    private static String getNextDate(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Calendar c = Calendar.getInstance();
        try {
		c.setTime(sdf.parse(date));
	}
	catch (ParseException e) { }
        c.add(Calendar.DATE,1);
        return sdf.format(c.getTime());
    }

    private static List<String> availableWorkDays(String area) throws SQLException{
        String sqlString = "SELECT dayName FROM tblAreaOperatesOn WHERE areaName='" + area + "'";
        Connection conn = DB.getConnection();
        List<String> returnVal = new ArrayList<String>();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sqlString);
        while (rs.next()){
            returnVal.add(rs.getString(1));
        }
        return returnVal;
    }

    private static boolean isDateValid(String date, List<String> validDays){
        SimpleDateFormat sdf_date = new SimpleDateFormat("MM/dd/yyyy");
        Calendar c = Calendar.getInstance();
        try {
		c.setTime(sdf_date.parse(date));
	}
	catch (ParseException e) { }
        SimpleDateFormat sdf_DOW = new SimpleDateFormat("EEE");
        String DOW = sdf_DOW.format(c.getTime()).toUpperCase();
        return validDays.contains(DOW);
    }

    private static void printError(SQLException e)
    {
	Logger.error(e.getMessage());
	StackTraceElement lines[] = e.getStackTrace();
	for(int i =0; i<lines.length; i++)
		Logger.error(lines[i].toString());
    }

}
