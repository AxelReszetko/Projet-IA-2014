package fr.univ_lorraine.pacman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;

import fr.univ_lorraine.pacman.PacmanGame;
import fr.univ_lorraine.pacman.controller.GameController;
import fr.univ_lorraine.pacman.controller.ghost.GhostStrategy;
import fr.univ_lorraine.pacman.controller.pacman.PacmanStrategy;
import fr.univ_lorraine.pacman.model.World;
import fr.univ_lorraine.pacman.view.WorldRenderer;

/**
 * @author Martine Gautier, Universit� de Lorraine
 * @author Laurent Bougrain, Universit� de Lorraine
 */
public class GameScreen implements Screen {

	private PacmanGame game;

	private World world;
	private WorldRenderer renderer;
	private GameController gameController;

	public GameScreen(PacmanGame aGame, GhostStrategy ghostStrategy, PacmanStrategy pacmanStrategy) {
		game = aGame;

		world = new World();
		renderer = new WorldRenderer(world);
		gameController = new GameController(world, ghostStrategy, pacmanStrategy);
	}

	@Override
	public void show() {
	}

	/**
	 * @param delta
	 *            temps qui s'�coule entre deux rafraichissements permet
	 *            d'adpter la vitesse du jeu � la vitesse du processeur
	 */
	@Override
	public void render(float delta) {

		if (!world.getEndOfGame()) {
			// Les 2 seuls liens explicites avec OpenGL
			// Pour fixer la couleur du fond (RVB + alpha) :
			// Les coordonn�es RVB sont entre 0 et 1, alors qu'habituellement,
			// elles sont entre 0 et 255
			// donc on peut �crire 50/255f, 56/255f, 70/255f
			Gdx.gl.glClearColor(0, 0, 0, 1);

			// Pour effacer l'�cran et lui donner la couleur du fond
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			gameController.update(delta);
			renderer.render();
		} else {
			game.displayMenuScreen();
		}
	}

	/*
	 * The setSize (int w, int h) method will be called every time the screen is
	 * resized and it simply (re)calculates the units in pixels.
	 */
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

}
