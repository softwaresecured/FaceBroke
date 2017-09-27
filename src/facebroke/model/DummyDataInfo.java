package facebroke.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DummyDataInfo {

	// Instance variables to be serialized
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String version;

	// Hibernate constructor
	public DummyDataInfo() {
	}
	
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

	
