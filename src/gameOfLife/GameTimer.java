package gameOfLife;

import java.util.TimerTask;

public class GameTimer extends TimerTask {
	private gameControllerBehaviour game;
	
	GameTimer (gameControllerBehaviour game) {
		this.game = game;
	}

	@Override
	public void run() {
		//System.out.println("run");
		this.game.moveCellsToNextStage();
		this.game.unlockCells();
		this.game.getStatusOfCells();
		//System.out.println("run finished");
	}

}
