package fr.univ_lorraine.pacman.controller;

import fr.univ_lorraine.pacman.controller.ghost.GhostStrategy;
import fr.univ_lorraine.pacman.controller.pacman.PacmanStrategy;
import fr.univ_lorraine.pacman.model.World;

/**
 * @author Martine Gautier, Université de Lorraine
 * @author Laurent Bougrain, Université de Lorraine
 */
public class GameController {

	boolean pacmanTurn;
	GhostController ghcontroller;
	PacmanController paccontroller;

	public GameController(World world, GhostStrategy ghostStrat, PacmanStrategy pacmanStrat) {
		pacmanTurn = true;
		ghcontroller = ghostStrat.getController(world);
		paccontroller = pacmanStrat.getController(world);
	}

	public GhostController getGhostController() {
		return ghcontroller;
	}

	public PacmanController getPacmanController() {
		return paccontroller;
	}

	/*
	 * Pac bouge si c'est son tour, sinon ce sont les fant�mes qui bougent
	 */
	public void update(float delta) {
		if (pacmanTurn)
			paccontroller.update(delta);
		else
			ghcontroller.update(delta);
		pacmanTurn = !pacmanTurn;
	}

}
