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
@Table(name = "tblShift")
public class Shift extends Model {

	@Column(name="date_")
	public String date;

	@Column(name="isHoliday")
	public boolean isHoliday;



}
