package ru.shpi0.snatrisx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SnatrisX extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img = null;

	int width = 0;
	int height = 0;
	int posX = 0;
	int posY = 0;
	float a = 0f;

	public SnatrisX() {
		final Thread logoColoringThread = new Thread(new Runnable() {
			@Override
			public void run() {
				boolean growUp = true;
				while (true) {
					if (growUp) {
						a += 0.006f;
					} else {
						a -= 0.006f;
					}
					if (a >= 1f) {
						growUp = false;
					}
					if (a <= 0.007f) {
						growUp = true;
					}
					try {
						Thread.sleep(16);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		logoColoringThread.setDaemon(true);
		logoColoringThread.start();

		Thread moveThread = new Thread(new Runnable() {
			@Override
			public void run() {
				int x = 1;
				int y = 1;
				while (true) {
					if (img != null) {
						width = Gdx.graphics.getWidth();
						height = Gdx.graphics.getHeight();
						if (img.getHeight() + posY > height || posY < 0) {
							y *= -1;
						}
						if (img.getWidth() + posX > width || posX < 0) {
							x *= -1;
						}
						posX += x;
						posY += y;
					}
					try {
						Thread.sleep(16);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		moveThread.setDaemon(true);
		moveThread.start();
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("snatris_logo.png");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.setColor(1, 1, 1, a);
		batch.draw(img, posX, posY);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
