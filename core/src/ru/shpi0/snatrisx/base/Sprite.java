package ru.shpi0.snatrisx.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.shpi0.snatrisx.math.Rect;

public class Sprite extends Rect {

    private float angle;
    private float scale = 1f;
    private TextureRegion[] regions;
    private int frame;

    public Sprite(TextureRegion region) {
        if (region == null) {
            throw new NullPointerException("region is null");
        }
        regions  = new TextureRegion[1];
        regions[0] = region;
    }
    public void draw(SpriteBatch batch) {
        batch.draw(
                regions[frame], //текущий регион
                getLeft(), getBottom(), // точка отрисовки
                halfWidth, halfHeight, // точка вращения
                getWidth(), getHeight(), // ширина и высота
                scale, scale, // масштаб по x и y
                angle // угол вращения
        );
    }

    public void setHeightProportion(float height) {
        setHeight(height);
        float aspect = regions[frame].getRegionWidth() / (float) regions[frame].getRegionHeight();
        setWidth(height * aspect);
    }

    public void setWidthProportion(float width) {
        setWidth(width);
        float aspect = regions[frame].getRegionHeight() / (float) regions[frame].getRegionWidth();
        setHeight(width* aspect);
    }

    public void update(float delta) {

    }

    public void resize(Rect worldBounds) {
    }

    public boolean touchDown(Vector2 touch, int pointer) {
        return false;
    }

    public boolean touchUp(Vector2 touch, int pointer) {
        return false;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
