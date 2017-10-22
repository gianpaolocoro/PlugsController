package it.gp.sockets.remote.orvibo.control;

import it.gp.sockets.remote.orvibo.interpretation.Message;

public class OrviboAutomaton {
	public enum State {
		START, INIT, OFF, ON, SUBSCRIBED, STOP
	};

	public Message initMsg;
	public State currentState;
	public int currentStatus = -1;
	public String discoveryCommand = "716100";
	public String subscribeCommand = "636C";
	public String statusCommand = "7366";
	public boolean informativeMode = false;
	public OrviboAutomaton() {
		currentState = State.START;
	}

	public int getState() {
		return currentStatus;
	}
	
	public void setInformativeMode(boolean informativeMode){
		this.informativeMode = informativeMode;
	}
	

	public String getAction(String msg) {
		String macAddress = null;
		if (initMsg != null)
			macAddress = initMsg.macAddress;

		String command = Message.parseCommand(msg, macAddress);
		System.out.println("COMMAND: " + command);
		String action = null;
		if (command.equals(discoveryCommand)) {
			if (currentState == State.START) {
				initMsg = new Message();
				initMsg.parseAckMsg(msg);
				action = Message.subscribe(initMsg.macAddressPadding, initMsg.macAddressLittleEndianPadding);
				currentState = State.INIT;
				if (informativeMode){
					currentState = State.STOP;
					action = null;
					currentStatus = initMsg.state;
				}
			}
		} else if (command.equals(subscribeCommand)) {
			if (currentState == State.INIT) {
				if (initMsg.state == 1) {
					action = Message.turnOffMsg(initMsg.macAddressPadding);
					currentState = State.OFF;
				} else {
					action = Message.turnOnMsg(initMsg.macAddressPadding);
					currentState = State.ON;
				}
			}
		}

		else if (command.equals(statusCommand)) {
			if (currentState == State.OFF || currentState == State.ON) {
				Message message = new Message();
				message.parseMsg(msg, initMsg.macAddress);
				currentStatus = message.state;
				initMsg = null;
				currentState = State.STOP;
			}
		}

		return action;
	}

}
