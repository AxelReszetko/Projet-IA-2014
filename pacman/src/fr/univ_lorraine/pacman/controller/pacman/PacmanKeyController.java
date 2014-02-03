package fr.univ_lorraine.pacman.controller.pacman;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import fr.univ_lorraine.pacman.controller.PacmanController;
import fr.univ_lorraine.pacman.model.Pacman;
import fr.univ_lorraine.pacman.model.World;

/**
 * @author Martine Gautier, Université de Lorraine
 * @author Laurent Bougrain, Université de Lorraine
 */
public class PacmanKeyController extends PacmanController implements InputProcessor {

	enum Keys {
		LEFT, RIGHT, UP, DOWN
	}

	// Pour m�moriser les touches
	private Map<Keys, Boolean> keys = new HashMap<Keys, Boolean>();
	private Pacman pac;

	public PacmanKeyController(World world) {
		super(world);
		pac = world.getPacman();
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
		keys.put(Keys.UP, false);
		keys.put(Keys.DOWN, false);
		Gdx.input.setInputProcessor(this);
	}

	// Pour m�moriser une action de l'utilisateur
	public void leftPressed() {
		keys.put(Keys.LEFT, true);
	}

	public void leftReleased() {
		keys.put(Keys.LEFT, false);
	}

	public void rightPressed() {
		keys.put(Keys.RIGHT, true);
	}

	public void rightReleased() {
		keys.put(Keys.RIGHT, false);
	}

	public void upPressed() {
		keys.put(Keys.UP, true);
	}

	public void upReleased() {
		keys.put(Keys.UP, false);
	}

	public void downPressed() {
		keys.put(Keys.DOWN, true);
	}

	public void downReleased() {
		keys.put(Keys.DOWN, false);
	}

	/*
	 * Pour faire avancer Pacman
	 */
	public void update(float delta) {
		// System.out.println("pacman="+pac);
		// On tente un pas dans la direction choisie
		if (keys.get(Keys.LEFT))
			pac.turnLeft();

		if (keys.get(Keys.RIGHT))
			pac.turnRight();

		if (keys.get(Keys.UP))
			pac.turnUp();

		if (keys.get(Keys.DOWN))
			pac.turnDown();

		pac.update(delta);

	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Input.Keys.UP:
			upPressed();
			break;
		case Input.Keys.DOWN:
			downPressed();
			break;
		case Input.Keys.LEFT:
			leftPressed();
			break;
		case Input.Keys.RIGHT:
			rightPressed();
			break;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		case Input.Keys.UP:
			upReleased();
			break;
		case Input.Keys.DOWN:
			downReleased();
			break;
		case Input.Keys.LEFT:
			leftReleased();
			break;
		case Input.Keys.RIGHT:
			rightReleased();
			break;
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
