package facebroke.model;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Posts")
public class Post {

	public enum PostType {
		IMAGE, LINK, TEXT
	}

	private ZonedDateTime created, updated;

	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "creator_id")
	private User creator;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String content;

	@Enumerated(EnumType.STRING)
	private PostType type;

	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "wall_id")
	private Wall wall;
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="parent", cascade=CascadeType.ALL)
	private List<Comment> comments = new ArrayList<>();
	

	// Special constructor for Hibernate
	public Post() {
	}

	/**
	 * Build a post
	 * 
	 * @param wall
	 *            - target wall to be posted to
	 * @param creator
	 *            - creating user
	 * @param title
	 *            - post title
	 * @param type
	 *            - post enum type
	 * @param content
	 *            - post content, either plaintext or Base64 encoded, based on type
	 */
	public Post(Wall wall, User creator, PostType type, String content) {
		this.wall = wall;
		this.creator = creator;
		this.type = type;
		this.content = content;
		this.created = this.updated = ZonedDateTime.now();
	}

	public String getContent() {
		return content;
	}

	public ZonedDateTime getCreated() {
		return created;
	}

	public User getCreator() {
		return creator;
	}

	public long getId() {
		return id;
	}

	public PostType getType() {
		return type;
	}

	public ZonedDateTime getUpdated() {
		return updated;
	}

	public Wall getWall() {
		return wall;
	}
	
	public List<Comment> getComments(){
		return comments;
	}

	public void setContent(String content) {
		this.content = content;
		this.updated = ZonedDateTime.now();
	}

	public void setCreator(User creator) {
		this.creator = creator;
		this.updated = ZonedDateTime.now();
	}

	public void setType(PostType type) {
		this.type = type;
		this.updated = ZonedDateTime.now();
	}

	public void setWall(Wall wall) {
		this.wall = wall;
		this.updated = ZonedDateTime.now();
	}
}
