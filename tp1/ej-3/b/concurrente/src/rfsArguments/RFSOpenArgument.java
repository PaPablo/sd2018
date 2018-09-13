/**
 * 
 */
package rfsArguments;

/**
 * @author luciano
 *
 */
public class RFSOpenArgument extends RFSArgument {

	private String filename;
	
	public RFSOpenArgument(String filename) {
		this.setFilename(filename);
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}
