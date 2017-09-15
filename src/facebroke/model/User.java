package facebroke.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "Users")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private String fname, lname;
	
	public User() {}
	
	public User(String fname, String lname) {
		this.fname = fname;
		this.lname = lname;
	}
	
	public long getId() { return this.id;}
		
	private void setId(long id) { this.id = id; }
	
	public String getFname() { return this.fname; }
	public String getLname() { return this.lname; }
	
	public void setFname(String s) { this.fname = s; }
	public void setLname(String s) { this.lname = s; }
}
