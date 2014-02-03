package fr.univ_lorraine.pacman.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import fr.univ_lorraine.pacman.view.TextureFactory;

/**
 * @author Martine Gautier, Université de Lorraine
 * @author Laurent Bougrain, Université de Lorraine
 */
public class SuperPellet extends GameBasicElement {
	
	public SuperPellet(Vector2 pos, World world) {
		super(pos, world);
	}

	@Override
	public Texture getTexture() {
		return TextureFactory.getInstance().getTexturePowerPellet();
	}

	/** 
	 * G�rer la collision d�tect�e entre this et p
	 * @param p : un �l�ment qui bouge 
	 */
	@Override
	public void manageCollision(GameMoveableBasicElement g) {
		g.collision(this);
	}

	public int getPoint() {
		return 50;
	}

}
