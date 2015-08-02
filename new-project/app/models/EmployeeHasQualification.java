package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;

import play.db.ebean.Model;

@Entity
@Table(name = "tblHasQualification")
public class EmployeeHasQualification extends Model{


public int eid;

public String dateReceived;

public String qualificationName;



@JoinColumn(name = "eid", referencedColumnName = "eid")
public Employee Employee ;


@JoinColumn(name = "qualificationName", referencedColumnName = "name")
public Qualification qualification ;

}