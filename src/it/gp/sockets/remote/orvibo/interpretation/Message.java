package it.gp.sockets.remote.orvibo.interpretation;

import javax.xml.bind.DatatypeConverter;

public class Message {

	public String magicKey;
	public int msgLen;
	public String macAddress;
	public String macAddressPadding;
	public String command;
	public String macAddressLittleEndian;
	public String macAddressLittleEndianPadding;
	public String socketCode;
	public int timeManifacture;
	public String auxDigit;
	public int state;

	public String auxMessage;
	
	
	public static String discoveryMessage= "686400067161";
	
	public static String parseCommand(String hexmsg, String macaddress) {
		String magicKey = hexmsg.substring(0, 4);
		int msgLen = Integer.parseInt(hexmsg.substring(4, 8), 16);
		int macidx = 14;
		String command = null;
		if (macaddress != null)
			macidx = hexmsg.indexOf(macaddress);

		command = hexmsg.substring(8, macidx);
		return command;
	}

	public void parseAckMsg(String hexmsg) {
		magicKey = hexmsg.substring(0, 4);
		msgLen = Integer.parseInt(hexmsg.substring(4, 8), 16);
		int macidx = 14;
		command = hexmsg.substring(8, macidx);
		macAddress = hexmsg.substring(macidx, macidx + 12);
		macAddressPadding = hexmsg.substring(macidx, macidx + 24);
		macAddressLittleEndian = hexmsg.substring(macidx + 24, macidx + 24 + 12);
		macAddressLittleEndianPadding = hexmsg.substring(macidx + 24, macidx + 24 + 24);
		socketCode = new String(DatatypeConverter.parseHexBinary(hexmsg.substring(macidx + 24 + 24, macidx + 24 + 24 + 12)));
		timeManifacture = Integer.parseInt(hexmsg.substring(macidx + 24 + 24 + 12, macidx + 24 + 24 + 12 + 6), 16);
		auxDigit = hexmsg.substring(macidx + 24 + 24 + 12 + 6, macidx + 24 + 24 + 12 + 8);
		state = Integer.parseInt(hexmsg.substring(macidx + 24 + 24 + 12 + 8, macidx + 24 + 24 + 12 + 10), 16);
	}

	public void parseMsg(String hexmsg, String macAddress) {
		magicKey = hexmsg.substring(0, 4);
		msgLen = Integer.parseInt(hexmsg.substring(4, 8), 16);
		int macidx = hexmsg.indexOf(macAddress);
		command = hexmsg.substring(8, macidx);
		this.macAddress = hexmsg.substring(macidx, macidx + 12);
		macAddressPadding = hexmsg.substring(macidx, macidx + 24);
		auxMessage = hexmsg.substring(macidx + 24, macidx + 24 + 8);
		state = Integer.parseInt(hexmsg.substring(macidx + 24 + 8, macidx + 24 + 10), 16);
//		session = hexmsg.substring(macidx + 24 + 12, msgLen * 2);
	}

	
	public static String turnOnMsg(String macAddressPadding){
		return "686400176463"+macAddressPadding+"0000000001";
	}
	
	public static String turnOffMsg(String macAddressPadding){
		return "686400176463"+macAddressPadding+"0000000000";
	}
	
	
	public static String subscribe(String macAddressPadding, String macAddressLittleEndianPadding){
		return ("6864001e636c"+macAddressPadding+macAddressLittleEndianPadding).toLowerCase();
	}
}
