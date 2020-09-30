package com.mike.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mike.game.main;

import static com.badlogic.gdx.Gdx.input;

public class MenuScreen implements Screen {
	private main game;
	Texture texture;
	private Stage stage;
	private Skin skin;
	private Table menuTable;
	private TextButton startButton;
	private TextButton quitButton;
	private Sound menuMusic;
	private Sound buttonClick;
	private ButtonGroup menuButtons;


	public MenuScreen(final main game){//not sure why final todo clean up and group code
		input.setCursorCatched(false);//allow use of mouse

		this.game = game;
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		stage = new Stage(new ScreenViewport());

		//independant pause menu  TODO
		//Window pause = new Window("PAUSE", skin);

		//fetch and play menu music
		menuMusic = Gdx.audio.newSound(Gdx.files.internal("menuDamned.wav"));
		menuMusic.loop(0.1f);

		//fetch button click sound
		buttonClick = Gdx.audio.newSound(Gdx.files.internal("buttonClick.mp3"));

		//fetch and format menu background
		Image menuBackground = new Image(new Texture(Gdx.files.internal("menuBackground.jpg")));
		menuBackground.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		//create table to format buttons on menu
		menuTable = new Table();//create then format table
		menuTable.setWidth(stage.getWidth());
		menuTable.align(Align.center|Align.top);
		menuTable.setPosition(0, Gdx.graphics.getHeight());

		//define buttons
		startButton = new TextButton("New Game", skin);
		quitButton = new TextButton("Quit Game", skin);

		startButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {//execute code if button pressed
				buttonClick.play();
				for(Actor actor : stage.getActors())
				{
					actor.remove();
				}
				menuTable.remove();
				menuMusic.stop();

				game.changeScreen(1);//change screen to game screen
			}
		});


		quitButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {//execute code if button pressed
				buttonClick.play();
				Gdx.app.exit();


			}
		});

		//more format for button table
		menuTable.padTop(30);//30 unit pad at top of table
		menuTable.add(startButton).padBottom(30);//add buttons to table with pad below
		menuTable.row();//insert row between each button addition
		menuTable.add(quitButton);

		//create button group and add buttons
		ButtonGroup menuButtons = new ButtonGroup();
		menuButtons.add(startButton, quitButton);

		//add contents of menutable and add background to stage
		stage.addActor(menuBackground);
		stage.addActor(menuTable);//add table to stage

		//set stage to handle inputs
		Gdx.input.setInputProcessor(stage);

	}

	@Override
	public void show() {

	}

	@Override
	public void render(float v) {
		Gdx.gl.glClearColor(1,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int i, int i1) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}
}
