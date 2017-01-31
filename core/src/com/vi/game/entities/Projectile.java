package com.vi.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.vi.game.main.Game;

/**
 * Created by Alerik Vi on 1/31/2017.
 */

public class Projectile extends MySprite {

    public Projectile(Body body) {

        super(body);

        Texture tx = Game.res.getTexture("bullet");
        TextureRegion[] sprites = TextureRegion.split(tx, 32, 32)[0];
        animation.setFrames(sprites, 1/12f);

        setAnimation(sprites, 0); //player does not move while idle if second param is 0
    }
}
