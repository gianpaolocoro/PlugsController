package it.gp.sockets.remote.orvibo.communication;

import it.gp.sockets.remote.orvibo.commons.OrviboSocket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class OrviboSearcher {
	int status = 0;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public OrviboSearcher() {
	}

	public List<OrviboSocket> searchAllDevices(int min, int max) throws Exception {
		status = 0;
		List<OrviboSocket> devices = new ArrayList<OrviboSocket>();

		try {
			InetAddress localhost = InetAddress.getLocalHost();
			byte[] ip = localhost.getAddress();
			InetAddress[] allMyIps = InetAddress.getAllByName(localhost.getCanonicalHostName());
			List<byte[]> baseaddresses = new ArrayList<byte[]>();
			// baseaddresses.add(ip);
			boolean baseAddress=false;
			for (InetAddress myip : allMyIps) {
				baseaddresses.add(myip.getAddress());
				if (myip.getHostAddress().startsWith("192.168.1."))
				{
					baseAddress=true;
				}
			}
			if (!baseAddress){
				try{baseaddresses.add(InetAddress.getByName("192.168.1.1").getAddress());}catch(Exception e){System.out.println("Impossible to retrieve base address");}
			}
			int neths=baseaddresses.size();
			int eth = 0;
			for (byte[] myip : baseaddresses) {
				ip = myip;
				for (int i = min; i <= max; i++) {
					status = Math.min(90, (eth*100/neths)+(i * 100 / (max*neths)));

					ip[3] = (byte) i;
					InetAddress address = InetAddress.getByAddress(ip);
					System.out.println("Trying..." + address);

					if (isReachable(address.getHostAddress())) {
						OrviboCommunication checker = new OrviboCommunication(address.getHostAddress());
						checker.retrieveStatus();
						if (checker.getStatus() > -1) {
							devices.add(new OrviboSocket(address.getHostAddress(), checker.getStatus()));
						}
					}
				}
				eth++;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			status = 100;
		}

		return devices;
	}

	public static boolean isReachable(String ipaddress) {
		boolean reachable = false;
		try {
			Runtime rt = Runtime.getRuntime();
			//String command = "ping -n 1 " + ipaddress;
			String command = "ping -c 1 " + ipaddress;
			Process proc = rt.exec(command);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

			// read the output from the command
			String s = null;

			while ((s = stdInput.readLine()) != null) {
				 System.out.println(s);
				if (s.contains("bytes=")||s.contains("byte=")) {
					reachable = true;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reachable;
	}

	public static void main(String args[]) throws Exception {
		List<OrviboSocket> found = new OrviboSearcher().searchAllDevices(1,5);
		System.out.println("FOUND " + found);
	}
}
