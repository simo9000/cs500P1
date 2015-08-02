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
@Table(name = "tblAssigment")
public class Assignment extends Model {

	@Column(name="A_start")
	public String aStart;

	@Column(name="A_end")
	public String aEnd;



}
