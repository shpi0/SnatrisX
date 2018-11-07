package ru.shpi0.snatrisx.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.shpi0.snatrisx.SnatrisX;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
//		float aspect = 3f / 4f;
		float aspect = 9f / 16f;
		config.height = 600;
		config.width = (int) (config.height * aspect);
		config.resizable = false;
		new LwjglApplication(new SnatrisX(), config);
	}
}
