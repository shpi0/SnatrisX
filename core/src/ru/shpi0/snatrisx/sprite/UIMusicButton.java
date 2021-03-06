package ru.shpi0.snatrisx.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.shpi0.snatrisx.base.Sprite;
import ru.shpi0.snatrisx.math.Rect;

public class UIMusicButton extends Sprite {
    public UIMusicButton(TextureRegion region) {
        super(region);
        setHeightProportion(0.05f);
    }

    @Override
    public void resize(Rect worldBounds) {
        pos.set(worldBounds.getRight() - getHalfWidth(), worldBounds.getTop() - getHalfHeight() - getHeight() * 3);
    }
}
