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


/**
 * JPA-annotated class to hold a Post and the assocaited metadata (creator, target, etc)
 * 
 */
@Entity
@Table(name = "Posts")
public class Post {

	/**
	 * An enum to represent the type stored in the 'content' string
	 * 
	 *   IMAGE -> an id of an Image is in 'content'
	 *   LINK -> simple string containing a link in 'content'
	 *   TEXT -> standard text post, all content should be HTML escaped
	 * 
	 */
	public enum PostType {
		IMAGE, LINK, TEXT
	}
	
	private ZonedDateTime created, updated;

	@OneToOne
	@JoinColumn(name = "creator_id")
	private User creator;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String content;

	@Enumerated(EnumType.STRING)
	private PostType type;

	@OneToOne
	@JoinColumn(name = "wall_id")
	private Wall wall;
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="parent", cascade=CascadeType.ALL)
	private List<Comment> comments = new ArrayList<>();
	

	// Private Hibernate constructor
	@SuppressWarnings("unused")
	private Post() {}

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

	public ZonedDateTime getCreated() {
		return created;
	}

	public void setCreated(ZonedDateTime created) {
		this.created = created;
		this.updated = ZonedDateTime.now();
	}

	public ZonedDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(ZonedDateTime updated) {
		this.updated = updated;
		this.updated = ZonedDateTime.now();
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
		this.updated = ZonedDateTime.now();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
		this.updated = ZonedDateTime.now();
	}

	public PostType getType() {
		return type;
	}

	public void setType(PostType type) {
		this.type = type;
		this.updated = ZonedDateTime.now();
	}

	public Wall getWall() {
		return wall;
	}

	public void setWall(Wall wall) {
		this.wall = wall;
		this.updated = ZonedDateTime.now();
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
		this.updated = ZonedDateTime.now();
	}

	public long getId() {
		return id;
	}
	
	public void deleteComment(Comment c) {
		this.comments.remove(c);
	}
}
