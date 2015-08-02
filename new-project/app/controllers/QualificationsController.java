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



    public static Result getQualificationReport() {


        String sql = "select qualificationName, count(e.eid) as number_of_employees  " +
        			 "from tblhasqualification q, tblEmployee e                  " +                                    
        			 "where q.eid=e.eid group by qualificationname  ";           
		

        Connection conn = DB.getConnection();
        String result = "<table border=1 width='950px'>";
        result += "<tr><th>Qualification</th><th>Number of Employees</th></tr>";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                result += "<tr><td>"+ rs.getString("qualificationname")       + "</td><td>"
                                    + rs.getString("number_of_employees")      + "</td>"
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
