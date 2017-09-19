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



@Entity
@Table(name = "Posts")
public class Post {
	
	public enum PostType{
		IMAGE,
		TEXT,
		LINK
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@OneToOne
	@JoinColumn(name = "wall_id")
	private Wall wall;
	
	@OneToOne
	@JoinColumn(name = "creator_id")
	private User creator;
	
	@Enumerated(EnumType.STRING)
	private PostType type;
	
	private String title, content;
	
	private ZonedDateTime created, updated;
	
	
	//Special constructor for Hibernate
	public Post() {}
	
	/**
	 * Build a post
	 * @param wall -  target wall to be posted to
	 * @param creator - creating user
	 * @param title - post title
	 * @param type - post enum type
	 * @param content - post content, either plaintext or Base64 encoded, based on type
	 */
	public Post(Wall wall, User creator, String title, PostType type, String content) {
		this.wall = wall;
		this.creator = creator;
		this.title = title;
		this.type = type;
		this.content = content;
		this.created = this.updated = ZonedDateTime.now();
	}

	
	// Getters and Setters
	public Wall getWall() {
		return wall;
	}

	public void setWall(Wall wall) {
		this.wall = wall;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public PostType getType() {
		return type;
	}

	public void setType(PostType type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public ZonedDateTime getCreated() {
		return created;
	}
}
