package com.vi.game.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * MyContactListener has overidden methods for the Box2d contact listener class. This allows to have full contorl
 * over maanaging contact events
 */

public class MyContactListener implements ContactListener{

    private boolean isContactWall;
    private boolean isShot;



    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        Gdx.app.log("contact", "begin contact");

        //a check on fa and fb are used because it's unknown which of the fixtures is the player
        if(fa.getUserData() != null && fa.getUserData().equals("player")) {
            isContactWall = true;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("player")) {
            isContactWall = true;
        }

        if(fa.getUserData() != null && fa.getUserData().equals("bullet")) {
            isShot = true;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("bullet")) {
            isShot = true;
        }


    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        Gdx.app.log("contact", "end contact");

        //a check on fa and fb are used because it's unknown which of the fixtures is the player
        if(fa.getUserData() != null && fa.getUserData().equals("player")) {
            isContactWall = false;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("player")) {
            isContactWall = false;
        }

        if(fa.getUserData() != null && fa.getUserData().equals("bullet")) {
            isShot = false;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("bullet")) {
            isShot = false;
        }

    }

    public boolean isContactWall() {
        return isContactWall;
    }



    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
