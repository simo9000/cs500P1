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

public class ScheduleController extends Controller {

    public static Result getAreaSchedule(String area){
        return ok();
    }
    
}