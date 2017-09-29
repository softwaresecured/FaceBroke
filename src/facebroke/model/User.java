package facebroke.model;

import java.time.ZonedDateTime;
import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

	public enum UserRole {
		ADMIN, USER
	}

	private String b64Salt, b64Pass;

	private ZonedDateTime created, updated;
	
	private Calendar dob;

	private String fname, lname;
	
	@Column(unique=true)
	private String username, email;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Enumerated(EnumType.STRING)
	private UserRole role;

	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "wall_id")
	private Wall wall;

	// Hibernate constructor
	public User() {
	}

	/**
	 * Build a User object
	 * 
	 * @param fname
	 *            - String
	 * @param lname
	 *            - String
	 * @param username
	 *            - String
	 * @param email
	 *            - String
	 */
	public User(String fname, String lname, String username, String email, Calendar dob) {
		this.fname = fname;
		this.lname = lname;
		this.username = username;
		this.email = email;
		this.dob = dob;
		this.created = this.updated = ZonedDateTime.now();
		this.role = UserRole.USER;
	}

	public String getB64Pass() {
		return b64Pass;
	}

	public String getB64Salt() {
		return b64Salt;
	}

	public ZonedDateTime getCreated() {
		return created;
	}

	public Calendar getDOB() {
		return dob;
	}
	
	public String getDOBString() {
		return String.format("%d-%02d-%02d", this.dob.get(Calendar.YEAR), this.dob.get(Calendar.MONTH), this.dob.get(Calendar.DATE));
	}
	
	public String getEmail() {
		return email;
	}

	public String getFname() {
		return fname;
	}

	public long getId() {
		return id;
	}

	public String getLname() {
		return lname;
	}

	public UserRole getRole() {
		return this.role;
	}

	public ZonedDateTime getUpdated() {
		return updated;
	}

	public String getUsername() {
		return username;
	}

	public Wall getWall() {
		return wall;
	}

	/**
	 * Validate an input, plaintext password against the stored salt and hash
	 * 
	 * @param pass
	 *            - UTF-8 plaintext password
	 * @return true if password match
	 */
	public boolean isPasswordValid(String pass) {
		if (this.b64Pass == null || this.b64Salt == null) {
			return false;
		}

		byte[] salt = AuthHelper.decodeBase64(this.b64Salt);

		String hashPassword = AuthHelper.hashPassword(pass, salt);

		return this.b64Pass.equals(hashPassword);
	}

	public void setB64Pass(String b64Pass) {
		this.b64Pass = b64Pass;
		this.updated = ZonedDateTime.now();
	}

	public void setB64Salt(String b64Salt) {
		this.b64Salt = b64Salt;
		this.updated = ZonedDateTime.now();
	}
	
	public void setDOB(Calendar d) {
		this.dob = d;
		this.updated = ZonedDateTime.now();
	}

	public void setEmail(String email) {
		this.email = email;
		this.updated = ZonedDateTime.now();
	}

	public void setFname(String fname) {
		this.fname = fname;
		this.updated = ZonedDateTime.now();
	}

	public void setLname(String lname) {
		this.lname = lname;
		this.updated = ZonedDateTime.now();
	}

	public void setRole(UserRole role) {
		this.role = role;
		this.updated = ZonedDateTime.now();
	}
	
	public void setUsername(String username) {
		this.username = username;
		this.updated = ZonedDateTime.now();
	}

	public void setWall(Wall wall) {
		this.wall = wall;
		this.updated = ZonedDateTime.now();
	}

	/**
	 * Set a new password. Will also reset the salt
	 * 
	 * @param password
	 */
	public void updatePassword(String password) {
		byte[] salt = AuthHelper.generateSalt(AuthHelper.SALTLENGTH);
		this.b64Pass = AuthHelper.hashPassword(password, salt);
		this.b64Salt = AuthHelper.encodeBase64(salt);
		this.updated = ZonedDateTime.now();
	}
}
