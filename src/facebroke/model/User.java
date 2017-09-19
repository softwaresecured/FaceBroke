package facebroke.model;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import facebroke.util.AuthHelper;



@Entity
@Table(name = "Users")
public class User {
	
	// Instance variables to be serialized
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private String fname, lname, username, email;
	
	@OneToOne
	@JoinColumn(name = "wall_id")
	private Wall wall;
	
	private String b64Salt, b64Pass;
	
	private ZonedDateTime created, updated;

	
	// Hibernate constructor
	public User() {}
	
	/**
	 * Build a User object
	 * @param fname - String
	 * @param lname - String
	 * @param username - String
	 * @param email - String
	 */
	public User(String fname, String lname, String username, String email) {
		this.fname = fname;
		this.lname = lname;
		this.username = username;
		this.email = email;
		this.created = this.updated = ZonedDateTime.now();
	}
	
	
	// Getters and setters
	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Wall getWall() {
		return wall;
	}

	public void setWall(Wall wall) {
		this.wall = wall;
	}

	public String getB64Salt() {
		return b64Salt;
	}

	public void setB64Salt(String b64Salt) {
		this.b64Salt = b64Salt;
	}

	public String getB64Pass() {
		return b64Pass;
	}

	public void setB64Pass(String b64Pass) {
		this.b64Pass = b64Pass;
	}

	public ZonedDateTime getCreated() {
		return created;
	}

	public void setCreated(ZonedDateTime created) {
		this.created = created;
	}

	public ZonedDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(ZonedDateTime updated) {
		this.updated = updated;
	}

	public long getId() {
		return id;
	}

	/**
	 * Validate an input, plaintext password against the stored 
	 * salt and hash
	 * @param pass - UTF-8 plaintext password
	 * @return true if password match
	 */
	public boolean isPasswordValid(String pass) {
		if(this.b64Pass == null || this.b64Salt == null) {
			return false;
		}
		
		byte[] salt = AuthHelper.decodeBase64(this.b64Salt);
		
		String hashPassword = AuthHelper.hashPassword(pass, salt);
		
		return this.b64Pass == hashPassword;
	}
	
	/**
	 * Set a new password. Will also reset the salt
	 * @param password
	 */
	public void updatePassword(String password) {
		byte[] salt = AuthHelper.generateSalt(AuthHelper.SALTLENGTH);
		this.b64Pass = AuthHelper.hashPassword(password, salt);
		this.b64Salt = AuthHelper.encodeBase64(salt);
	}
}
