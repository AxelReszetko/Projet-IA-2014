package fr.univ_lorraine.pacman.controller.ghost;

import java.util.Iterator;

import fr.univ_lorraine.pacman.controller.GhostController;
import fr.univ_lorraine.pacman.model.Ghost;
import fr.univ_lorraine.pacman.model.World;

public class GhostRandomStrategy extends GhostController {

	public GhostRandomStrategy(World world) {
		super(world);
	}

	@Override
	public void update(float delta) {
		int direction;
		// On tente un pas dans la direction choisie
		Iterator<Ghost> iterGhost = world.ghostsIterator();
		while (iterGhost.hasNext()) {
			Ghost gh = iterGhost.next();
			direction = (int) (Math.random() * 4);
			switch (direction) {
			case 0:
				gh.turnLeft();
				break;
			case 1:
				gh.turnUp();
				break;
			case 2:
				gh.turnRight();
				break;
			case 3:
				gh.turnDown();
				break;
			}
	
			gh.update(delta);
		}
	}

}
