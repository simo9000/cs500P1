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
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ScheduleController extends Controller {

    private Connection conn;

    public static Result getAreaSchedule(String area){
    	String sqlString = "SELECT tblShift.date_ as date \n" +
    			   "FROM tblShift \n" +
    			   "	INNER JOIN tblscheduledToWork \n" +
    			   "		ON tblShift.date_ = tblscheduledToWork.shiftDate \n" +
			       "WHERE tblscheduledToWork.areaName='" + area + "';";
	
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
    		return ok(e.getMessage() + "\r" + e.getStackTrace());
    	}

        return ok(returnVal);
    }

    public static Result addNextShift(String area){
        
        conn = DB.getConnection();

        String newDate = getNextDate(area);

        scheduleNewShift(newDate);
        
        scheduleAssignedEmployees(newDate, area);

        return ok();
    }

    private String getNewDate(String area){
        String sqlString = "SELECT Max(tblShift.date_) \n" +
                           "FROM tblShift \n" +
                           "    INNER JOIN tblscheduledToWork \n" +
                           "        ON tblShift.date_ = tblscheduledToWork.shiftDate \n" +
                           "WHERE tblscheduledToWork.areaName='" + area + "';";

        Connection conn = DB.getConnection();
        String lastDate = "";
        List<String> acceptableDays = new List<String>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sqlString);
            if (rs.first())
                lastDate = rs.getString(1);
            else
                lastDate = "08312015";
            rs.close();
            st.close();
            acceptableDays = availableWorkDays(area);
        }
        catch (SQLException e)
        {
            return internalServerError(e.getMessage());
        }

        string newDate = getNextDate(lastDate);
        while(!isDateValid(newDate, acceptableDays)) newDate = getNextDate(newDate);

        return newDate;
    }

    private void scheduleNewShift(String date){

        String sqlString = "SELECT * FROM tblShift WHERE date_='" + date + "';";

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sqlString);
            if (!rs.first())
            {
                sqlString = "INSERT INTO tblShift(date_, isHoliday) VALUES ('" + newDate + "',false);";
                st.execute(sqlString);
            }
        }
        catch (SQLException e)
        {
            return internalServerError(e.getMessage());
        }
    }

    private void scheduleAssignedEmployees(String date, String area){
        String sqlString = "SELECT EID FROM tblResourceAssignedTo WHERE areaName='" + area + "';";

        List<int> ids = new List<int>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sqlString);
            while(rs.next())
                ids.add(rs.getInt(1));
            rs.close();
            

            for(int i =0; i < ids.size(); i++)
            {
                sqlString = "INSERT INTO tblScheduledToWork(EID, shiftDate, areaName) VALUES (" + ids[i] + ",'" + date + "','" + area + "')";
                st.execute(sqlString);
            }
            st.close();
        }
        catch (SQLException e)
        {
            return internalServerError(e.getMessage());
        }
    }

    private static String getScheduledEmployees(String shiftDate, String area){
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
    		return e.getMessage();
    	}

    	returnVal += "</ul>\r";
    	return returnVal;
    }

    private static String getNextDate(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(date));
        c.add(Calendar.Date,1);
        return sdf.format(c.getTime());
    }

    private static List<String> availableWorkDays(String area) throws SQLException{
        String sqlString = "SELECT dayName FROM tblAreaOperatesOn WHERE dayName='" + area + "'";

        Connection conn = DB.getConnection();
        List<String> returnVal = new List<String>();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sqlString);
        while (rs.next()){
            returnVal.add(rs.getString(1));
        }
        return returnVal;
    }

    private boolean isDateValid(String date, List<String> validDays){
        SimpleDateFormat sdf_date = new SimpleDateFormat("ddMMyyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(date));
        SimpleDateFormat sdf_DOW = new SimpleDateFormat("EEE");
        String DOW = sdf_DOW.format(c.getTime()).toUpperCase();
        return validDays.contains(DOW);
    }
}
