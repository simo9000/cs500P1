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
import java.sql.Date

public class ScheduleController extends Controller {

    public static Result getAreaSchedule(String area){
    	String sqlString = "SELECT tblShift.date_ as date \n" +
    					   "FROM tblShift \n" +
    					   "	INNER JOIN tblscheduleToWork \n" +
    					   "		ON tblShift.date_ = tblscheduleToWork.shiftDate \n";

    	Connection conn = DB.getConnection();
    	String returnVal = "<table border="1"> \r" +
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
    						"\t\t<td>" + date + "</td>";
    						"\t\t<td>" + getScheduledEmployees(date,area) + "</td>";
    		}
    		rs.close();
    		st.close();
    	}
    	catch (SQLException e)
    	{
    		return noContent();
    	}




        return ok();
    }

    private static String getScheduledEmployees(String shiftDate, String area){
    	String sqlString = "SELECT tblEmployee.Name \r" +
    					   "FROM tblEmployee \r" +
    					   "	INNER JOIN tblscheduleToWork \r" +
    					   "		ON tblEmployee.EID = tblscheduleToWork.EID \r" +
    					   "WHERE tblscheduleToWork.shiftDate='" + shiftDate + "' \r" +
    					   "AND tblscheduleToWork.areaName='" + area + "'\r";
    	Connection conn = DB.getConnection();

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
    		return "";
    	}

    	returnVal += "</ul>\r";
    	return returnVal;
    }

    
}
