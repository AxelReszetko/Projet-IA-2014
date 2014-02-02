package fr.univ_lorraine.pacman.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import fr.univ_lorraine.pacman.view.WorldRenderer;
//import com.badlogic.gdx.math.Rectangle;

/**
 * @author Martine Gautier, Universit� de Lorraine
 * @author Laurent Bougrain, Universit� de Lorraine
 */
public abstract class GameBasicElement implements GameElement {

	protected Vector2 position ;
	protected World world;
	protected float size = 1f;
	
	
	protected GameBasicElement(Vector2 pos, World world) {
		this.position = pos;
		this.world = world;
	}

	public abstract Texture getTexture() ;
	
	/** 
	 * G�rer la collision d�tect�e entre this et p
	 * @param p : un �l�ment qui bouge 
	 */
	public abstract void manageCollision(GameMoveableBasicElement p) ;
	
	public Vector2 getPosition() {
		return this.position;
	}
	
	public float getWidth() {
		return size;
	}

	public float getHeight() {
		return size;
	}
	
	public int getPoint() {
		return 0;
	}
	
	public  void draw(SpriteBatch spriteBatch) {
		int ppuX = WorldRenderer.PPUX;
		int ppuY = WorldRenderer.PPUY;
		spriteBatch.draw(getTexture(), getPosition().x * ppuX, getPosition().y * ppuY, getWidth() * ppuX, getHeight() * ppuY);
	}
	

		public boolean hasCollision(GameBasicElement ge) {
		  if((ge.position.x >= this.position.x + this.getWidth())      // trop � droite
					|| (ge.position.x + ge.getWidth() <= this.position.x) // trop � gauche
					|| (ge.position.y >= this.position.y + this.getHeight()) // trop en bas
					|| (ge.position.y + ge.getHeight() <= this.position.y))  // trop en haut
				          return false; 
				   else
				          return true; 
	}

}
