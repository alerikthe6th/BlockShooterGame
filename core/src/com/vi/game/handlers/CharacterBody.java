package com.vi.game.handlers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Creates a box2d body for a generic character for the game.
 */

public class CharacterBody {

    private BodyDef bDef;
    private FixtureDef fDef;
    private Body charBody;

    private String name;
    private float posX;
    private float posY;

    /**
     * constructor that builds a character body and adds it to the world
     * at a given X and Y position.
     *
     * @param name
     * @param world
     * @param posX
     * @param posY
     */
    public CharacterBody(String name, World world, float posX, float posY, String type) {

        this.name = name;
        this.posX = posX;
        this.posY = posY;

        bDef = new BodyDef();
        fDef = new FixtureDef();

        PolygonShape shape = new PolygonShape();

        //create player
        bDef.position.set(posX, posY);
        bDef.type = BodyDef.BodyType.DynamicBody;
        charBody = world.createBody(bDef);

        shape.setAsBox(16,16); //32x32 box for the player
        fDef.shape = shape;
        //fDef.isSensor = true;
        if(type.equals("character")) {
            fDef.filter.categoryBits = B2DVariables.BIT_PLAYER;
            fDef.filter.maskBits = B2DVariables.BIT_WALL | B2DVariables.BIT_BULLET;
        }else if(type.equals("bullet")){
            fDef.filter.categoryBits = B2DVariables.BIT_BULLET;
            fDef.filter.maskBits = B2DVariables.BIT_WALL; //adds the player and wall bit patterns: 0000 0000 0000 00110 (ones place is an unused default)
        }
        //fDef.restitution = 1f; bounciness
        charBody.createFixture(fDef).setUserData(name);

    }

    public String getName() {
        return name;
    }
    public float getPosX() {
        return posX;
    }
    public float getPosY() {
        return posY;
    }
    public Body getBody() {
        return charBody;
    }

}
