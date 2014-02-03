package fr.univ_lorraine.pacman.controller;

import fr.univ_lorraine.pacman.model.World;

/**
 * @author Martine Gautier, Université de Lorraine
 * @author Laurent Bougrain, Université de Lorraine
 */
public abstract class PacmanController {
	protected World world;

	public PacmanController(World world) {
		this.world = world;
	}

	public abstract void update(float delta);
}
