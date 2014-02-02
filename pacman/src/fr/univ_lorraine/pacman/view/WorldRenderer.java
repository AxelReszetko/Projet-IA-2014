package fr.univ_lorraine.pacman.view;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.univ_lorraine.pacman.model.World;

/**
 * @author Martine Gautier, Université de Lorraine
 * @author Laurent Bougrain, Université de Lorraine
 */
public class WorldRenderer {

	// taille d'un bloc, en pixels
	public static int PPUX = 20;
	public static int PPUY = 20;
	
	private World world;							// le monde
	private SpriteBatch spriteBatch;				// la feuille de dessin	
	
	public WorldRenderer(World world) {
		this.world = world;
		this.spriteBatch = new SpriteBatch();
	}
	
	/**
	 * Pour afficher
	 */
	public void render() {
		spriteBatch.begin();    // indispensable, sinon java.lang.IllegalStateException: SpriteBatch.begin must be called before draw.
		spriteBatch.draw(TextureFactory.getInstance().getMaze(), 0, 0, 540, 620);
		world.draw(spriteBatch);
		spriteBatch.end();
	}


}
