package controllers;


import models.*;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.db.DB;
import play.db.ebean.Model;
import play.mvc.*;
import utils.DBUtils;
import views.html.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


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


    // Adding a Employee to your model/table
    public static Result addEmployee() {
        Employee employee = Form.form(Employee.class).bindFromRequest().get();
        employee.save();
        return redirect(routes.Application.index());
    }
    

     // Getting table data using model
    public static Result employeeTable() {
        List <Employee> employees = new Model.Finder(int.class, Employee.class).all();
        String table = "<table border=1px solid>"
                + "<tr>"
                + "<th>Employee ID</th>"
                + "<th>Name</th>"
                + "<th>Date Of birth</th>"
                + "</tr>";
        for(Employee employee: employees) {
            table += "<tr>";
            table += "<td>" + employee.eid+ "</td>";
            table += "<td>" + employee.name + "</td>";
            table += "<td>" + employee.dob + "</td>";
            table += "</tr>";           
        }
        table += "</table>";
        return ok(table);
    }
    
    // Adding a Achievement to your model/table
    public static Result addAchievement() {
        Achievement achievement = Form.form(Achievement.class).bindFromRequest().get();
        achievement.save();
        return redirect(routes.Application.index());
    }
    
     // Getting table data using model
    public static Result achievementTable() {
        List <Achievement> achievements = new Model.Finder(int.class, Achievement.class).all();
        String table = "<table border=1px solid>"
                + "<tr>"
                + "<th>Name</th>"
                + "<th>Description</th>"
                + "</tr>";
        for(Achievement achievement: achievements) {
            table += "<tr>";
            table += "<td>" + achievement.name+ "</td>";
            table += "<td>" + achievement.description + "</td>";
            table += "</tr>";           
        }
        table += "</table>";
        return ok(table);
    }


    
}
