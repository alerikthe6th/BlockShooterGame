package com.vi.game.handlers;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.vi.game.handlers.B2DVariables;

/**
 * This class sends in map objects from the tiled maps and applies box2d physics to it
 * Created by Alerik Vi on 1/27/2017.
 */

public class MapCollisionBuilder {

    BodyDef bDef = new BodyDef();
    FixtureDef fDef = new FixtureDef();

    /**
     * method that creates a body and fixture for each object in a map layer.
     * A world, TileMap object and a string name of the collision layer of the map
     * must be passed in
     *
     * @param world
     * @param map
     * @param colName
     */
    public void buildCollisionLayer(World world, TiledMap map, String colName) {

        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(colName);
        float tileSize = layer.getTileWidth();


        //probably very inefficient for a double for loop to iterate an entire map layer
        for(int row = 0; row < layer.getHeight(); row++) { //y direction
            for(int col = 0; col < layer.getWidth(); col++) { //x direction

                //get the cell
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);

                //check if valid cell
                if(cell == null) continue;
                if(cell.getTile() == null) continue;

                //create the body and fixture from the cell data
                bDef.type = BodyDef.BodyType.StaticBody;
                //position is set at the center, so to obtain the edge, you offset by a half
                bDef.position.set((col + 0.5f) * tileSize, (row + 0.5f) * tileSize);


                //define the corners of the tile
                ChainShape cs = new ChainShape();
                Vector2[] v = new Vector2[4];
                v[0] = new Vector2(-tileSize / 2, -tileSize /2 );
                v[1] = new Vector2(-tileSize / 2, tileSize /2 );
                v[2] = new Vector2(tileSize / 2, -tileSize /2 );
                v[3] = new Vector2(tileSize / 2, tileSize /2 );

                cs.createChain(v);
                fDef.friction = 0; //no slow down after collision
                fDef.shape = cs;

                fDef.filter.categoryBits = B2DVariables.BIT_WALL;
                fDef.filter.maskBits = B2DVariables.BIT_PLAYER | B2DVariables.BIT_BULLET;

                fDef.isSensor = false; //don't want a sensor, otherwise it'll ghost through other
                                       //objects

                //add the layer object to the world
                world.createBody(bDef).createFixture(fDef);
            }
        }

    }



}
