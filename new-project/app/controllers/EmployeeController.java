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

public class EmployeeController extends Controller {

    
    public static Result getAllEmployees() {
        String sql = "select eid, name from tblEmployee";
        Connection conn = DB.getConnection();
        String result = "<select id='employee_list'>";
         result += "<option value='-1'>Select An Employee</option>";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                result += "<option value='"+rs.getString("eid")+"'>"
                        +rs.getString("name")+"-" + rs.getString("eid")   +"</option>";
            }
            rs.close();
            st.close();

            DBUtils.closeDBConnection(conn);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        result += "</select>";
        return ok(result);
    }

public static Result getScheduleByEmployee(String eid_str) {

        int eid = Integer.parseInt(eid_str);


        String sql = "select e.eid, e.name, shiftdate, areaname, hours  from "+                       
                     "tblscheduledtowork w, "                                +                                  
                     "tblemployee e, "                                        +                   
                     "tbldayofweek d "                                        + 
                     "where e.eid = w.eid and "                               +      
                     "d.name = left(to_char(date(shiftdate), 'DAY'),3)"     +
                     "and  e.eid= "+ eid;

        Connection conn = DB.getConnection();
        String result = "<table border=1 width='950px'>";
        result += "<tr><th>EID</th><th>Name</th><th>shiftdate</th><th>Area</th><th>Hours</th></tr>";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                result += "<tr><td>"+ rs.getString("eid")       + "</td><td>"
                                    + rs.getString("Name")      + "</td><td>"
                                    + rs.getString("shiftdate") + "</td><td>"
                                    + rs.getString("areaname")  + "</td><td>"
                                    + rs.getString("hours")     + "</td>"
                                    + "</tr>";
                    }
            rs.close();
            st.close();

            DBUtils.closeDBConnection(conn);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        result += "</table>";
        return ok(result);
    }


public static Result getQualificationByEmployee(String eid_str) {

        int eid = Integer.parseInt(eid_str);


        String sql = "select e.eid, name, q.qualificationname, datereceived, achievementname  from tblhasqualification q   left join tblEmployee e on  q.eid = e.eid                                                                                                                               left join  tblachievementsrequiresforqualification a on a.qualificationname=q.qualificationname  where e.eid=" + eid;
        Connection conn = DB.getConnection();
        String result = "<table border=1 width='950px'>";
        result += "<tr><th>EID</th><th>Name</th><th>Qualification</th><th>Date Received</th><th>Achievement Completed</th></tr>";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                result += "<tr><td>"+ rs.getString("eid")       + "</td><td>"
                                    + rs.getString("Name")      + "</td><td>"
                                    + rs.getString("qualificationname") + "</td><td>"
                                    + rs.getString("datereceived") + "</td><td>"
                                    + rs.getString("achievementname")  + "</td>"
                                    + "</tr>";
                    }
            rs.close();
            st.close();

            DBUtils.closeDBConnection(conn);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        result += "</table>";
        return ok(result);
    }

}
