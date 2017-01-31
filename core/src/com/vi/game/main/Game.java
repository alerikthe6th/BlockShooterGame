package com.vi.game.main;

import com.vi.game.handlers.Content;
import com.vi.game.handlers.GameStateManager;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import static com.vi.game.handlers.B2DVariables.STEP;

/**
 * Main class that creates the HUD, loads the character textures, has the main render loop controlling
 * the game state manager, and sets the cameras
 *
 * This is a LibGDX project with heavy reliance on the Box2d game engine used to create a top down
 * shooter by placing a tile map and imposing a "world" from the box2d engine that creates "body"
 * objects that track sprites as they're animated and interact with the world.
 *
 * A lot of handler and entities classes were made to cover a lot of the animating and
 * keep track of sprites to their relative box2d bodies.
 *
 * Author: Alerik Vi
 *
 * 3rd parties used: Tiled, Texture Packer
 * Sources: sprites came from http://opengameart.org/content/animated-top-down-survivor-player
 *          tile set for tile map from http://www.lorestrome.com/pixel_archive/main.htm
 *          basic box2d tutorials from ForeignGuyMike https://www.youtube.com/channel/UC_IV37n-uBpRp64hQIwywWQ
 *          tile set tutorial from http://www.gamefromscratch.com/post/2014/04/16/LibGDX-Tutorial-11-Tiled-Maps-Part-1-Simple-Orthogonal-Maps.aspx
 *          touch pad example and tutorial from http://www.bigerstaff.com/libgdx-touchpad-example/
 */

public class Game implements ApplicationListener{

    public static final String TITLE = "Generic Game";

    private SpriteBatch sb;
    private OrthographicCamera cam;

    //a stage holds a table (an actor) of HUD elements
    private Stage stage;

    //used for setting up the touch pad joystick
    public Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;


    public Touchpad shoot;
    private Touchpad.TouchpadStyle btnStyle;
    private Skin btnSkin;
    private Drawable shootBckgrnd;

    //tracks accumulated delta time
    private float accum;

    private GameStateManager gsm;

    public static Content res;

    @Override
    public void create() {

        res = new Content();
        res.loadTexture("playerMove2.png", "player"); //TODO: make different animation states for the player
        res.loadTexture("bullet2.png", "bullet");

        sb = new SpriteBatch();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, 800, 400);

        stage = new Stage();

        gsm = new GameStateManager(this);

        setHUD();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render() {

        accum += Gdx.graphics.getDeltaTime();
        while(accum >= STEP) {
            accum -= STEP;
            gsm.update(STEP);
            gsm.render();
        }
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

        btnSkin = new Skin();
        btnSkin.add("btnShoot", new Texture("data/block.png"));
        btnStyle = new Touchpad.TouchpadStyle();
        shootBckgrnd = btnSkin.getDrawable("btnShoot");
        btnStyle.background = shootBckgrnd;
        shoot = new Touchpad(10, btnStyle);


        //a table is used to format the layout of the HUD  and it's just simple to add more
        //objects to it
        Table table = new Table();
        table.setFillParent(true);
        table.bottom().left();
        table.add(touchpad).row(); //only joystick is currently added

        Table table2 = new Table();
        table2.setFillParent(true);
        table2.bottom().right();
        //table2.row().height(100);
        table2.row().minHeight(100);
        table2.add(shoot);

        stage.addActor(table);
        stage.addActor(table2);

        Gdx.input.setInputProcessor(stage);
    }

    //returns an X scalar based from the touchpad
    public float getXPercentage() {
        return touchpad.getKnobPercentX();
    }

    //returns an X scalar based from the touchpad
    public float getYPercentage() {
        return touchpad.getKnobPercentY();
    }

    public boolean isShooting() {
        return shoot.isTouched();
    }

    public Stage getStage() {
        return stage;
    }

    public SpriteBatch getSpriteBatch() {
        return sb;
    }
    public OrthographicCamera getCamera() {
        return cam;
    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        sb.dispose();
        res.disposeTexture("player");

    }


}
