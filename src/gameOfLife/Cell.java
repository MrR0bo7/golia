package gameOfLife;

import jade.core.Agent;

public class Cell extends Agent {
	private int x;
	private int y;
	private int xBound;
	private int yBound;
	private boolean alive = true;
	private Boolean aliveInNextStage = null;
	private Boolean[][] neighbourCell = new Boolean[3][3];
	public enum CellState {
		START,
		GETNEIGHBOURS,
		CALCULATENEXTSTAGE,
		CALCULATED,
		FINISHED
	};
	private CellState state = CellState.FINISHED;
	
	@Override
	protected void setup() {
		addBehaviour(new CellBehaviour(this));
	}
	
	@Override
	protected void afterClone() {
		String[] agentname = this.getLocalName().split("_");
		this.setLocation(Integer.parseInt(agentname[1]), Integer.parseInt(agentname[2]));
		
		if (Math.random() < 0.3) {
			this.alive = true;
		} else {
			this.alive = false;
		}
		
		this.neighbourCell[1][1] = this.alive;
	}
	
	private void checkIfNeighbourCellsAreComplete() {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (this.neighbourCell[x][y] == null) {
					return;
				}
			}
		}
		this.state = CellState.CALCULATENEXTSTAGE;
		this.calculateNextStage();
	}
	
	private int countAliveNeighbours() {
		int aliveNeighbours = 0;
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (x == 1 && y == 1) {
					continue;
				} else if (this.neighbourCell[x][y] == true) {
					aliveNeighbours++;
				}
			}
		}
		return aliveNeighbours;
	}
	
	private void calculateNextStage() {
		int aliveNeighbours = this.countAliveNeighbours();
		
		if (this.alive) {
			if (aliveNeighbours == 2 || aliveNeighbours == 3) {
				this.aliveInNextStage = true;
			} else {
				this.aliveInNextStage = false;
			}
		} else {
			if (aliveNeighbours == 3) {
				this.aliveInNextStage = true;
			} else {
				this.aliveInNextStage = false;
			}
		}
		
		this.state = CellState.CALCULATED;
	}
	
	public boolean isAlive() {
		return this.alive;
	}
	
	public Boolean isAliveInNextStage() {
		return this.aliveInNextStage;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getXBound() {
		return this.xBound;
	}
	
	public int getYBound() {
		return this.yBound;
	}
	
	public CellState getCellState() {
		return this.state;
	}
	
	public void proceedToNextStage() {
		this.alive = this.aliveInNextStage;
		this.aliveInNextStage = null;
		this.neighbourCell = new Boolean[3][3];
		this.neighbourCell[1][1] = this.alive;
	}
	
	public void unlockCell() {
		this.state = CellState.START;
	}
	
	public void setAliveInNextStage(boolean alive) {
		this.aliveInNextStage = alive;
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setBounds(int x, int y) {
		this.xBound = x;
		this.yBound = y;
	}
	
	public void setCellState(CellState state) {
		this.state = state;
	}
	
	public void setNeighbourState(boolean alive, int x, int y) {
		x = x - this.x + 1;
		y = y - this.y + 1;
		
		if (x < 0) {
			x += this.xBound;
		} else if (x == this.xBound) {
			x -= this.xBound;
		}
		
		if (y < 0) {
			y += this.yBound;
		} else if (y == this.yBound) {
			y -= this.yBound;
		}
		
		this.neighbourCell[x][y] = alive;
		
		this.checkIfNeighbourCellsAreComplete();
	}
}
