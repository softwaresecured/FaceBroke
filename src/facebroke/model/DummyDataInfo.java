package facebroke.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Very simple JPA-annotated class to hold the version info of the Dummy Data in the DB.
 * Main use case is as follows:
 *   - User attempts login
 *   - No Hibernate SessionFActory exists so a new one must start
 *   - On startup, Hibernate looks for version info on Dummy Data
 *   - Since its the last object entered into the DB on dummy generation, its presence implies a successful DB load
 *   - If an instance of this object is not found in the DB, then the DB must not be in a loaded state, so run the Loader.java
 * 
 */
@Entity
public class DummyDataInfo {

	// Instance variables to be serialized
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String version;

	// Private Hibernate constructor
	@SuppressWarnings("unused")
	private DummyDataInfo() {}
	
	/**
	 * Hibernate object to hold version info about the Dummy DB
	 * @param version
	 */
	public DummyDataInfo(String version) {
		this.version = version;
	}

	public String getVersion() {
		return version;
	}
}

	
