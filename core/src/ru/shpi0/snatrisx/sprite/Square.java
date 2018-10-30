package ru.shpi0.snatrisx.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.shpi0.snatrisx.base.Sprite;

public class Square extends Sprite {

    public Square(TextureRegion region) {
        super(region);
        setHeightProportion(0.045f);
    }

}
