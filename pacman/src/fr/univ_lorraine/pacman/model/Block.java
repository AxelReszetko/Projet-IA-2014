package fr.univ_lorraine.pacman.model;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import fr.univ_lorraine.pacman.view.TextureFactory;

/**
 * @author Martine Gautier, Universit� de Lorraine
 * @author Laurent Bougrain, Universit� de Lorraine
 */
public class Block extends GameBasicElement {
	
	public Block(Vector2 pos, World world) {
		super(pos, world);
	}

	@Override
	public Texture getTexture() {
		return TextureFactory.getInstance().getTextureBloc();
	}

	@Override
	public void manageCollision(GameMoveableBasicElement g) {
		g.collision(this);
	}

	

	
	
}
