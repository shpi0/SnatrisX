package ru.shpi0.snatrisx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.shpi0.snatrisx.base.BaseScreen;

public class MenuScreen extends BaseScreen {

    // Бегущий человечек :)
    private static final int FRAME_COLS = 6; // Размерность атласа
    private static final int FRAME_ROWS = 5; // Размерность атласа
    private static final float HUMAN_SIZE = 0.1f; // Размер человечка от размера экрана
    private static final float HUMAN_WIDTH = (512f / 6f) / (512f / 5f); // Пропорция ширины человечка
    private Animation walkAnimation;
    private Texture walkSheet;
    private TextureRegion[] walkFrames;
    private TextureRegion currentFrame;
    private float stateTime;
    private float humanWidth;
    private float humanHeight;
    private float humanStepSize; // Размер шага
    private float humanJumpHeight; // Высота прыжка
    private Vector2 humanPosition = new Vector2(); // Вектор текущей позиции человечка
    private  Vector2 humanJumpVector = new Vector2(0, 0.1f); // Вектор направления прыжка
    private boolean isDirectionToRight = true; // Указатель направления движения
    private boolean isStopped = true; // Ключ остановлен ли человек
    private boolean gonnaToStop = false; // Ключ намерения остановиться (для прыжка), имитация инерции
    private boolean isJumping = false; // Ключ нахождения в прыжке
    private boolean isJumpingUp = true; // Ключ для направления в прыжке (снижение)

    Music music = Gdx.audio.newMusic(Gdx.files.internal("m/mario.mp3"));
    Sound soundSteps = Gdx.audio.newSound(Gdx.files.internal("m/steps.mp3"));
    Sound soundJumpUp = Gdx.audio.newSound(Gdx.files.internal("m/jump_up.mp3"));
    Sound soundJumpDown = Gdx.audio.newSound(Gdx.files.internal("m/jump_down.mp3"));

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

        walkSheet = new Texture(Gdx.files.internal("sprite-animation4.png"));
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);
        walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        walkAnimation = new Animation(0.025f, walkFrames);
        stateTime = 0f;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        imgWidth = worldBounds.getWidth() * IMG_WIDTH;
        imgHeight = (img.getHeight() / (float) img.getWidth()) * worldBounds.getWidth() * IMG_WIDTH;
        humanHeight = worldBounds.getWidth() * HUMAN_SIZE;
        humanWidth = humanHeight * HUMAN_WIDTH;
        humanPosition.x = worldBounds.getLeft();
        humanPosition.y = worldBounds.getBottom();
        humanStepSize = worldBounds.getWidth() * 0.003f;
        humanJumpHeight = humanHeight * 0.6f;
        music.setVolume(0.15f);                 // устанавливает громкость на половину максимального объема
        music.setLooping(true);                // повторное воспроизведение, пока не будет вызван music.stop()
        music.play();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stateTime += Gdx.graphics.getDeltaTime();
        if (isStopped) {
            currentFrame = (TextureRegion) walkAnimation.getKeyFrame(0.025f * 5f);
        } else {
            if (gonnaToStop && !isJumping) {
                isStopped = true;
                gonnaToStop = false;
            }
            currentFrame = (TextureRegion) walkAnimation.getKeyFrame(stateTime, true);
        }
        if (!isStopped) {
            humanPosition.x += (isDirectionToRight ? humanStepSize : -humanStepSize);
            if (humanPosition.x > worldBounds.getRight()) {
                humanPosition.x = worldBounds.getLeft() - humanWidth;
            }
            if (humanPosition.x < worldBounds.getLeft() - humanWidth) {
                humanPosition.x = worldBounds.getRight();
            }
        }
        if (isJumping) {
            humanPosition.add(humanJumpVector);
            if (isJumpingUp) {
                if (Math.abs(humanPosition.y - worldBounds.getBottom()) > humanJumpHeight) {
                    isJumpingUp = false;
                    humanJumpVector.set(0, -0.1f);
                }
            } else {
                if (Math.abs(humanPosition.y - worldBounds.getBottom()) < 0.1f) {
                    soundJumpDown.play(1.0f);
                    if (!isStopped) {
                        soundSteps.play();
                    }
                    humanPosition.y = worldBounds.getBottom();
                    isJumpingUp = true;
                    humanJumpVector.set(0, 0.1f);
                    isJumping = false;
                }
            }

        }
        batch.begin();
        batch.setColor(1, 1, 1, 1);
        batch.draw(currentFrame, humanPosition.x, humanPosition.y, humanWidth, humanHeight);
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

    private void flipHuman() {
        for (Object o : walkAnimation.getKeyFrames()) {
            ((TextureRegion) o).flip(true, false);
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
            case Input.Keys.A:
                gonnaToStop = true;
                soundSteps.stop();
                break;
            case Input.Keys.D:
                gonnaToStop = true;
                soundSteps.stop();
                break;
            case Input.Keys.W:

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
            case Input.Keys.A:
                if (!isJumping) { // В прыжке нельзя поменять направление движения
                    if (isDirectionToRight) { // Повернуть текстуру, если нужно
                        flipHuman();
                    }
                    isDirectionToRight = false;
                    isStopped = false;
                    gonnaToStop = false;
                    soundSteps.play(1.0f);
                }
                break;
            case Input.Keys.D:
                if (!isJumping) { // В прыжке нельзя поменять направление движения
                    if (!isDirectionToRight) { // Повернуть текстуру, если нужно
                        flipHuman();
                    }
                    isDirectionToRight = true;
                    isStopped = false;
                    gonnaToStop = false;
                    soundSteps.play(1.0f);
                }
                break;
            case Input.Keys.W:
                if (!isJumping) { // Прыгнуть, только если человечек находится не в прыжке
                    isJumping = true;
                    soundSteps.stop();
                    soundJumpUp.play(1.0f);
                }
                break;
        }
        return super.keyDown(keycode);
    }
}
