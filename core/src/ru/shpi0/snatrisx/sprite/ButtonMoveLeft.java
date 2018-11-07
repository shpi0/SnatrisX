package ru.shpi0.snatrisx.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.shpi0.snatrisx.base.Sprite;
import ru.shpi0.snatrisx.math.Rect;

public class ButtonMoveLeft extends Sprite {
    public ButtonMoveLeft(TextureRegion region) {
        super(region);
        setHeightProportion(0.1f);
    }

    @Override
    public void resize(Rect worldBounds) {
        pos.set(worldBounds.getLeft() + getHalfWidth(), 0f - worldBounds.getHalfHeight() / 2 - getHeight() * 1.75f / 2);
    }

}
