package facebroke.model;

import javax.persistence.Entity;
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
	
	private PostType type;
	
	private String titleContent;
	
	//private Object content;
	
	public Post() {}
	
	public long getId() { return this.id;}
	
	public void addWall(Wall w) {
		this.wall = w;
	}
	
	public Wall getWall() {
		return this.wall;
	}
	
	public void addCreator(User c) {
		this.creator = c;
	}
	
	public User getCreator() {
		return this.creator;
	}
	
	public void setTitleContent(String t) {
		this.titleContent = t;
	}
	
	public String getTitleContent() {
		return this.titleContent;
	}
	
	/*
	public void setContent(String c) {
		this.content = c;
	}
	
	public Object getContent() {
		return this.content;
	}*/
	
	public void setPostType(PostType t) {
		this.type = t;
	}
	
	public PostType getPostType() {
		return this.type;
	}
}
