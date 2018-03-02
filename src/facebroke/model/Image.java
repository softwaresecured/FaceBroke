package facebroke.model;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * JPA-annotated class to hold an Image (png, jpeg, etc.) and it's associated metadata
 * 
 */
@Entity
@Table(name = "Images")
public class Image {
	
	/**
	 * Represents who can view the image (permission level):
	 *   involved -> only the creator and owner
	 *   all -> any validated user of the site
	 *   
	 */
	public enum Viewable {
		INVOLVED, All
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@OneToOne
	@JoinColumn(name = "creator_id")
	private User creator;
	
	@OneToOne
	@JoinColumn(name = "owner_id")
	private User owner;
	
	private Viewable access;
	private byte[] content;
	private int size;
	private String label, contentType;
	
	private ZonedDateTime created, updated;
	
	// Private Hibernate constructor
	@SuppressWarnings("unused")
	private Image() {}


	/**
	 * Image constructor
	 * @param owner - User who owns the context of the image (i.e. owner of the Wall or the Owner of the profile)
	 * @param creator - User initiating the creation of the Images
	 * @param access - {@link Viewable}
	 * @param content - the byte[] holding the image
	 * @param size - the size in bytes of the image (size of byte[])
	 * @param label - the description of the image, optional
	 * @param type - the mimetype reported by the server upon upload
	 */
	public Image(User owner, User creator, Viewable access, byte[] content, int size, String label, String type) {
		this.owner = owner;
		this.creator = creator;
		this.access = access;
		this.content = content.clone();
		this.setSize(size);
		this.label = label;
		this.setContentType(type);
		this.created = this.updated = ZonedDateTime.now();
	}


	public User getCreator() {
		return creator;
	}


	public void setCreator(User creator) {
		this.creator = creator;
		this.updated = ZonedDateTime.now();
	}


	public User getOwner() {
		return owner;
	}


	public void setOwner(User owner) {
		this.owner = owner;
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


	public int getSize() {
		return size;
	}


	public void setSize(int size) {
		this.size = size;
		this.updated = ZonedDateTime.now();
	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
		this.updated = ZonedDateTime.now();
	}


	public String getContentType() {
		return contentType;
	}


	public void setContentType(String contentType) {
		this.contentType = contentType;
		this.updated = ZonedDateTime.now();
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
