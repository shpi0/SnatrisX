package ru.shpi0.snatrisx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.shpi0.snatrisx.base.BaseScreen;

public class MenuScreen extends BaseScreen {

    Texture img;
    private static final float IMG_WIDTH = 0.75f; // Ширина текстуры в процентах от ширины экрана
    private float imgWidth; // Ширина и высота текстуры в новой системе координат
    private float imgHeight; // используется для центровки изображения

    Texture square1;

    private Vector2 initVector = new Vector2(); // Вектор текущей (начальной) позиции объекта
    private Vector2 moveVector = new Vector2(); // Вектор направления движения
    private Vector2 destVector = new Vector2(); // Вектор назначения
    private Vector2 tempVector = new Vector2(); // Временный вектор
    private float alpha = 0f; // Коэффициент прозрачности (альфа канал)

    // Логические ключи
    private boolean growUp = true; // Ключ для мерцания, если true, то яркость увеличивается, иначе уменьшается
    private boolean leftMove = false; // Ключ движения объекта влево
    private boolean rightMove = false; // Ключ движения объекта вправо
    private boolean upMove = false; // Ключ движения объекта вверх
    private boolean downMove = false; // Ключ движения объекта вниз
    private boolean touched = false;// Ключ движения объекта до точки назначения при нажатии на экран

    @Override
    public void show() {
        super.show();
        img = new Texture("snatris_logo.png");
        square1 = new Texture("square_blue.png");
        initVector.x = 0;
        initVector.y = 0;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        imgWidth = worldBounds.getWidth() * IMG_WIDTH;
        imgHeight = (img.getHeight() / (float) img.getWidth()) * worldBounds.getWidth() * IMG_WIDTH;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.setColor(1, 1, 1, alpha);
        batch.draw(img, initVector.x - imgWidth / 2, initVector.y - imgHeight / 2, imgWidth, imgHeight);
        batch.end();
        calculateAlpha();
        if (touched) {
            doMoveObject();
        }
        if (leftMove) {
            destVector.x = initVector.x - 1f;
            destVector.y = initVector.y;
            norMoveVector();
            doMoveObject();
        }
        if (rightMove) {
            destVector.x = initVector.x + 1f;
            destVector.y = initVector.y;
            norMoveVector();
            doMoveObject();
        }
        if (upMove) {
            destVector.x = initVector.x;
            destVector.y = initVector.y + 1f;
            norMoveVector();
            doMoveObject();
        }
        if (downMove) {
            destVector.x = initVector.x;
            destVector.y = initVector.y - 1f;
            norMoveVector();
            doMoveObject();
        }
    }

    private void doMoveObject() {
        tempVector.set(destVector);
        if (tempVector.sub(initVector).len() > moveVector.len()) {
            initVector.add(moveVector);
        } else {
            initVector.set(destVector);
            touched = false;
        }
    }

    private void calculateAlpha() {
        if (growUp) {
            alpha += 0.006f;
        } else {
            alpha -= 0.006f;
        }
        if (alpha >= 1f) {
            growUp = false;
        }
        if (alpha <= 0.007f) {
            growUp = true;
        }
    }

    @Override
    public void dispose() {
        img.dispose();
        square1.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        this.destVector = touch;
        norMoveVector();
        touched = true;
        return false;
    }

    private void norMoveVector() {
        moveVector.set(destVector.cpy().sub(initVector).nor().scl(worldBounds.getWidth() / 200f));
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
                upMove = false;
                break;
            case Input.Keys.DOWN:
                downMove = false;
                break;
            case Input.Keys.LEFT:
                leftMove = false;
                break;
            case Input.Keys.RIGHT:
                rightMove = false;
                break;
        }
        return super.keyUp(keycode);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
                upMove = true;
                break;
            case Input.Keys.DOWN:
                downMove = true;
                break;
            case Input.Keys.LEFT:
                leftMove = true;
                break;
            case Input.Keys.RIGHT:
                rightMove = true;
                break;
        }
        return super.keyDown(keycode);
    }
}
