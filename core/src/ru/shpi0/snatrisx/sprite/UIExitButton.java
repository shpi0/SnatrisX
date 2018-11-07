package ru.shpi0.snatrisx.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.shpi0.snatrisx.base.Sprite;
import ru.shpi0.snatrisx.math.Rect;

public class UIExitButton extends Sprite {
    public UIExitButton(TextureRegion region) {
        super(region);
        setWidthProportion(0.05f);
    }

    @Override
    public void resize(Rect worldBounds) {
        pos.set(worldBounds.getLeft() + getHalfWidth(), worldBounds.getTop() - getHeight());
    }
}
