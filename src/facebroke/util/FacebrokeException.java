package facebroke.util;

/**
 * A simple Exception to represent an error specific to the functionality of FaceBroke
 */
public class FacebrokeException extends Exception {
	private static final long serialVersionUID = 1L;

	public FacebrokeException() {}
	
	public FacebrokeException(String msg) {
		super(msg);
	}
}
