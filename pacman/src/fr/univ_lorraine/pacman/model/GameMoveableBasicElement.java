package fr.univ_lorraine.pacman.model;

import com.badlogic.gdx.math.Vector2;

/**
 * @author Martine Gautier, Université de Lorraine
 * @author Laurent Bougrain, Université de Lorraine
 */
public abstract class GameMoveableBasicElement extends GameBasicElement {
	
    protected boolean isMoving;				// indique si l'élément est en mouvement
	protected final float SPEED = 8.0f;    // vitesse du mouvement
	protected Direction direction ;			// direction du mouvement
	public enum Direction {
		LEFT, RIGHT, UP, DOWN
	}
    
    protected State state;  
    public enum State {
		HUNTING, HUNTED, DEAD
	}
   	
	public GameMoveableBasicElement(Vector2 pos, World world) {
		super(pos, world);
		this.isMoving = false;
		this.direction = Direction.LEFT;
	}

	/**
	 * Tuer l'élément
	 */
	public void kill() {
		this.state = State.DEAD;		
	}

	/**
	 * On tente un pas et on gère les éventuelles collisions
	 * @param delta
	 */
	public void update(float delta) {
		if (isMoving) {
			this.goForward(delta);
			// Détecter et gérer les collisions
			// avec les élements fixes du labyrinthe
			this.detectAndManageCollision();
		}
	}
	//////////////////////////////////////////////////////////////////////////
	/**
	 * récupération de l'état
	 */
	public State getState() {
		return state;
	}
	
	/**
	 * Changer de direction
	 */
	public void turnLeft() {
		isMoving = true;
		this.direction = Direction.LEFT;
	}
	
	public void turnRight() {
		isMoving = true;
		this.direction = Direction.RIGHT;
	}
	
	public void turnUp() {
		isMoving = true;
		this.direction = Direction.UP;
	}
	
	public void turnDown() {
		isMoving = true;
		this.direction = Direction.DOWN;
	}
	
	//////////////////////////////////////////////////////////////////////////
	/**
	 * Avancer d'un pas selon la direction
	 */
	public void goForward(float delta) {
        if (this.direction==Direction.LEFT)  if ((position.x-delta*SPEED) < 0) position.x=world.getWidth()-this.getWidth()-delta*SPEED; else position.x=position.x-delta*SPEED;
        if (this.direction==Direction.RIGHT) if ((position.x+this.getWidth()+delta*SPEED) >= world.getWidth()) position.x=0.0f; else position.x=position.x+delta*SPEED;        
        if (this.direction==Direction.UP)    position.y=position.y+delta*SPEED;        
        if (this.direction==Direction.DOWN)  position.y=position.y-delta*SPEED;                        
}
	
	
	//////////////////////////////////////////////////////////////////////////
	/**
	 * Détection d'une collision éventuelle 
	 */
	public void detectAndManageCollision() {
		// A affiner pour prendre en compte la direction
		Vector2 pos = this.getPosition() ;
		int col, lig;
		GameBasicElement element;

		// haut droite
		col = (int)(pos.x+this.getWidth());
		lig = (int)(pos.y+this.getHeight()); //bis
		element = world.getElement(col, lig);
		if (element!=null) element.manageCollision(this);
		
		//bas, droite
		col = (int)(pos.x+this.getWidth()); //bis
		lig = (int)pos.y;
		element = world.getElement(col, lig);
		if (element!=null) element.manageCollision(this);
		
		// bas gauche
		col = (int)pos.x;
		lig = (int)pos.y;
		element = world.getElement(col, lig);
		if (element!=null) element.manageCollision(this);

		// bas haut
		col = (int)pos.x; //bis
		lig = (int)(pos.y+this.getHeight());
		element = world.getElement(col, lig);
		if (element!=null) element.manageCollision(this);

	}

	/** 
	 * Gérer la collision avec un bloc
	 */
	public void collision(Block b) {
		this.isMoving = false;
		// Reculer pour se repositionner au contact du bloc
		switch (direction) {
		case LEFT : position.x=b.position.x+1.0f; break;
		case RIGHT : position.x=b.position.x-this.getWidth()-0.01f; break;
		case UP :    position.y=b.position.y-this.getHeight()-0.01f; break;
		case DOWN : position.y=b.position.y+1.0f; break;
		}
	}
	
	/** 
	 * Gérer la collision avec une pellet
	 */
	public abstract void collision(Pellet p) ;
	
	/** 
	 * Gérer la collision avec une super pellet
	 */
	public abstract void collision(SuperPellet sp);
	
	/** 
	 * Gérer la collision avec Pacman
	 */
	public abstract void collision(Pacman p);
	
	/** 
	 * Gérer la collision avec un fantôme
	 */
	public abstract void collision(Ghost g);
	
}
