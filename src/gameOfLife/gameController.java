package gameOfLife;

import gameOfLife.Cell.CellState;
import jade.core.Agent;

public class gameController extends Agent {
	private int xBound = 10;
	private int yBound = 10;
	private Boolean[][] gameBoard = new Boolean[this.xBound][this.yBound];
	private Boolean[][] cellsFinished = new Boolean[this.xBound][this.yBound];
	private int cycle = 2000;
	public enum GameState {
		STARTED,
		GETCELLSTATES,
		PRINTGAMEBOARD,
		FINISHED
	};
	private GameState game = GameState.STARTED;
	public enum CellStates {
		GETSTATUS,
		FINISHED
	};
	private CellStates cells = CellStates.FINISHED;
	
	@Override
	protected void setup() {
		addBehaviour(new gameControllerBehaviour(this));
	}
	
	private void checkIfGameBoardIsComplete() {
		for (int x = 0; x < this.xBound; x++) {
			for (int y = 0; y < this.yBound; y++) {
				if (this.gameBoard[x][y] == null) {
					return;
				}
			}
		}
		this.game = GameState.PRINTGAMEBOARD;
		this.printGameBoard();
	}
	
	private void printGameBoard() {
		System.out.print("#");
		for (int x = 0; x < this.xBound; x++) {
			System.out.print("#");
		}
		System.out.println("#");
		for (int y = 0; y < this.yBound; y++) {
			System.out.print("#");
			for (int x = 0; x < this.xBound; x++) {
				if (this.gameBoard[x][y] == true) {
					System.out.print("X");
				} else {
					System.out.print(" ");
				}
			}
			System.out.println("#");
		}
		System.out.print("#");
		for (int x = 0; x < this.xBound; x++) {
			System.out.print("#");
		}
		System.out.println("#");
		this.gameBoard = new Boolean[this.xBound][this.yBound];
		this.game = GameState.FINISHED;
	}
	
	private void checkIfCellsAreFinished() {
		for (int x = 0; x < this.xBound; x++) {
			for (int y = 0; y < this.yBound; y++) {
				if (this.cellsFinished[x][y] == null || this.cellsFinished[x][y] == false) {
					return;
				}
			}
		}
		this.cellsFinished = new Boolean[this.xBound][this.yBound];
		this.cells = CellStates.FINISHED;
		//System.out.println("All cells are ready.");
	}
	
	public int getXBound() {
		return this.xBound;
	}
	
	public int getYBound() {
		return this.yBound;
	}
	
	public int getCycle() {
		return this.cycle;
	}
	
	public void setCycle(int cycle) {
		this.cycle = cycle;
	}
	
	public GameState getGameState() {
		return this.game;
	}
	
	public void setGameState(GameState state) {
		this.game = state;
	}
	
	public CellStates getCellStates() {
		return this.cells;
	}
	
	public void setCellStates(CellStates state) {
		this.cells = state;
	}
	
	public boolean getGameBoard(int x, int y) {
		return this.getGameBoard(x, y);
	}
	
	public void setGameBoard(boolean alive, int x, int y) {
		this.gameBoard[x][y] = alive;
		this.checkIfGameBoardIsComplete();
	}
	
	public void setCellFinished(boolean finished, int x, int y) {
		this.cellsFinished[x][y] = finished;
		this.checkIfCellsAreFinished();
	}
}
