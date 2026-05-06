package com.game.game;

import com.game.engine.window.GameWindow;

public interface IGameLogic {
	void cleanup();

	void init(GameWindow window);

	void input(GameWindow window);

	void render(GameWindow window);

	void update();
}