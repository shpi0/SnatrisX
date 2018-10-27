package ru.shpi0.snatrisx.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.shpi0.snatrisx.base.Sprite;
import ru.shpi0.snatrisx.math.Rect;

public class ButtonHowTo extends Sprite {
    public ButtonHowTo(TextureRegion region) {
        super(region);
        setHeightProportion(0.1f);
    }

    @Override
    public void resize(Rect worldBounds) {
        pos.set(pos.x, pos.y - 0.125f);
    }
}
