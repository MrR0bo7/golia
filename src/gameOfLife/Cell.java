package gameOfLife;

import jade.core.Agent;

public class Cell extends Agent {
	@Override
	protected void setup() {
		addBehaviour(new CellBehaviour(this));
	}
}
