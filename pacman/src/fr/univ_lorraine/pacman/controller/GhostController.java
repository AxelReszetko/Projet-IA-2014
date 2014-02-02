package fr.univ_lorraine.pacman.controller;

import fr.univ_lorraine.pacman.model.World;

/**
 * @author Martine Gautier, Universite de Lorraine
 * @author Laurent Bougrain, Universite de Lorraine
 */
public abstract class GhostController {
	protected World world;

	public GhostController(World world) {
		this.world = world;
	}

	public abstract void update(float delta);
}
