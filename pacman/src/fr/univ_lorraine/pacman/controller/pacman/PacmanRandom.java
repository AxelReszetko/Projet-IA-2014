package fr.univ_lorraine.pacman.controller.pacman;

import fr.univ_lorraine.pacman.controller.PacmanController;
import fr.univ_lorraine.pacman.model.Pacman;
import fr.univ_lorraine.pacman.model.World;

public class PacmanRandom extends PacmanController{
	private Pacman pac;
	private boolean prem;
	private long fin;
	private long duree;
	
	public PacmanRandom(World world) {
		super(world);
		System.out.println("PacmanRandomCreator");
		pac = world.getPacman();
		duree=200;
		prem=true;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(float delta) {		
		int direction;
		
		if (prem||System.currentTimeMillis() > fin){
			direction = (int) (Math.random() * 4);
			switch (direction) {
			case 0:
				pac.turnLeft();
				break;
			case 1:
				pac.turnUp();
				break;
			case 2:
				pac.turnRight();
				break;
			case 3:
				pac.turnDown();
				break;
			}
			prem=false;
			System.out.println("Update Pacman");
			fin = System.currentTimeMillis()+duree;
		}		
		pac.update(delta);
	}
	

}