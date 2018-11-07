package ru.shpi0.snatrisx.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.shpi0.snatrisx.base.Sprite;
import ru.shpi0.snatrisx.math.Rect;

public class ButtonRotate extends Sprite {
    public ButtonRotate(TextureRegion region) {
        super(region);
        setHeightProportion(0.15f);
    }

    @Override
    public void resize(Rect worldBounds) {
        pos.set(0f + worldBounds.getHalfWidth() / 2 + getHalfHeight() / 2, 0f - worldBounds.getHalfHeight() / 2 - getHalfHeight() * 1.5f);
    }

}
