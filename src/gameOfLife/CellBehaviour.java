package gameOfLife;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ControllerException;
import gameOfLife.Cell.CellState;

public class CellBehaviour extends CyclicBehaviour {
	private Cell cell;
	
	private void getStatusOfNeighbourCells() {
		ACLMessage msg;
		int x2, y2;
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (x == 1 && y == 1) {
					continue;
				} else {
					x2 = this.cell.getX() + x - 1;
					y2 = this.cell.getY() + y - 1;
					
					if (x2 == -1) {
						x2 += this.cell.getXBound();
					} else if (x2 == this.cell.getXBound()) {
						x2 -= this.cell.getXBound();
					}
					
					if (y2 == -1) {
						y2 += this.cell.getYBound();
					} else if (y2 == this.cell.getYBound()) {
						y2 -= this.cell.getYBound();
					}
					
					msg = new ACLMessage(ACLMessage.INFORM);
	    			msg.addReceiver(new AID("cellAgent_" + x2 + "_" + y2, AID.ISLOCALNAME));
	    			msg.setContent("isAlive");
	    			myAgent.send(msg);
				}
			}
		}
	}
	
	public CellBehaviour() {
		super();
	}
	
	public CellBehaviour(Cell a) {
		super(a);
		this.cell = a;
	}
	
	@Override
	public void action() {
		ACLMessage msgRx = myAgent.receive();
		String[] msgContent;
		String msgName;
		String msgValue;
		
		if (this.cell.getCellState() == CellState.START) {
			this.cell.setCellState(CellState.GETNEIGHBOURS);
			getStatusOfNeighbourCells();
		}
		
		if (this.cell.getCellState() == CellState.CALCULATED) {
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.addReceiver(new AID("gameController", AID.ISLOCALNAME));
			msg.setContent("cellFinished=true" +
					"_" + Integer.toString(this.cell.getX()) +
					"_" + Integer.toString(this.cell.getY()));
			myAgent.send(msg);
			
			this.cell.setCellState(CellState.FINISHED);
		}
		
	    if (msgRx != null) {
	    	msgContent = msgRx.getContent().split("=");
	    	msgName = msgContent[0];
	    	if (msgContent.length > 1) {
	    		msgValue = msgContent[1];
	    	} else {
	    		msgValue = "";
	    	}
	    	
	    	if (msgName.equals("isAlive")) {
				ACLMessage msgTx = msgRx.createReply();
				msgTx.setContent("cellState=" + Boolean.toString(this.cell.isAlive()) +
						"_" + Integer.toString(this.cell.getX()) +
						"_" + Integer.toString(this.cell.getY()));
				myAgent.send(msgTx);
	    	} else if (msgName.equals("setBounds")) {
				this.cell.setBounds(Integer.parseInt(msgValue.split("_")[0]), Integer.parseInt(msgValue.split("_")[1]));
	    	} else if (msgName.equals("cellState")) {
				this.cell.setNeighbourState(Boolean.parseBoolean(msgValue.split("_")[0]),
						Integer.parseInt(msgValue.split("_")[1]),
						Integer.parseInt(msgValue.split("_")[2]));
	    	} else if (msgName.equals("nextStage")) {
				this.cell.proceedToNextStage();
	    	} else if (msgName.equals("unlockCell")) {
				this.cell.unlockCell();
	    	} else {
	    		System.out.println(msgRx.getContent());
	    	}
	    } else {
	    	block();
	    }
	}
}
