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
        
	WTF {
		@Override
		public PacmanController getController(World world) {
			return new PacmanWTF(world);
		}

	},
	RANDOM {
		@Override
		public PacmanController getController(World world) {
			
			return new PacmanRandom(world);
		}

	},
        
        QUANTUM {
		@Override
		public PacmanController getController(World world) {
			
			return new QuantumPacman(world);
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
