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

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Scheduling"));
    }

    public static Result employeePage(){
    	return ok(employee.render());
    }

    public static Result schedulePage(){
    	return ok(schedule.render());
    }

    public static Result qualificationsPage(){
    	return ok(qualifications.render());
    }

    public static Result getAreas(){
    	String sqlString = "SELECT Name FROM tblArea;";
        Connection conn = DB.getConnection();
        String returnVal = "<option value=\"\">SELECT AREA</option>";

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sqlString);
            while(rs.next()) { 
                String areaName = rs.getString(1);
                returnVal += "<option value=\"" + areaName + "\">" + areaName + "</option>\n";
            }
            rs.close();
            st.close();
        }
        catch (SQLException e)
        {
            return noContent();
        }           
        
        return ok(returnVal);
    }

    public static Result getAreaSchedule(String area){
        return ok();
    }
    
}
