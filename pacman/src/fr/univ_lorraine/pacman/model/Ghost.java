package fr.univ_lorraine.pacman.model;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import fr.univ_lorraine.pacman.view.TextureFactory;

import com.badlogic.gdx.utils.TimeUtils;

/**
 * @author Martine Gautier, UniversitŽ de Lorraine
 * @author Laurent Bougrain, UniversitŽ de Lorraine
 */
public class Ghost extends GameMoveableBasicElement {

	private int numero;			  // identifiant du fant™me (entre 1 et 4)
	private long lastChasedTime;  // temps auquel Pacman a mangŽ une super pac-gomme
	private final float SPEED = super.SPEED*0.9f; //la vitesse des fant™mes est lŽgrement infŽrieure ˆ Pacman

	public Ghost(Vector2 pos, int num, World world) {
		super(pos, world);
		this.state = State.HUNTING;
		this.size = 0.8f;
		this.numero = num;
	}
	
	/** 
	 * On tente un pas et on gre les Žventuelles collisions
	 * Aprs 7 secondes, les fant™mes redeviennent chasseurs
	 */
	public void update(float delta) {
		if (((TimeUtils.millis() - lastChasedTime) > 7000)
		|| ((this.state == State.DEAD) && isInBase())) this.state = State.HUNTING; 
		super.update(delta);
	}
	
	boolean isInBase() {
		return ((this.position.x >= 11 && this.position.x <= 15)
		&& (this.position.y >= 15 && this.position.y <= 17));
	}
	
	@Override
	public Texture getTexture() {
		switch (this.state) {
			case HUNTING : 
					return TextureFactory.getInstance().getTextureGhost(numero);
			case HUNTED :  if ((TimeUtils.millis() - lastChasedTime) < 5000) 	  
				return TextureFactory.getInstance().getTextureGhostChased();
				else 
			return TextureFactory.getInstance().getTextureGhostChasedend();

			case DEAD : return TextureFactory.getInstance().getTextureGhostDead();
			
			default : return null;
		}
	}
	
	/** 
	 * DŽtecter les collisions avec les ŽlŽments fixes du labyrinthe et Pacman
	 */
	public void detectAndManageCollision() {
		//avec les Žlements fixes du labyrinthe;
		super.detectAndManageCollision();
		// avec pacman
		if (this.hasCollision(world.getPacman()))
			world.getPacman().manageCollision(this);
	}
	
	/** 
	 * GŽrer la collision avec un autre personnage
	 */
	@Override
	public void manageCollision(GameMoveableBasicElement g) {
//		System.out.println("Collision avec "+this);
		g.collision(this);
	}

	/** 
	 * GŽrer la collision avec une pellet
	 */
	@Override
	public void collision(Pellet p) {
		// Un fant™me passe ˆ travers une pellet
	}

	/** 
	 * GŽrer la collision avec une superPellet
	 */
	@Override
	public void collision(SuperPellet sp) {
		// Un fant™me passe ˆ travers une super pellet
	}

	/** 
	 * GŽrer la collision avec Pacman
	 */
	@Override
	public void collision(Pacman p) {
		// C'est Pacman qui va gŽrer
		p.collision(this);
	}

	/** 
	 * GŽrer la collision avec un autre fant™me
	 */
	@Override
	public void collision(Ghost g) {
		// Deux fant™mes se rentrent dedans ... Rien ˆ faire
	}

	/**
	 * Le fant™me est poursuivi
	 */
	public void setChased() {
		this.state = State.HUNTED;
	    lastChasedTime = TimeUtils.millis();
	}

 }
