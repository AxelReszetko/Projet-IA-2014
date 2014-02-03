package fr.univ_lorraine.pacman.model;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Martine Gautier, Université de Lorraine
 * @author Laurent Bougrain, Université de Lorraine
 */
public class World implements GameElement {

	private GameBasicElement[][] gel; // tableau contenant les blocs, les
										// pellets et les super pellets
	private ArrayList<GameElement> pellets; // les pellets et super pellets
	private ArrayList<Ghost> ghosts; // les fant�mes
	private Pacman pacman; // Pacman
	private int height, width; // largeur, hauteur du tableau
	private int nbPelletsRestantes; // nombre de pellets et super pellets
									// restantes en jeu
	private int score; // nombre de points marqu�s par Pacman
	private boolean endOfGame; // indicateur de fin de jeu (vrai si Pacman a
								// gagn� ou si les fant�mes ont gagn�

	public World() {
		this.nbPelletsRestantes = 0;
		loadMap();
		ghosts = new ArrayList<Ghost>();
		ghosts.add(new Ghost(new Vector2(13, 17), 1, this));
		ghosts.add(new Ghost(new Vector2(13, 15), 2, this));
		ghosts.add(new Ghost(new Vector2(15, 15), 3, this));
		ghosts.add(new Ghost(new Vector2(11, 15), 4, this));
		pacman = new Pacman(new Vector2(13, 7), this);
		this.score = 0;
		this.endOfGame = false;
	}

	public Pacman getPacman() {
		return pacman;
	}

	/**
	 * @return It�rateur de fant�mes
	 */
	public Iterator<Ghost> ghostsIterator() {
		return ghosts.iterator();
	}

	public GameBasicElement getElement(int col, int lig) {
		return gel[col][lig];
	}

	public int getNbPelletsRestantes() {
		return this.nbPelletsRestantes;
	}

	public boolean getEndOfGame() {
		return this.endOfGame;
	}

	public void setEndOfGame(boolean value) {
		this.endOfGame = value;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public void addScore(int value) {
		this.score += value;
		System.out.println("SCORE = " + this.score);
	}

	/*
	 * Pour effacer une pellet ou une superPellet
	 */
	public void remove(GameBasicElement pellet) {
		pellets.remove(pellet);
		Vector2 pos = pellet.getPosition();
		gel[(int) pos.x][(int) pos.y] = null;

		nbPelletsRestantes--;
		System.out.println("nb de pac-gommes restantes = " + this.nbPelletsRestantes);

		this.addScore(pellet.getPoint());

		if (nbPelletsRestantes == 0) {
			System.out.println("pacman gagne !");
			this.setEndOfGame(true);
		}
	}

	public void draw(SpriteBatch spriteBatch) {
		for (GameElement ge : this.pellets) {
			ge.draw(spriteBatch);
		}
		for (GameElement ge : this.ghosts) {
			ge.draw(spriteBatch);
		}
		pacman.draw(spriteBatch);
	}

	private void loadMap() {
		width = laby.length;
		height = laby[0].length;
		gel = new GameBasicElement[width][height];
		pellets = new ArrayList<GameElement>();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				switch (laby[i][j]) {
				case 0:
					gel[i][j] = new Pellet(new Vector2(i, j), this);
					this.nbPelletsRestantes++;
					pellets.add(gel[i][j]);
					break;
				case 1:
					gel[i][j] = new Block(new Vector2(i, j), this);
					break;
				case 2:
					gel[i][j] = new SuperPellet(new Vector2(i, j), this);
					this.nbPelletsRestantes++;
					pellets.add(gel[i][j]);
					break;
				}
			}
		}
		System.out.println("nb initial de pac-gommes : " + this.nbPelletsRestantes);
	}

	static private int laby[][] = {
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 1, 0, 0, 0, 0, 1, 1, 2, 0, 0, 0, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 2, 0, 0, 1 },
			{ 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 1, 1, 3, 3, 3, 3, 3, 3, 3, 1, 1, 0, 0, 0, 0, 1, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 3, 1, 3, 3, 3, 1, 3, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1 },
			{ 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 3, 1, 3, 3, 3, 1, 3, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 1, 1, 1, 1, 1, 5, 1, 1, 1, 1, 1, 3, 1, 3, 3, 3, 3, 3, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1 },
			{ 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 3, 1, 3, 3, 3, 1, 3, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 3, 1, 3, 3, 3, 1, 3, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 1, 1, 3, 3, 3, 3, 3, 3, 3, 1, 1, 0, 0, 0, 0, 1, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1 },
			{ 1, 0, 0, 0, 0, 1, 1, 2, 0, 0, 0, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 2, 0, 0, 1 },
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 } };

}
