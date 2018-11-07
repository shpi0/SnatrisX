package ru.shpi0.snatrisx.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.shpi0.snatrisx.base.Sprite;
import ru.shpi0.snatrisx.math.Rect;

public class ButtonMoveDown extends Sprite {
    public ButtonMoveDown(TextureRegion region) {
        super(region);
        setHeightProportion(0.1f);
    }

    @Override
    public void resize(Rect worldBounds) {
        pos.set(worldBounds.getLeft() + getWidth(), 0f - worldBounds.getHalfHeight() / 2 - getHeight() * 1.75f);
    }

}
