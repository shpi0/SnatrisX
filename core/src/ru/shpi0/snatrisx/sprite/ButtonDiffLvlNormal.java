package ru.shpi0.snatrisx.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.shpi0.snatrisx.base.Sprite;
import ru.shpi0.snatrisx.math.Rect;

public class ButtonDiffLvlNormal extends Sprite {

    public ButtonDiffLvlNormal(TextureRegion region) {
        super(region);
        setHeightProportion(0.1f);
    }

    @Override
    public void resize(Rect worldBounds) {
        pos.set(worldBounds.pos.x, worldBounds.pos.y - getHeight() * 2 - 0.02f);
    }

}
