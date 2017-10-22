package it.gp.sockets.remote.orvibo.communication;

import it.gp.sockets.remote.orvibo.control.OrviboAutomaton;
import it.gp.sockets.remote.orvibo.control.OrviboAutomaton.State;
import it.gp.sockets.remote.orvibo.interpretation.Message;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;
import javax.xml.bind.DatatypeConverter;

public class OrviboCommunication {
	public DatagramSocket serverSocket;
	public int port = 10000;
	// public final LinkedList<String> queue = new LinkedList<String>();
	public boolean stopserver = false;
	public String serverAddress = "192.168.1.8";
	public boolean statusOnlyMode;
	public int currentState = -1;
	public int tries = 2;
	public OrviboAutomaton onoffautomaton;

	public OrviboCommunication(String serverAddress){
		this.serverAddress=serverAddress;
	}
	
	public void switchStatus() throws Exception {
		execute(false);
	}

	public void retrieveStatus() throws Exception {
		execute(true);
	}
	
	public int getStatus() throws Exception {
		return currentState;
	}

	public void execute(boolean statusOnlyMode) throws Exception {
		System.out.println("Starting Orvibo Server");
		stopserver = false;
		serverSocket = null;
		serverSocket = new DatagramSocket(port);
		serverSocket.setSoTimeout(1000);
		this.statusOnlyMode = statusOnlyMode;
		onoffautomaton=new OrviboAutomaton();
		onoffautomaton.setInformativeMode(statusOnlyMode);
		currentState =-1;
		
		Thread t = new Thread(new ServerThread());
		t.start();
		System.out.println("Orbivo Server started");
		
		for (int i=0;i<tries;i++)
		{
			send(Message.discoveryMessage, serverAddress);
		}
		
		while (!stopserver) {
			Thread.sleep(200);
		}
	}

	public void shutdown() throws Exception {
		if (!serverSocket.isClosed())
			serverSocket.close();
		stopserver = true;
	}

	public void sendBroadcast(String hexmessage) throws Exception {
		send(hexmessage, "255.255.255.255");
	}

	public void send(String hexmessage, String address) throws Exception {
		DatagramSocket clientSocket = null;
		try {
			clientSocket = new DatagramSocket();
			InetAddress IPAddress = InetAddress.getByName(address);
			byte[] data = DatatypeConverter.parseHexBinary(hexmessage);
			DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, port);
			if (address.equals("255.255.255.255"))
				clientSocket.setBroadcast(true);
			clientSocket.setSoTimeout(3000);
			System.out.println("Orvibo sending:" + hexmessage);
			clientSocket.send(sendPacket);

		} catch (Exception e) {
			clientSocket.close();
		}
	}

	public void act(String hexmsg) throws Exception {

		String action = onoffautomaton.getAction(hexmsg);
		
		if (action != null && !stopserver){
			Thread.sleep(500);
			send(action, serverAddress);
		}
		
		if (onoffautomaton.currentState == OrviboAutomaton.State.STOP){
			currentState = onoffautomaton.getState();
			shutdown();
		}
	}

	class ServerThread implements Runnable {

		@Override
		public void run() {
			try {
				byte[] receiveData = new byte[42];
				int maxerrors = 3;
				int errorcounter = 1;
				while (!stopserver) {
					System.out.println("WAITING...");
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					try {
						serverSocket.receive(receivePacket);

//						System.out.println("RECEIVED: " + receivePacket.getData().length + " bytes");
						InetAddress IPAddress = receivePacket.getAddress();
						int rport = receivePacket.getPort();
						if (rport == port) {
							String hexmsg = DatatypeConverter.printHexBinary(receivePacket.getData());
//							String message = IPAddress + ":" + port + ":" + hexmsg;
//							System.out.println("RECEIVED MESSAGE: " + hexmsg);
							// System.out.println("RECEIVED MESSAGE: " + new
							// String(receivePacket.getData()));
							System.out.println("RECEIVED FROM: " + IPAddress + " Port " + rport);
							// queue.push(message);
							act(hexmsg);
						} else
							System.out.println("IGNORING MESSAGE");
					} catch (java.net.SocketTimeoutException e) {
						System.out.println("TIMEOUT");
						errorcounter++;
						if (errorcounter>=maxerrors)
							throw e;
					}
				}
			} catch (Exception e) {
				System.out.println(e.getLocalizedMessage());
			} finally {
				try {
					shutdown();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}

	}

	public static void main(String[] args) throws Exception {
		OrviboCommunication client = new OrviboCommunication("192.168.1.183");
		client.switchStatus();
		client.retrieveStatus();
		System.out.println("STATUS:" + client.getStatus());
	}
}
