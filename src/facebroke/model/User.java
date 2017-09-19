package facebroke.model;

import java.util.Base64;

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
	
	private Base64 salt, pass;

	public User() {}
	
	public User(String fname, String lname) {
		this.fname = fname;
		this.lname = lname;
	}
	
	public long getId() { return this.id;}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Base64 getSalt() {
		return salt;
	}

	public void setSalt(Base64 salt) {
		this.salt = salt;
	}

	public Base64 getPass() {
		return pass;
	}

	public void setPass(Base64 pass) {
		this.pass = pass;
	}

	public void setWall(Wall wall) {
		this.wall = wall;
	}
	
	public Wall getWall() {
		return this.wall;
	}
	
	public String getFname() { return this.fname; }
	public String getLname() { return this.lname; }
	
	public void setFname(String s) { this.fname = s; }
	public void setLname(String s) { this.lname = s; }
}
