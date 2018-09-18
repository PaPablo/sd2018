package exceptions;

public class CouldNotWriteFileException extends Exception {

	private String msg;
	public CouldNotWriteFileException(String msg) {
		this.setMsg(msg);
	}
	
	@Override
	public String getMessage() {
		return this.getMsg();
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
