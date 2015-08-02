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

public class QualificationsController extends Controller {

        public static Result getAllQualifications() {
        String sql = "select  name from tblQualification";
        Connection conn = DB.getConnection();
        String result = "<select id='qualification_list'>";
         result += "<option value='all'>Show All</option>";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                result += "<option value='"+rs.getString("name")+"'>"
                        +rs.getString("name") +"</option>";
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



    public static Result getQualificationReport(String qualification) {


        String sql = "select e.eid, name, q.qualificationname, datereceived, achievementname  from tblhasqualification q " 
    				+ "left join tblEmployee e on  q.eid = e.eid  "
    				+ "left join  tblachievementsrequiresforqualification a on a.qualificationname=q.qualificationname ";

    	if (!qualification.equals("all")){
    		sql += " where q.qualificationname ="+ "'"+ qualification+ "'";
    		}			

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
