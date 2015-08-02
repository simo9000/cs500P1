package models;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import play.db.ebean.Model;

@Entity
@Table(name = "tblEmployee")
public class Employee extends Model {

	@Id
	@Column(name = "eid")
   @SequenceGenerator(name="gen", sequenceName="tblEmployee_eid_seq",allocationSize=1) 
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
	public int eid;

	public String name;

	public String dob;


}
