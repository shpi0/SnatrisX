package ru.shpi0.snatrisx.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.shpi0.snatrisx.base.Sprite;

public class Target extends Sprite {

    public Target(TextureRegion region) {
        super(region);
        setHeightProportion(0.048f);
    }

}
