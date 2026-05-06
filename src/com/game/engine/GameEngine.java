package com.game.engine;

import com.game.engine.window.GameWindow;
import com.game.game.IGameLogic;

public class GameEngine implements Runnable {

	private IGameLogic gameLogic;
	private GameWindow window;

	public GameEngine(String title, int width, int height, IGameLogic gameLogic) {
		this.window = new GameWindow(title, width, height);
		this.gameLogic = gameLogic;
	}

	private void cleanup() {
		gameLogic.cleanup();
	}

	private void init() {
		window.init();
		gameLogic.init(window);
	}

	private void loop() {
		while (!window.shouldClose()) {
			gameLogic.input(window);
			gameLogic.update();
			gameLogic.render(window);

			window.update();
		}
	}

	@Override
	public void run() {
		init();
		loop();
		cleanup();
	}

	public void start() {
		run();
	}

}
