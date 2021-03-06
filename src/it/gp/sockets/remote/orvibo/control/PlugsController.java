package it.gp.sockets.remote.orvibo.control;

import it.gp.sockets.remote.orvibo.commons.OrviboSocket;
import it.gp.sockets.remote.orvibo.communication.MailSender;
import it.gp.sockets.remote.orvibo.communication.OrviboCommunication;
import it.gp.sockets.remote.orvibo.communication.OrviboSearcher;

import java.util.List;

public class PlugsController {
	public List<it.gp.sockets.remote.orvibo.commons.OrviboSocket> sockets;
	
	StringBuffer log = new StringBuffer();
	String[] logaddresses = {"ashtoash@gmail.com", "valebarta@yahoo.it"};
//	String[] logaddresses = {"ashtoash@gmail.com"};
	
	public synchronized int getStatus(int socketIdx) {
		return sockets.get(socketIdx).getStatus();
	}

	public PlugsController() throws Exception {

	}
	
	String getStatusName(int status){
		if (status==0)
			return "OFF";
		else
			return "ON";
	} 
	
	public int checkAll() throws Exception{
		System.out.println("Retrieving sockets");
		retrieveSockets();
		int nsocks = sockets.size();
		System.out.println("**************Retrieved "+nsocks+" sockets*************");
		log.append("There are "+nsocks+" sockets active in the house\n");
		System.out.println("Found sockets: "+sockets.size());
		System.out.println("Active Sockets: "+sockets);
		int sockn = 0;
		
		for (OrviboSocket socket:sockets){
			System.out.println("Found Socket: "+socket.getAddress()+" Status "+socket.getStatus() + " N. "+sockn+" of "+nsocks);
			log.append("The socket with address "+socket.getAddress()+" is in "+getStatusName(socket.getStatus())+" status \n");
			sockn++;
		}
		if (nsocks==0)
			log.append("I could not report any socket.\n");
		return sockn;
	}
	
	public int switchAll(int fromStatus) throws Exception{
		System.out.println("Retrieving sockets");
		retrieveSockets();
		int nsocks = sockets.size();
		System.out.println("**************Retrieved "+nsocks+" sockets*************");
		log.append("There are "+nsocks+" sockets active in the house\n");
		System.out.println("Found sockets: "+sockets.size());
		System.out.println("Active Sockets: "+sockets);
		int sockn = 0;
		int switched = 0;
		for (OrviboSocket socket:sockets){
			System.out.println("Found Socket: "+socket.getAddress()+" Status "+socket.getStatus() + " N. "+sockn+" of "+nsocks);
			if (socket.getStatus()==fromStatus){
				switchSocket(sockn);
				System.out.println("Switched to "+socket.getStatus());
				log.append("The socket with address "+socket.getAddress()+" was switched from "+getStatusName(fromStatus)+" to "+getStatusName(socket.getStatus())+"\n");
				switched++;
			}
			else{
				System.out.println("Left to "+socket.getStatus());
				log.append("The socket with address "+socket.getAddress()+" was left in "+getStatusName(socket.getStatus())+" status\n");
			}
			sockn++;
		}
		if (nsocks==0)
			log.append("I could not make any action.\n");
		return switched;
	}
	public void switchSocket(int number) throws Exception {
		OrviboSocket socket = sockets.get(number);
		OrviboCommunication communication = new OrviboCommunication(socket.getAddress());
		communication.switchStatus();
		socket.setStatus(communication.getStatus());
	}
	
	
	public void retrieveSockets() throws Exception{
		OrviboSearcher searcher = new OrviboSearcher();
		sockets = searcher.searchAllDevices(1,255);
	}
	
	public void checkAllSockets() throws Exception{
		System.out.println("CHECKING ALL SOCKETS!");
		int checked  = checkAll();
		System.out.println("CHECKING ALL SOCKETS FINISHED - SOCKETS "+checked);
	}
	
	public void statusAlerterSockets() throws Exception{
		System.out.println("CHECKING ALL SOCKETS!");
		int checked  = checkAll();
		MailSender.sendJavaMail("STATUS REPORT", log.toString(), logaddresses);
		System.out.println("CHECKING ALL SOCKETS FINISHED - SOCKETS "+checked);
	}
	
	public void turnOnAll() throws Exception{
		System.out.println("TURING ALL SOCKETS ON!");
		int switched  = switchAll(0);
		MailSender.sendJavaMail("ACTIVATION REPORT", log.toString(), logaddresses);
		System.out.println("TURING ALL SOCKETS ON FINISHED - SWITCHED SOCKETS "+switched);
	}
	
	public void turnOffAll() throws Exception{
		System.out.println("TURING ALL SOCKETS OFF!");
		int switched  = switchAll(1);
		MailSender.sendJavaMail("DEACTIVATION REPORT", log.toString(), logaddresses);
		System.out.println("TURING ALL SOCKETS OFF FINISHED - SWITCHED SOCKETS "+switched);
	}
	
}
