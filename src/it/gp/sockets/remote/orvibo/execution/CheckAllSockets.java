package it.gp.sockets.remote.orvibo.execution;

import it.gp.sockets.remote.orvibo.control.PlugsController;

public class CheckAllSockets {

	public static void main(String[] args) throws Exception{
		PlugsController controller = new PlugsController();
		controller.checkAllSockets();
	}

}
