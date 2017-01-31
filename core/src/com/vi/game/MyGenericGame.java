/*
DEAD CLASS, DON'T USE

 */


package com.vi.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class MyGenericGame extends ApplicationAdapter {

    public static final String TITLE = "Generic Game";

	private OrthographicCamera camera;

	private TiledMap tiledMap;
	private TiledMapRenderer tiledMapRenderer;
	private Stage stage;

	//data fields for setting up a joystick
	private Touchpad touchpad;
	private Touchpad.TouchpadStyle touchpadStyle;
	private Skin touchpadSkin;
	private Drawable touchBackground;
	private Drawable touchKnob;

	private SpriteBatch spriteBatch;

	private Texture player;
	private com.badlogic.gdx.graphics.g2d.Sprite playerSprite;

	final float SPRITE_SPEED = 200;
    final float PPT = 32;
    final float PIXELS_TO_METERS = 100f;

    public static final float STEP = 1/60f;

    private World world;
    private Body playerBody;

    private Body bodyEdgeMap;


    private Array<Body> bodies;

    private com.vi.game.handlers.MapCollisionBuilder mcb;
	
	@Override
	public void create () {

        //TODO: make classes for some of these gfx features

		//Create the camera
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 400);
		camera.update();

		//vp = new StretchViewport(w,h,camera);
		stage = new Stage();


		tiledMap = new TmxMapLoader().load("testMap.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);



		spriteBatch = new SpriteBatch();

		//Create block sprite
		player = new Texture(Gdx.files.internal("data/block.png"));
		playerSprite = new Sprite(player);
		//Set position to centre of the screen
		playerSprite.setPosition(w/2- playerSprite.getWidth()/2, h/2- playerSprite.getHeight()/2);

        //sets a box2d world
        world = new World(new Vector2(0, 0), true);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        //set the player body to the sprite of the player
        bodyDef.position.set(playerSprite.getX(), playerSprite.getY());

        // creates a body in the world
        playerBody = world.createBody(bodyDef);

        // Now the physics body of the bottom edge of the map
        // doesn't work
        BodyDef bodyDefScreen = new BodyDef();
        bodyDefScreen.type = BodyDef.BodyType.StaticBody;
        float sWidth = Gdx.graphics.getWidth()/PIXELS_TO_METERS;
        float sHeight = Gdx.graphics.getHeight()/PIXELS_TO_METERS;


        bodyDefScreen.position.set(0,0);
        FixtureDef fixtureDefScreen = new FixtureDef();

        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(-sWidth/2,-sHeight/2,sWidth/2,-sHeight/2);
        fixtureDefScreen.shape = edgeShape;

        bodyEdgeMap = world.createBody(bodyDef);
        bodyEdgeMap.createFixture(fixtureDefScreen);
        edgeShape.dispose();

        mcb = new com.vi.game.handlers.MapCollisionBuilder();
       // bodies = mcb.buildShapes(tiledMap, PPT, world);

        Gdx.app.log("mcb list", "" + bodies.toString());
        // define dimensions of the shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(playerSprite.getWidth()/2, playerSprite.getHeight());

            // welp. can't get this to work

//        world.setContactListener(new ContactListener() {
//
//            @Override
//            public void beginContact(Contact contact) {
//
//                Gdx.app.log("in method", "going through contact list");
//
//                for(Body body : bodies) {
//
//                    if ((contact.getFixtureA().getBody() == body) ||
//                            (contact.getFixtureB().getBody() == body)) {
//
//                        Gdx.app.log("in method", "contact detected!");
//
//                        playerBody.setLinearVelocity(0,0);
//                    }
//                }
//            }
//
//            @Override
//            public void endContact(Contact contact) {
//
//            }
//
//            @Override
//            public void preSolve(Contact contact, Manifold oldManifold) {
//
//            }
//
//            @Override
//            public void postSolve(Contact contact, ContactImpulse impulse) {
//
//            }
//        });


//        shape.dispose();
		//initialize the layers

		//initialize the HUD aka the stage with all the actor items used for the UI
		setHUD();
	}

	public void setHUD() {

		//Create a touchpad skin
		touchpadSkin = new Skin();
		//Set the background image
		touchpadSkin.add("touchBackground", new Texture("data/touchBackground.png"));
		//Set knob image
		touchpadSkin.add("touchKnob", new Texture("data/touchKnob.png")); //needs to point in the asset folder (and in the data folder)

		//Create a new TouchPad Style
		touchpadStyle = new Touchpad.TouchpadStyle();
		//Create drawables from TouchPad skin
		touchBackground = touchpadSkin.getDrawable("touchBackground");
		touchKnob = touchpadSkin.getDrawable("touchKnob");
		//apply the drawable images to the skin
		touchpadStyle.background = touchBackground;
		touchpadStyle.knob = touchKnob;

		//Create new TouchPad with the created style
		touchpad = new Touchpad(10, touchpadStyle);

        //a table is used to format the layout of the HUD  and it's just simple to add more
        //objects to it
		Table table = new Table();
		table.setFillParent(true);
		table.bottom().left();

		//other stats? HP, xp, action controls
		table.add(touchpad).row(); //only joystick is currently added


		stage.addActor(table);

		Gdx.input.setInputProcessor(stage);


	}

	@Override
	public void render () {

		world.step(STEP, 6, 2);

        Gdx.app.log("object position", "[" + bodies.get(0).getPosition().x + ", " +  bodies.get(0).getPosition().y + "]");
        Gdx.app.log("player position", "[" +playerBody.getPosition().x + ", " +  playerBody.getPosition().y + "]");


        //im just testing and the printing the positions of the player's body and an object layer from the tile map
        float pbX = playerBody.getPosition().x;
        float pbY = playerBody.getPosition().y;

        float oX = bodies.get(0).getPosition().x;
        float oY = bodies.get(0).getPosition().y;

//        if(pbX + 32f >= oX && pbX <= oX + 32f) {
//            Gdx.app.log("x collide", "within the x region");
//        }
//
//        if(pbY + 32f >= oY && pbY <= oY + 32f) {
//            Gdx.app.log("y collide", "within the y region");
//        }


//        probably very inefficient to loop through all the collidable objects in the render method
//        for(Body body : bodies) {
//            if(body.getPosition().x == playerBody.getPosition().x
//                    || body.getPosition().y == playerBody.getPosition().y) {
//
//                Gdx.app.log("collision!", "you hit something");
//
//            }
//        }



		//Set the background color to sky blue, renders the tile map and updates the camera
		Gdx.gl.glClearColor(135 / 255f, 206 / 255f, 235 / 255f, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();



		spriteBatch.setProjectionMatrix(camera.combined);

		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		//Move playerSprite with TouchPad
		//playerSprite.setX(playerSprite.getX() + touchpad.getKnobPercentX()* spriteSpeed);
		//playerSprite.setY(playerSprite.getY() + touchpad.getKnobPercentY()* spriteSpeed);



        //moves the sprite position to the moving body
        playerSprite.setPosition(playerBody.getPosition().x, playerBody.getPosition().y);

        //hardcore collision detect...
        if(pbX + 32f >= oX && pbX <= oX + 32f
                && pbY + 32f >= oY && pbY <= oY + 32f) {
            Gdx.app.log("collide", "within the region");


             //doesnt allow to backtrack
//            if(pbX + 32f >= oX && pbX <= oX + 32f) {
//
//                playerBody.setLinearVelocity(0, SPRITE_SPEED * touchpad.getKnobPercentY());
//            }
//
//            if(pbY + 32f >= oY && pbY <= oY + 32f) {
//
//                playerBody.setLinearVelocity(SPRITE_SPEED * touchpad.getKnobPercentX(), 0);
//            }

        } else {

            //moves the body with the touchpad with a set speed.
            playerBody.setLinearVelocity(SPRITE_SPEED * touchpad.getKnobPercentX(), SPRITE_SPEED * touchpad.getKnobPercentY());

        }


		//moves camera with the moving sprite
		camera.position.set(playerSprite.getX(), playerSprite.getY(), 0);

        //deal with collision


		//Draw the sprite
		spriteBatch.begin();
		playerSprite.draw(spriteBatch);
		spriteBatch.end();

		//draw the stage
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
	
	@Override
	public void dispose () {

		spriteBatch.dispose();
		player.dispose();
		world.dispose();
		player.dispose();
	}
}
