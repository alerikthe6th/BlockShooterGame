package com.vi.game.handlers;

import com.badlogic.gdx.graphics.g2d.TextureRegion;


/**
 * This class is used to animate sprites using a texture region called from the
 * sprite class
 */
public class Animation {

    private TextureRegion[] frames;
    private float time;
    private float delay;
    private int currFrame;
    private int timesPlayed;

    public Animation() {}

    public Animation(TextureRegion[] frames) {
        this(frames, 1/12f); // 1/12f is a default delay
    }

    public Animation(TextureRegion[] frames, float delay) {
        setFrames(frames, delay);
    }

    public void setFrames(TextureRegion[] frames, float delay) {
        this.frames = frames;
        this.delay = delay;
        time = 0;
        currFrame = 0;
        timesPlayed = 0;
    }

    public  void update(float dt) {

        //if the delay is zero, nothing is animating
        if (delay <= 0) {
            return;
        }
        time += dt;
        while (time >= delay) {
            step();
        }
    }

    private void step() {
        time -= delay;
        currFrame++;
        if(currFrame == frames.length) {
            currFrame = 0;
            timesPlayed++;
        }
    }
    public TextureRegion getFrame() {
        return frames[currFrame];
    }

    public int getTimesPlayed() {
        return timesPlayed;
    }

}
