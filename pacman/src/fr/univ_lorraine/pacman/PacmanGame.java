package fr.univ_lorraine.pacman;

import com.badlogic.gdx.Game;

import fr.univ_lorraine.pacman.controller.ghost.GhostStrategy;
import fr.univ_lorraine.pacman.controller.pacman.PacmanStrategy;
import fr.univ_lorraine.pacman.screens.GameScreen;
import fr.univ_lorraine.pacman.screens.MenuScreen;

/**
 * @author Martine Gautier, Université de Lorraine
 * @author Laurent Bougrain, Université de Lorraine
 */
public class PacmanGame extends Game {

	MenuScreen ms = new MenuScreen(this);

	@Override
	public void create() {
		displayMenuScreen();
	}

	public void displayMenuScreen() {
		setScreen(ms);
	}

	public void startGame(GhostStrategy ghostStrategy, PacmanStrategy pacmanStrategy) {
		setScreen(new GameScreen(this, ghostStrategy, pacmanStrategy));
	}

}
