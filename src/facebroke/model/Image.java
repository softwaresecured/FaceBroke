package facebroke.model;

import java.time.ZonedDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Images")
public class Image {
	
	public enum Viewable {
		INVOLVED, All
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "creator_id")
	private User creator;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "owner_id")
	private User owner;
	
	private Viewable access;
	private byte[] content;
	private int size;
	private String label;
	
	private ZonedDateTime created, updated;
	
	
	public Image() {}


	public Image(User owner, User creator, Viewable access, byte[] content, int size, String label) {
		this.owner = owner;
		this.creator = creator;
		this.access = access;
		this.content = content;
		this.setSize(size);
		this.label = label;
		this.created = this.updated = ZonedDateTime.now();
	}


	public User getOwner() {
		return owner;
	}


	public void setOwner(User owner) {
		this.owner = owner;
		this.updated = ZonedDateTime.now();
	}


	public User getCreator() {
		return creator;
	}


	public void setCreator(User creator) {
		this.creator = creator;
		this.updated = ZonedDateTime.now();
	}


	public Viewable getAccess() {
		return access;
	}


	public void setAccess(Viewable access) {
		this.access = access;
		this.updated = ZonedDateTime.now();
	}


	public byte[] getContent() {
		return content;
	}


	public void setContent(byte[] content) {
		this.content = content;
		this.updated = ZonedDateTime.now();
	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
		this.updated = ZonedDateTime.now();
	}


	public long getId() {
		return id;
	}


	public ZonedDateTime getUpdated() {
		return updated;
	}


	public ZonedDateTime getCreated() {
		return created;
	}


	public int getSize() {
		return size;
	}


	public void setSize(int size) {
		this.size = size;
	}
}
