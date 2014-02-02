package fr.univ_lorraine.pacman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.univ_lorraine.pacman.PacmanGame;
import fr.univ_lorraine.pacman.controller.ghost.GhostStrategy;
import fr.univ_lorraine.pacman.controller.pacman.PacmanStrategy;

public class MenuScreen implements Screen {

	private PacmanGame game;

	private Skin skin;
	private Stage stage;

	private GhostStrategy ghostStrategy = null;
	private PacmanStrategy pacmanStrategy = null;

	public MenuScreen(PacmanGame aGame) {
		super();
		game = aGame;
	}

	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		// A skin can be loaded via JSON or defined programmatically, either is
		// fine. Using a skin is optional but strongly
		// recommended solely for the convenience of getting a texture, region,
		// etc as a drawable, tinted drawable, etc.
		skin = new Skin();

		// Generate a 1x1 white texture and store it in the skin named "white".
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));

		// Store the default libgdx font under the name "default".
		skin.add("default", new BitmapFont());

		// Configure a TextButtonStyle and name it "default". Skin resources are
		// stored by type, so this doesn't overwrite the font.
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
		textButtonStyle.font = skin.getFont("default");
		skin.add("default", textButtonStyle);
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.background = skin.newDrawable("white", Color.WHITE);
		labelStyle.font = skin.getFont("default");
		labelStyle.fontColor = Color.BLACK;
		skin.add("default", labelStyle);

		int nbGhost = GhostStrategy.values().length;
		int nbPac = PacmanStrategy.values().length;

		final Label ghostLbl = new Label("Select a ghost strategy", skin);
		ghostLbl.setPosition(Gdx.graphics.getWidth() / 3 - ghostLbl.getWidth() / 2 - 50, Gdx.graphics.getHeight() - 25);
		stage.addActor(ghostLbl);
		final Label pacmanLbl = new Label("Select a pacman strategy", skin);
		pacmanLbl.setPosition(2 * Gdx.graphics.getWidth() / 3 - pacmanLbl.getWidth() / 2 + 50,
				Gdx.graphics.getHeight() - 25);
		stage.addActor(pacmanLbl);

		int y = nbGhost * Gdx.graphics.getHeight() / (nbGhost + 1);
		int x = Gdx.graphics.getWidth() / 3;
		Actor[] ghosts = new Actor[nbGhost];
		for (final GhostStrategy strat : GhostStrategy.values()) {
			TextButton button = new TextButton(strat.toString(), skin);
			button.setPosition(x - button.getWidth() / 2, y);
			button.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					ghostStrategy = strat;
					ghostLbl.setText("Ghost strategy: " + strat);
					((TextButton) event.getListenerActor()).setChecked(false);
				}
			});
			stage.addActor(button);

			y -= Gdx.graphics.getHeight() / (nbGhost + 1);
		}

		y = nbPac * Gdx.graphics.getHeight() / (nbPac + 1);
		x += Gdx.graphics.getWidth() / 3;
		Actor[] pacmans = new Actor[PacmanStrategy.values().length];
		for (final PacmanStrategy strat : PacmanStrategy.values()) {
			TextButton button = new TextButton(strat.toString(), skin);
			button.setPosition(x - button.getWidth() / 2, y);
			button.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					pacmanStrategy = strat;
					pacmanLbl.setText("Pacman strategy: " + strat);
					((TextButton) event.getListenerActor()).setChecked(false);
				}
			});
			stage.addActor(button);

			y -= Gdx.graphics.getHeight() / (nbPac + 1);
		}

		TextButton button = new TextButton("PLAY", skin);
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (ghostStrategy != null && pacmanStrategy != null)
					game.startGame(ghostStrategy, pacmanStrategy);
				((TextButton) event.getListenerActor()).setChecked(false);
			}
		});
		button.setPosition(Gdx.graphics.getWidth() / 2, 10);
		stage.addActor(button);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, true);
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
