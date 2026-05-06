package com.game;

import com.game.engine.GameEngine;
import com.game.game.Game;

public class Main {

	public static void main(String[] args) {
		Game gameLogic = new Game();
		GameEngine engine = new GameEngine("3D Game", 800, 600, gameLogic);
		engine.start();
	}

}
