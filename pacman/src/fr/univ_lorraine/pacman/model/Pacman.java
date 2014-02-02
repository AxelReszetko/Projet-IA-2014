package fr.univ_lorraine.pacman.model;

import java.util.Iterator;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import fr.univ_lorraine.pacman.view.TextureFactory;

import com.badlogic.gdx.utils.TimeUtils;


/**
 * @author Martine Gautier, Université de Lorraine
 * @author Laurent Bougrain, Université de Lorraine
 */
public class Pacman extends GameMoveableBasicElement {
	
	private long lastChasingTime;
	int nbGhostsKilled;
	
	public Pacman(Vector2 pos, World world) {
		super(pos, world);
		this.state = State.HUNTED;
		this.size = 0.8f;
		this.direction = Direction.LEFT;
		this.nbGhostsKilled = 0;
	}

	public void update(float delta) {
		if ((TimeUtils.millis() - lastChasingTime) > 7000) { 
			this.state = State.HUNTED;
			this.nbGhostsKilled = 0;
		}
		super.update(delta);
	}
	
	@Override
	public Texture getTexture() {
		TextureFactory tf = TextureFactory.getInstance();
		switch (direction) {
			case LEFT :  return tf.getTexturePacmanLeft();
			case RIGHT : return tf.getTexturePacmanRight();
			case UP :    return tf.getTexturePacmanUp();
			case DOWN :  return tf.getTexturePacmanDown();
		}
		return null;
	}
		

	//////////////////////////////////////////////////////////////////////////
	
	
	//////////////////////////////////////////////////////////////////////////
	public void detectAndManageCollision() {
		//avec les élements fixes du labyrinthe;
		super.detectAndManageCollision();
		// avec les fantômes
		Iterator<Ghost> iterGhost = world.ghostsIterator();
		while (iterGhost.hasNext()) {
			Ghost gh = iterGhost.next();
		      if (this.hasCollision(gh)) {
		    	  gh.manageCollision(this);
		      }
		}		
	}
	
	
	/** 
	 * Gérer la collision avec une pellet
	 */
	public void collision(Pellet p) {
		world.remove(p);
	}
	
	/** 
	 * Gérer la collision avec une super pellet
	 */
	public void collision(SuperPellet sp) {
		world.remove(sp);
		this.state = State.HUNTING;
		lastChasingTime = TimeUtils.millis();
		// Prévenir les fantômes qu'ils sont poursuivis
		Iterator<Ghost> iterGhost = world.ghostsIterator() ;
        while (iterGhost.hasNext()) {
            Ghost gh = iterGhost.next();
            gh.setChased();
        } 
	}

	/** 
	 * Gérer la collision avec Pacman
	 */
	public void collision(Pacman p) {
		throw new RuntimeException ("Bug sévère : Pacman téléscope Pacman");
	}

	
	/** 
	 * Gérer la collision avec un fantôme
	 */
	public void collision(Ghost g) {
		switch (g.getState()) {
			case HUNTED : {
				g.kill();
				this.nbGhostsKilled++;
				world.addScore(400*nbGhostsKilled);
				break;
			}
			case HUNTING : { 
				this.kill(); 
				this.state = State.DEAD;
				System.out.println("les fantômes gagnent !"); 
				world.setEndOfGame(true);
				break;
				}
			default: //DEAD : System.out.println("le fantôme est mort !");  break;
		}
	}

	/** 
	 * Gérer la collision avec un autre personnage
	 */
	public void manageCollision(GameMoveableBasicElement g) {
		g.collision(this);
	}

	
 }
