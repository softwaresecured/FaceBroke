package facebroke.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;



@Entity
@Table(name = "Users")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private String fname, lname, username;
	
	@OneToOne
	@JoinColumn(name = "wall_id")
	private Wall wall;
	
	public User() {}
	
	public User(String fname, String lname) {
		this.fname = fname;
		this.lname = lname;
	}
	
	public long getId() { return this.id;}
	
	public void addWall(Wall w) {
		w.setUser(this);
		this.wall = w;
	}
	
	public Wall getWall() {
		return this.wall;
	}
	
	public String getFname() { return this.fname; }
	public String getLname() { return this.lname; }
	
	public void setFname(String s) { this.fname = s; }
	public void setLname(String s) { this.lname = s; }
}
