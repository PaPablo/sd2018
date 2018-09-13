package exceptions;

public class CouldNotReadFileException extends Exception{

	private String msg;
	public CouldNotReadFileException(String msg) {
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
