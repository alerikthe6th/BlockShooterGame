package com.vi.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.vi.game.main.Game;

/**
 * A specific sprite object for the player
 */

public class Player extends MySprite {

    public Player(Body body) {

        super(body);

        Texture tx = Game.res.getTexture("player");
        TextureRegion[] sprites = TextureRegion.split(tx, 32, 32)[0];
        animation.setFrames(sprites, 1/12f);

        setAnimation(sprites, 1/12f); //player does not move while idle if second param is 0
    }

}
