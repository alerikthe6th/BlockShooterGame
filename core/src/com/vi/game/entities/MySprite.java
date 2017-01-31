package com.vi.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.vi.game.handlers.Animation;

import static java.lang.StrictMath.toDegrees;

/**
 * Created by Alerik Vi on 1/31/2017.
 */

/**
 * this class creates a sprite from the anmation class that
 * references a box2d body
 */
public class MySprite extends Sprite{

    protected Body body;
    protected Animation animation;
    protected float width;
    protected float height;
    private float prevAngle = 0;
    private float angle = 0;

    public MySprite(Body body) {
        this.body = body;
        animation = new Animation();
    }

    public void setAnimation(TextureRegion[] reg, float delay) {
        animation.setFrames(reg, delay);
        width = reg[0].getRegionWidth();
        height = reg[0].getRegionHeight();
    }

    public void update(float dt) {
        animation.update(dt);
    }

    public void render(SpriteBatch sb, float rad) {



        float originX = width / 2;
        float originY = height /2;
        float angle = (float) Math.toDegrees(rad); //converts rad to an angle from the touch pad

        this.angle = angle;

        //this keeps the orientation of the sprite because
        //the touchpad knobs reset to an angle of zero once contact
        //has been lost
        if(angle != 0) {
            prevAngle = angle;
        }else{
            angle = prevAngle;
        }

        //Gdx.app.log("angle", "sprite angle: " + angle);
        //Gdx.app.log("prev angle", "sprite prev angle: " + prevAngle);

        sb.begin();

        //Gdx.app.log("dimen", "sprite width, height: " + width  + ", " + height);

        // angle must be reversed because LibGDX and the Camera have a different coordinate system
        // the 1,-1 in the the paramters below denote a a positive X-scale and a negative Y-scale
        sb.draw(animation.getFrame(), body.getPosition().x - width / 2, body.getPosition().y - height / 2,
                    originX, originY, width, height, 1, -1, -angle, true);

        sb.end();

    }

    public Body getBody() {
        return body;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getAngle() {
        return prevAngle;
    }


}
