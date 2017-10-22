package it.gp.sockets.remote.orvibo.commons;

public class OrviboSocket {

	String address;
	int status;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public OrviboSocket(String address, int status){
		this.address = address;
		this.status = status;
	}
	
	public String toString(){
		return address+":"+status;
	}
	
	public static OrviboSocket fromString(String string){
		String[]split = string.split(":");
		return new OrviboSocket(split[0], Integer.parseInt(split[1]));
	}

}
