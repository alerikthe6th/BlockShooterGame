package com.vi.game.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

/**
 * Content uses a hash map that loads and tracks images based on an input "key" or tag called from
 * the game class.
 */

public class Content {

    private HashMap<String, Texture> textures;

    public Content() {

        textures = new HashMap<String, Texture>();
    }
    //loads a texture into the hash map
    public void loadTexture(String path, String key) {
        Texture tx = new Texture(Gdx.files.internal(path));
        textures.put(key, tx);
    }
    //getter for texture
    public Texture getTexture(String key) {
        return textures.get(key);
    }

    //disposes textures
    public void disposeTexture(String key) {
        Texture tx = textures.get(key);
        if(tx != null) {
            tx.dispose();
        }
    }

}
