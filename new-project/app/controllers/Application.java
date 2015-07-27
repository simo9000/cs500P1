package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {
<<<<<<< HEAD
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
=======
        return ok(index.render("Your new application is ready."));
    }

>>>>>>> 13e5837b1b90dfdff3e6498307e3c1fd4305c561
}
