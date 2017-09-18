package facebroke.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;



@Entity
@Table(name = "Posts")
public class Post {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@OneToOne
	@JoinColumn(name = "wall_id")
	private Wall wall;
	
	public Post() {}
	
	public long getId() { return this.id;}
	
	public void addWall(Wall w) {
		this.wall = w;
	}
	
	public Wall getWall() {
		return this.wall;
	}
}
