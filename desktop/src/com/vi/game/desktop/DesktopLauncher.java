package com.vi.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.vi.game.MyGenericGame;
import com.vi.game.main.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//320 width
		//240 height
		//2 scale
		config.title = "GENERIC GAME";
		config.width = 320 * 2;
		config.height = 240 * 2;

		config.vSyncEnabled = true; //helps with some screen tearing

		new LwjglApplication(new Game(), config);
	}
}
