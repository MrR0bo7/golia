package gameOfLife;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;

public class CellBehaviour extends SimpleBehaviour {
	private boolean finished = true;
	
	public CellBehaviour(Agent a) {
		super(a);
	}
	
	@Override
	public void action() {
		System.out.println("New Cell: "+myAgent.getLocalName());
	}

	@Override
	public boolean done() {
		return finished;
	}

}
