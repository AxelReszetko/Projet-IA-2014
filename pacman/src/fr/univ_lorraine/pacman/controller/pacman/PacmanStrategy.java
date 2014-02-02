package fr.univ_lorraine.pacman.controller.pacman;

import fr.univ_lorraine.pacman.controller.PacmanController;
import fr.univ_lorraine.pacman.model.World;

public enum PacmanStrategy {
	KEY {
		@Override
		public PacmanController getController(World world) {
			return new PacmanKeyController(world);
		}

	},
	
	RANDOM {
		@Override
		public PacmanController getController(World world) {
			
			return new PacmanRandom(world);
		}

	},
	
	TEST {
		@Override
		public PacmanController getController(World world) {
			
			return new PacmanTest(world);
		}

	}
	;
	

	public abstract PacmanController getController(World world);
}
