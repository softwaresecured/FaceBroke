package facebroke.util;

public class FacebrokeException extends Exception {
	private static final long serialVersionUID = 1L;

	public FacebrokeException() {}
	
	public FacebrokeException(String msg) {
		super(msg);
	}
}
