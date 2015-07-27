package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

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
}
