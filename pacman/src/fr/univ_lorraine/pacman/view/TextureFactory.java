package fr.univ_lorraine.pacman.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * @author Martine Gautier, Université de Lorraine
 * @author Laurent Bougrain, Université de Lorraine
 */
public class TextureFactory {

	private static TextureFactory instance = new TextureFactory();
	private TextureFactory() {
		
	}
	
	public static TextureFactory getInstance() {
		return instance;
	}
	/**
	private Texture bloc = new Texture(Gdx.files.internal("C:/Users/Fabien/Documents/GitHub/Projet-IA-2014/pacman-desktop/assets/images/bloc.png"));
	private Texture maze = new Texture(Gdx.files.internal("C:/Users/Fabien/Documents/GitHub/Projet-IA-2014/pacman-desktop/assets/images/maze.png"));
	private Texture pellet = new Texture(Gdx.files.internal("C:/Users/Fabien/Documents/GitHub/Projet-IA-2014/pacman-desktop/assets/images/pellet.png"));
	private Texture pacmanLeft = new  Texture(Gdx.files.internal("C:/Users/Fabien/Documents/GitHub/Projet-IA-2014/pacman-desktop/assets/images/pacmanLeft.png"));
	private Texture pacmanRight = new  Texture(Gdx.files.internal("C:/Users/Fabien/Documents/GitHub/Projet-IA-2014/pacman-desktop/assets/images/pacmanRight.png"));
	private Texture pacmanUp = new  Texture(Gdx.files.internal("C:/Users/Fabien/Documents/GitHub/Projet-IA-2014/pacman-desktop/assets/images/pacmanUp.png"));
	private Texture pacmanDown = new  Texture(Gdx.files.internal("C:/Users/Fabien/Documents/GitHub/Projet-IA-2014/pacman-desktop/assets/images/pacmanDown.png"));
	private Texture[] ghosts = {new  Texture(Gdx.files.internal("C:/Users/Fabien/Documents/GitHub/Projet-IA-2014/pacman-desktop/assets/images/ghost1.png")),
								new  Texture(Gdx.files.internal("C:/Users/Fabien/Documents/GitHub/Projet-IA-2014/pacman-desktop/assets/images/ghost2.png")),
								new  Texture(Gdx.files.internal("C:/Users/Fabien/Documents/GitHub/Projet-IA-2014/pacman-desktop/assets/images/ghost3.png")),
								new  Texture(Gdx.files.internal("C:/Users/Fabien/Documents/GitHub/Projet-IA-2014/pacman-desktop/assets/images/ghost4.png")) };
	private Texture ghostChased = new  Texture(Gdx.files.internal("C:/Users/Fabien/Documents/GitHub/Projet-IA-2014/pacman-desktop/assets/images/ghostChased.png"));
	private Texture ghostChasedend = new  Texture(Gdx.files.internal("C:/Users/Fabien/Documents/GitHub/Projet-IA-2014/pacman-desktop/assets/images/ghostChasedend.png"));
	private Texture ghostDead = new  Texture(Gdx.files.internal("C:/Users/Fabien/Documents/GitHub/Projet-IA-2014/pacman-desktop/assets/images/ghostDead.png"));
	private Texture superPellet = new  Texture(Gdx.files.internal("C:/Users/Fabien/Documents/GitHub/Projet-IA-2014/pacman-desktop/assets/images/superPellet.png"));
        /**/
        private Texture bloc = new Texture(Gdx.files.internal("images/bloc.png"));
	private Texture maze = new Texture(Gdx.files.internal("images/maze.png"));
	private Texture pellet = new Texture(Gdx.files.internal("images/pellet.png"));
	private Texture pacmanLeft = new  Texture(Gdx.files.internal("images/pacmanLeft.png"));
	private Texture pacmanRight = new  Texture(Gdx.files.internal("images/pacmanRight.png"));
	private Texture pacmanUp = new  Texture(Gdx.files.internal("images/pacmanUp.png"));
	private Texture pacmanDown = new  Texture(Gdx.files.internal("images/pacmanDown.png"));
	private Texture[] ghosts = {new  Texture(Gdx.files.internal("images/ghost1.png")),
								new  Texture(Gdx.files.internal("images/ghost2.png")),
								new  Texture(Gdx.files.internal("images/ghost3.png")),
								new  Texture(Gdx.files.internal("images/ghost4.png")) };
	private Texture ghostChased = new  Texture(Gdx.files.internal("images/ghostChased.png"));
	private Texture ghostChasedend = new  Texture(Gdx.files.internal("images/ghost1.png"));
	private Texture ghostDead = new  Texture(Gdx.files.internal("images/ghostDead.png"));

	private Texture superPellet = new  Texture(Gdx.files.internal("images/superPellet.png"));
	//private Texture superPellet = new  Texture(Gdx.files.internal("images/ghostDead.png"));
	/**/
	public Texture getTextureBloc() {
		return bloc ;
	}
	
	public Texture getTexturePellet() {
		return pellet ;
	}
	
	public Texture getTexturePowerPellet() {
		return superPellet ;
	}
	
	public Texture getTexturePacmanLeft() {
		return pacmanLeft ;
	}
	public Texture getTexturePacmanRight() {
		return pacmanRight ;
	}
	public Texture getTexturePacmanUp() {
		return pacmanUp;
	}
	public Texture getTexturePacmanDown() {
		return pacmanDown;
	}

	public Texture getTextureGhost(int num) {
		return ghosts[num-1];
	}

	public Texture getTextureGhostChased() {
		return ghostChased;
	}
	
	public Texture getTextureGhostChasedend() {
		return ghostChasedend;
	}
	
	public Texture getTextureGhostDead() {
		return ghostDead;
	}
	
	public Texture getMaze() {
		return maze;
	}
	

}
