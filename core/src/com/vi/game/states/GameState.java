package com.vi.game.states;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.vi.game.handlers.GameStateManager;

/**
 * Abstract game state class handlded by a game state manager using a stack data structure.
 */

public abstract class GameState {

        protected com.vi.game.handlers.GameStateManager gsm;
        public com.vi.game.main.Game game;

        protected SpriteBatch sb;
        protected OrthographicCamera cam;

        protected GameState(GameStateManager gsm) {
            this.gsm = gsm;
            game = gsm.game();
            sb = game.getSpriteBatch();
            cam = game.getCamera();
        }


    public abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render();
    public abstract void dispose();


}
