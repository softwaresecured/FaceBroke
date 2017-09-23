package facebroke.model;

import java.time.ZonedDateTime;

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

	private String fname, lname, username, email;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Enumerated(EnumType.STRING)
	private UserRole role;

	@OneToOne
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
	public User(String fname, String lname, String username, String email) {
		this.fname = fname;
		this.lname = lname;
		this.username = username;
		this.email = email;
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
	}

	public void setB64Salt(String b64Salt) {
		this.b64Salt = b64Salt;
	}

	public void setCreated(ZonedDateTime created) {
		this.created = created;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public void setUpdated(ZonedDateTime updated) {
		this.updated = updated;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setWall(Wall wall) {
		this.wall = wall;
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
	}
}
