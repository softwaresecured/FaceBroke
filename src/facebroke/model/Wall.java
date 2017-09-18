package facebroke.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="Walls")
public class Wall {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@OneToOne
    @JoinColumn(name = "user_id")
	private User user;
	
	public Wall() {}
	
	public void setUser(User u) {
		this.user = u;
	}
	
	public User getUser() {
		return this.user;
	}
	
}
