package ru.shpi0.snatrisx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.shpi0.snatrisx.SnatrisX;
import ru.shpi0.snatrisx.base.BaseScreen;
import ru.shpi0.snatrisx.game.GameField;
import ru.shpi0.snatrisx.math.Rect;
import ru.shpi0.snatrisx.sprite.Background;
import ru.shpi0.snatrisx.sprite.ButtonDiffLvlEasy;
import ru.shpi0.snatrisx.sprite.ButtonDiffLvlHard;
import ru.shpi0.snatrisx.sprite.ButtonDiffLvlNormal;
import ru.shpi0.snatrisx.sprite.ButtonExit;
import ru.shpi0.snatrisx.sprite.ButtonHowTo;
import ru.shpi0.snatrisx.sprite.ButtonStart;
import ru.shpi0.snatrisx.sprite.Logo;
import ru.shpi0.snatrisx.sprite.MusicButton;

public class MenuScreen extends BaseScreen {

    private Texture backgroundTexture;
    private Background bg;

    private Texture logoTexture;
    private Logo logo;

    private TextureAtlas buttonsAtlas;
    private ButtonStart buttonStart;
    private ButtonExit buttonExit;
    private ButtonHowTo buttonHowTo;
    private ButtonDiffLvlEasy buttonDiffLvlEasy;
    private ButtonDiffLvlNormal buttonDiffLvlNormal;
    private ButtonDiffLvlHard buttonDiffLvlHard;

    private MusicButton musicOnButton;
    private MusicButton musicOffButton;
    private TextureAtlas miniButtonsAtlas;

    private float alpha = 0f; // Коэффициент прозрачности (альфа канал)
    private boolean growUp = true; // Ключ для мерцания, если true, то яркость увеличивается, иначе уменьшается
    private boolean isChoosingDiffLvl = false;

    public MenuScreen(SnatrisX game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        miniButtonsAtlas = new TextureAtlas("gamebtns.atlas");
        musicOnButton = new MusicButton(new TextureRegion(miniButtonsAtlas.findRegion("musicon")));
        musicOffButton = new MusicButton(new TextureRegion(miniButtonsAtlas.findRegion("musicoff")));
        backgroundTexture = new Texture("bg.jpg");
        bg = new Background(new TextureRegion(backgroundTexture));
        logoTexture = new Texture("logo.png");
        logo = new Logo(new TextureRegion(logoTexture));
        buttonsAtlas = new TextureAtlas("btn.atlas");
        buttonStart = new ButtonStart(new TextureRegion(buttonsAtlas.findRegion("btn_start")));
        buttonHowTo = new ButtonHowTo(new TextureRegion(buttonsAtlas.findRegion("btn_instr")));
        buttonExit = new ButtonExit(new TextureRegion(buttonsAtlas.findRegion("btn_exit")));
        buttonDiffLvlEasy = new ButtonDiffLvlEasy(new TextureRegion(buttonsAtlas.findRegion("btn_lvl_easy")));
        buttonDiffLvlNormal = new ButtonDiffLvlNormal(new TextureRegion(buttonsAtlas.findRegion("btn_lvl_normal")));
        buttonDiffLvlHard = new ButtonDiffLvlHard(new TextureRegion(buttonsAtlas.findRegion("btn_lvl_hard")));
    }

    @Override
    public void resize(Rect worldBounds) {
        bg.resize(worldBounds);
        logo.resize(worldBounds);
        buttonStart.resize(worldBounds);
        buttonHowTo.resize(worldBounds);
        buttonExit.resize(worldBounds);
        buttonDiffLvlEasy.resize(worldBounds);
        buttonDiffLvlNormal.resize(worldBounds);
        buttonDiffLvlHard.resize(worldBounds);
        musicOnButton.resize(worldBounds);
        musicOffButton.resize(worldBounds);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        draw();
    }

    private void draw() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        bg.draw(batch);
        logo.draw(batch);
        if (!isChoosingDiffLvl) {
            buttonStart.draw(batch);
            buttonHowTo.draw(batch);
            buttonExit.draw(batch);
        } else {
            buttonDiffLvlEasy.draw(batch);
            buttonDiffLvlNormal.draw(batch);
            buttonDiffLvlHard.draw(batch);
        }
        if (game.isMusicEnabled()) {
            musicOnButton.draw(batch);
        } else {
            musicOffButton.draw(batch);
        }
        batch.end();
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
        miniButtonsAtlas.dispose();
        backgroundTexture.dispose();
        logoTexture.dispose();
        buttonsAtlas.dispose();
        super.dispose();
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if (!isChoosingDiffLvl) {
            if (buttonExit.isMe(touch)) {
                Gdx.app.exit();
            }
            if (buttonStart.isMe(touch)) {
                isChoosingDiffLvl = true;
            }
            if (buttonHowTo.isMe(touch)) {
                game.setScreen(new AboutScreen(game));
            }
        } else {
            if (buttonDiffLvlEasy.isMe(touch)) {
                GameField.getInstance().setDifficultModificator(1f);
                game.setScreen(new GameScreen(game));
            }
            if (buttonDiffLvlNormal.isMe(touch)) {
                GameField.getInstance().setDifficultModificator(0.75f);
                game.setScreen(new GameScreen(game));
            }
            if (buttonDiffLvlHard.isMe(touch)) {
                GameField.getInstance().setDifficultModificator(0.65f);
                game.setScreen(new GameScreen(game));
            }
        }
        buttonExit.setScale(1f);
        buttonHowTo.setScale(1f);
        buttonStart.setScale(1f);
        buttonDiffLvlEasy.setScale(1f);
        buttonDiffLvlNormal.setScale(1f);
        buttonDiffLvlHard.setScale(1f);
        return super.touchUp(touch, pointer);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if (!isChoosingDiffLvl) {
            if (buttonExit.isMe(touch)) {
                buttonExit.setScale(1.1f);
            }
            if (buttonStart.isMe(touch)) {
                buttonStart.setScale(1.1f);
            }
            if (buttonHowTo.isMe(touch)) {
                buttonHowTo.setScale(1.1f);
            }
        } else {
            if (buttonDiffLvlEasy.isMe(touch)) {
                buttonDiffLvlEasy.setScale(1.1f);
            }
            if (buttonDiffLvlNormal.isMe(touch)) {
                buttonDiffLvlNormal.setScale(1.1f);
            }
            if (buttonDiffLvlHard.isMe(touch)) {
                buttonDiffLvlHard.setScale(1.1f);
            }
            if (!musicOnButton.isMe(touch) && !buttonDiffLvlEasy.isMe(touch) && !buttonDiffLvlNormal.isMe(touch) && !buttonDiffLvlHard.isMe(touch)) {
                isChoosingDiffLvl = false;
            }
        }
        if (musicOnButton.isMe(touch)) {
            if (game.isMusicEnabled()) {
                game.musicStop();
                game.setMusicEnabled(false);
            } else {
                game.musicPlay();
                game.setMusicEnabled(true);
            }
        }
        return super.touchDown(touch, pointer);
    }
}
