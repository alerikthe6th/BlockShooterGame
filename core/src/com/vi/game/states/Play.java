package com.vi.game.states;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.TimeUtils;
import com.vi.game.entities.Projectile;
import com.vi.game.handlers.B2DVariables;
import com.vi.game.entities.Player;
import com.vi.game.handlers.CharacterBody;
import com.vi.game.handlers.MapCollisionBuilder;
import com.vi.game.handlers.GameStateManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.vi.game.handlers.MyContactListener;
import com.vi.game.main.Game;

import static com.vi.game.handlers.B2DVariables.BIT_BULLET;
import static com.vi.game.handlers.B2DVariables.BULLET_SPEED;
import static com.vi.game.handlers.B2DVariables.SPRITE_SPEED;

/**
 * main game loop class that renders the box2d body and tile map layers
 */

public class Play extends GameState {

    private World world;
    private Box2DDebugRenderer b2dr;

    private CharacterBody playerBody;
    private MyContactListener cl;

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tmr;

    private MapCollisionBuilder mcb;

    private Player player;

    private float knobAngle;

    private Stage stage;

    int[] backgroundLayers = {0}; //assumes the first layer is always background
    int[] foregroundLayers = {1, 2, 3}; //assumes all other layers are foreground
                                        //in ascending order

    private long lastShootTime = 0;





    public Play(GameStateManager gsm) {
        super(gsm);

        //set the Box2d world
        world = new World(new Vector2(0,0),true);
        cl = new MyContactListener();
        world.setContactListener(cl);
        //b2d debugger shows body boxes
        b2dr = new Box2DDebugRenderer();

        //create playerBody body
        playerBody = new CharacterBody("player", world, 400, 500, "character");
        //creates the player with the body
        player = new Player(playerBody.getBody());

        // load the map
        tiledMap = new TmxMapLoader().load("testMap.tmx");
        tmr = new OrthogonalTiledMapRenderer(tiledMap);

        //create a collision map from the tile map
        mcb = new MapCollisionBuilder();
        mcb.buildCollisionLayer(world, tiledMap, "collision");

        stage = game.getStage();



}

    @Override
    public void handleInput() {

        //gets the arctan between the x and y vectors
        knobAngle = (float) Math.atan2(game.getXPercentage(), game.getYPercentage());

        //Gdx.app.log("knob angle", "knob angle is: " + knobAngle);
        //playerBody.getBody().setLinearVelocity(SPRITE_SPEED * game.getXPercentage(), SPRITE_SPEED * game.getYPercentage());
        player.getBody().setLinearVelocity(SPRITE_SPEED * game.getXPercentage(), SPRITE_SPEED * game.getYPercentage());

        player.getBody().setTransform(player.getBody().getPosition().x, player.getBody().getPosition().y, -knobAngle);


        if(game.isShooting() && TimeUtils.timeSinceMillis(lastShootTime) > 1000) {

            lastShootTime = TimeUtils.millis();

            Gdx.app.log("shooting", "player is shooting");
            //creates a bullet body where the character shoots
            CharacterBody bulletBody = new CharacterBody("bullet", world, player.getBody().getPosition().x,
                    player.getBody().getPosition().y, "bullet");
            //creates bullet sprite
            Projectile bullet = new Projectile(bulletBody.getBody());
            //launches a bullet body
            float velocityX = (float) Math.cos(player.getAngle()) * BULLET_SPEED;
            float velocityY = (float) Math.sin(player.getAngle()) * BULLET_SPEED;


            bullet.getBody().setLinearVelocity(velocityX, velocityY);
            //gives direction to the bullet in the same direction as the player
            bullet.getBody().setTransform(player.getBody().getPosition().x, player.getBody().getPosition().y, -player.getAngle());

            Gdx.app.log("player angle", "player angle:" + player.getAngle());

        }

        //player.setRotation(knobAngle);
        //origin.X = 16;
        //origin.Y = 16;
        //screenpos.X = player.getBody().getPosition().x
        //screenpos.Y = player.getBody().getPosition().y
        //Gdx.app.log("angle", "player angle: " + player.getBody().getAngle());

        //Gdx.app.log("moving", "moving in: [" + game.getXPercentage() + ", " + game.getYPercentage());

    }

    @Override
    public void update(float dt) {

        //updates the handler and the player and "steps" the world
        handleInput();
        world.step(dt, 6, 2);
        player.update(dt);

    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cam.update();

        //draw til map
        tmr.setView(cam);

        //draw background tile map
        tmr.render(backgroundLayers);
        //draw player
        player.render(sb, knobAngle);
        //draw foreground tile map
        tmr.render(foregroundLayers);


         //track the camera to the player
        cam.position.set(player.getBody().getPosition().x, player.getBody().getPosition().y, 0);
        sb.setProjectionMatrix(cam.combined);

        //render b2d debugger world
        b2dr.render(world, cam.combined);

        //draw the stage
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();



       // Gdx.app.log("moving", "moving in: [" + game.getXPercentage() + ", " + game.getYPercentage() + "]");
    }

    @Override
    public void dispose() {
        world.dispose();
    }
}
