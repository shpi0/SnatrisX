package ru.shpi0.snatrisx.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.shpi0.snatrisx.base.Sprite;
import ru.shpi0.snatrisx.math.Rect;

public class CoinAcceptor extends Sprite {
    public CoinAcceptor(TextureRegion region) {
        super(region);
        setHeightProportion(0.25f);
    }

    @Override
    public void resize(Rect worldBounds) {
        pos.set(worldBounds.pos.x, worldBounds.pos.y - worldBounds.getHalfHeight() * 0.78f);
    }
}
