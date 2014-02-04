package fr.univ_lorraine.pacman.controller.ghost;

import fr.univ_lorraine.pacman.controller.GhostController;
import fr.univ_lorraine.pacman.model.World;

public enum GhostStrategy {
	RANDOM {
		@Override
		public GhostController getController(World world) {
			return new GhostRandomStrategy(world);
		}
	},
        
        HUNTER {
		@Override
		public GhostController getController(World world) {
			return new GhostHunter(world);
		}
	};

	public abstract GhostController getController(World world);
}
