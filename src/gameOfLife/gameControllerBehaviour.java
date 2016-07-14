package gameOfLife;

import java.util.Timer;
import java.util.TimerTask;

import gameOfLife.Cell.CellState;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;
import gameOfLife.gameController.CellStates;
import gameOfLife.gameController.GameState;

public class gameControllerBehaviour extends CyclicBehaviour {
	private gameController game;
	Timer timer = new Timer();

	public void getStatusOfCells() {
		this.game.setGameState(GameState.GETCELLSTATES);
		
		ACLMessage msg;
		for (int x = 0; x < this.game.getXBound(); x++) {
			for (int y = 0; y < this.game.getYBound(); y++) {
				msg = new ACLMessage(ACLMessage.INFORM);
    			msg.addReceiver(new AID("cellAgent_" + x + "_" + y, AID.ISLOCALNAME));
    			msg.setContent("isAlive");
    			myAgent.send(msg);
			}
		}
	}
	
	private void generateCells() {
		ACLMessage msg;
		String agentName;

		msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(new AID("cellPrototype", AID.ISLOCALNAME));
		msg.setContent("setBounds=" + this.game.getXBound() + "_" + this.game.getYBound());
		myAgent.send(msg);
		
		try {
			// wait for the bounds to be set
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for (int x = 0; x < this.game.getXBound(); x++){
			for (int y = 0; y < this.game.getYBound(); y++) {
				agentName = "cellAgent_" + x + "_" + y;
	    		try {
	    			// create agent cell
	    			myAgent.getContainerController().getAgent("cellPrototype").clone(myAgent.here(), agentName);
	    			// wait for the cells to get cloned
	    			Thread.sleep(10);
	    		} catch (ControllerException | InterruptedException e) {
					e.printStackTrace();
	    		}
			}
		};
		
		this.game.setGameState(GameState.FINISHED);
	}
	
	public void moveCellsToNextStage() {
		if (this.game.getCellStates() == CellStates.FINISHED) {
			ACLMessage msg;
			for (int x = 0; x < this.game.getXBound(); x++) {
				for (int y = 0; y < this.game.getYBound(); y++) {
					msg = new ACLMessage(ACLMessage.INFORM);
	    			msg.addReceiver(new AID("cellAgent_" + x + "_" + y, AID.ISLOCALNAME));
	    			msg.setContent("nextStage");
	    			myAgent.send(msg);
				}
			}
		} else {
			System.out.println("Cells are not ready.");
		}
	}
	
	public void unlockCells() {
		this.game.setCellStates(CellStates.GETSTATUS);
		
		ACLMessage msg;
		for (int x = 0; x < this.game.getXBound(); x++) {
			for (int y = 0; y < this.game.getYBound(); y++) {
				msg = new ACLMessage(ACLMessage.INFORM);
    			msg.addReceiver(new AID("cellAgent_" + x + "_" + y, AID.ISLOCALNAME));
    			msg.setContent("unlockCell");
    			myAgent.send(msg);
			}
		}
	}
	
	private void run() {
		timer.scheduleAtFixedRate(new GameTimer(this), 1000, this.game.getCycle());
	}
	
	public gameControllerBehaviour() {
		super();
	}
	
	public gameControllerBehaviour(gameController a) {
		super(a);
		this.game = a;
	}

	@Override
	public void action() {
		ACLMessage msgRx = myAgent.receive();
		String[] msgContent;
		String msgName;
		String msgValue;
		
		if (this.game.getGameState() == GameState.STARTED) {
			this.generateCells();
			this.unlockCells();
			this.getStatusOfCells();
			this.run();
		}
		
	    if (msgRx != null) {
	    	msgContent = msgRx.getContent().split("=");
	    	msgName = msgContent[0];
	    	if (msgContent.length > 1) {
	    		msgValue = msgContent[1];
	    	} else {
	    		msgValue = "";
	    	}
	    	if (msgName.equals("generateCells")) {
	    		this.generateCells();
	    	} else if (msgName.equals("printGameBoard")) {
				this.getStatusOfCells();
	    	} else if (msgName.equals("cellState")) {
				this.game.setGameBoard(Boolean.parseBoolean(msgValue.split("_")[0]),
						Integer.parseInt(msgValue.split("_")[1]),
						Integer.parseInt(msgValue.split("_")[2]));
	    	} else if (msgName.equals("cellFinished")) {
				this.game.setCellFinished(Boolean.parseBoolean(msgValue.split("_")[0]),
						Integer.parseInt(msgValue.split("_")[1]),
						Integer.parseInt(msgValue.split("_")[2]));
	    	} else if (msgName.equals("nextStage")) {
				this.moveCellsToNextStage();
				this.unlockCells();
				this.getStatusOfCells();
	    	} else if (msgName.equals("unlockCells")) {
				this.unlockCells();
				this.getStatusOfCells();
	    	} else if (msgName.equals("run")) {
				this.run();
	    	} else {
	    		System.out.println(msgRx.getContent());
	    	}
	    } else {
	    	block();
	    }
	}
}
