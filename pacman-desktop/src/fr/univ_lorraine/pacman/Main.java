package fr.univ_lorraine.pacman;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * @author Martine Gautier, Université de Lorraine
 * @author Laurent Bougrain, Université de Lorraine
 */
public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Pacman";
		cfg.useGL20 = true; // enable 'useGL20' to make textures that not a
							// power of 2
		cfg.width = 540;
		cfg.height = 620;
		cfg.resizable = false;

		new LwjglApplication(new PacmanGame(), cfg);
	}
}
