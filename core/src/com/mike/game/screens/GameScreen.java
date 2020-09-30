package com.mike.game.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mike.game.listeners.CollisionListener;
import com.mike.game.main;
import com.mike.game.scenes.HUD;
import com.mike.game.sprites.Man;
import com.mike.game.tools.B2DWorldCreator;
import com.mike.game.tools.BackgroundCreator;
import com.mike.game.tools.SectionSelector;

import static com.badlogic.gdx.Gdx.input;

public class GameScreen implements Screen, InputProcessor {//todo implement difficulty
	private Viewport gameViewport;
	private Viewport backgroundViewport;
	private OrthographicCamera camera;
	private OrthographicCamera backgroundCamera;
	public static main game;
	boolean movingRightAction = false;
	boolean movingLeftAction = false;
	boolean movingRight = false;
	boolean movingLeft = false;
	public static int score;
	private int scoreX = 0;
	private HUD hud;
	private BackgroundCreator backgroundCreator;
	public static boolean isPlayerTeleporting = false;

	//player character
	private Man player;

	//section selector stuffs
	private SectionSelector selector;
	private Array<Float> XY;

	//sprite textures and animations
	private TextureAtlas atlas;

	//map variables
	private TmxMapLoader mapLoader;
	private TiledMap mapSection;
	private OrthogonalTiledMapRenderer mapRenderer;

	//box2d variables
	private World world;
	private B2DWorldCreator boxWorld;
	private Box2DDebugRenderer b2dr;

	//collision listener
	private CollisionListener listener;

	//private GameObject gameObject;

	Float last = null;
	Float diff = null;

	public GameScreen(main game){
		input.setCursorCatched(true);//disable use of mouse

		this.game = game;


		//sprite textures
		atlas = new TextureAtlas("spritesheets/player_sheets/completePlayerSpriteSheet.pack");

		//camera for background below
		backgroundCamera = new OrthographicCamera();
		backgroundCamera.setToOrtho(false,main.GAME_WORLD_WIDTH/main.PIXELS_PER_METER, main.GAME_WORLD_HEIGHT/main.PIXELS_PER_METER);
		backgroundCamera.update();

		backgroundViewport = new FitViewport(main.GAME_WORLD_WIDTH/ main.PIXELS_PER_METER, main.GAME_WORLD_HEIGHT/main.PIXELS_PER_METER, backgroundCamera);

		//camera for tiled map below
		camera = new OrthographicCamera();
		camera.setToOrtho(false,main.GAME_WORLD_WIDTH/main.PIXELS_PER_METER/5, main.GAME_WORLD_HEIGHT/main.PIXELS_PER_METER/5);
		camera.update();

		gameViewport = new FitViewport(main.GAME_WORLD_WIDTH/ main.PIXELS_PER_METER, main.GAME_WORLD_HEIGHT/main.PIXELS_PER_METER, camera);

		//map usage below
		mapLoader = new TmxMapLoader();
		mapSection = mapLoader.load("maps/loopingmaps/map.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(mapSection, 1/main.PIXELS_PER_METER);
		camera.position.set(gameViewport.getWorldWidth()/2, gameViewport.getWorldHeight()/2, 0);

		//box2d usage below
		world = new World(new Vector2(0,-10), true);// 0 0 = no gravity
		b2dr = new Box2DDebugRenderer();
		boxWorld = new B2DWorldCreator(world, mapSection);

		//create player below
		player = new Man(world, this);

		selector = new SectionSelector();

		//sets up HUD
		hud = new HUD(game.batch);

		//creates and sets up background
		backgroundCreator = new BackgroundCreator(backgroundViewport);

		//initiate listener
		listener = new CollisionListener();
		world.setContactListener(listener);

		//gameObject = new GameObject();

		score = 0;


		//InputMultiplexer im = new InputMultiplexer(gameStage, this);//combines both input processors in order of priority
		input.setInputProcessor(this);


	}
	public static Vector2 getStageLocation(Actor actor) {
		return actor.localToStageCoordinates(new Vector2(0, 0));
	}

	public TextureAtlas getAtlas(){
		return atlas;
	}


	@Override
	public void show() {

	}

	public void updateVelocities(float dt){//todo on teleport record player height above map and use to calculate height for next stage OR alternative roof to stop jumping at end of section.
		world.step(1/60f, 6, 2);
		if(last == null || isPlayerTeleporting){//set difference between last position and current position to 0 if null or isplayerteleporting
			diff = 0f;
		}else {
			diff = last - player.b2body.getPosition().x;//otherwise calculate diff from last pos and current pos
		}
		//Gdx.app.log(diff.toString(), "");
		//update movement variables
		if(player.b2body.getLinearVelocity().x <= 0){
			movingRight = false;
		}
		if(player.b2body.getLinearVelocity().x >= 0){
			movingLeft = false;
		}
		if(player.b2body.getLinearVelocity().x > 0){//method sets movingRight and keeps background movement speed constant to player movement speed
			if(last == null){
				backgroundCamera.position.set(backgroundCamera.position.x, backgroundCamera.position.y, 0);
				movingRight = true;
			}else if (last != player.b2body.getPosition().x) {
				backgroundCamera.position.set(backgroundCamera.position.x - diff, backgroundCamera.position.y, 0);
				movingRight = true;
			}else {
				movingRight = false;
			}
		}
		if(player.b2body.getLinearVelocity().x < 0){//method sets movingLeft and keeps background movement speed constant to player movement speed
			if(last == null){
				backgroundCamera.position.set(backgroundCamera.position.x, backgroundCamera.position.y, 0);
				movingLeft = true;
			}else if (last != player.b2body.getPosition().x) {
				backgroundCamera.position.set(backgroundCamera.position.x - diff, backgroundCamera.position.y, 0);
				movingLeft = true;
			}else {
				movingLeft = false;
			}
		}
		//set camera positions
		camera.position.set(player.b2body.getWorldCenter().x, player.b2body.getWorldCenter().y, 0);

		//update player velocity for less movement -- time to stop is faster with lower player velocity
		if(movingRight){
			if(!movingRightAction && player.b2body.getLinearVelocity().x < 1.5){
				player.b2body.setLinearVelocity(0, player.b2body.getLinearVelocity().y);
			}
		}
		if(movingLeft){
			if(!movingLeftAction && player.b2body.getLinearVelocity().x > -1.5){
				player.b2body.setLinearVelocity(0, player.b2body.getLinearVelocity().y);
			}
		}



		//update player velocity for additional movement
		//if (player.b2body.getLinearVelocity().x < 3f && player.b2body.getLinearVelocity().x > -3f) {//not used anymore as player cant jump if in air
			if (movingRightAction) {//methods for acceleration in both directions with set top speed
				if (player.b2body.getLinearVelocity().x > 2.5f) {
					player.b2body.applyLinearImpulse(new Vector2(0.025f, 0), player.b2body.getWorldCenter(), true);
				} else {
					player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
				}

			}
			if (movingLeftAction) {
				if (player.b2body.getLinearVelocity().x < -2.5f) {
					player.b2body.applyLinearImpulse(new Vector2(-0.025f, 0), player.b2body.getWorldCenter(), true);
				} else {
					player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
				}
			}
		//}
		//update cameras
		camera.update();
		backgroundCamera.update();
		mapRenderer.setView(camera);
		last = player.b2body.getPosition().x;//update last to current position
	}

	@Override
	public void render(float v) {

		updateVelocities(v);

		//saves last background camera position
		backgroundCreator.lastCameraPos(backgroundCamera.position.x, backgroundCamera.position.y);

		Vector2 vector = selector.getNextLevel(player.b2body.getPosition());
		player.moveMan(vector);

		Gdx.gl.glClearColor(1,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//score handler
		if (movingRight) {//score handler
			Body body2 = boxWorld.mapBody();
			if(scoreX < 0) {
				scoreX = scoreX + 1;
			}
			if(scoreX >= 0){
				score = score + 1;
			}
		}

		if (movingLeft) {//score handler
			scoreX = scoreX - 1;
		}
		if(score % 10 == 0){//score formatting
			hud.currentScore = score/10;
		}

		//update player texture/ animations
		player.update(v);

		//render background
		backgroundCreator.updateBackground(movingRight, movingLeft, backgroundCamera, isPlayerTeleporting, boxWorld.mapBody().getFixtureList().size);

		//update hud
		hud.updateHUD();
		hud.HUDstage.draw();

		//render 2d map textures
		mapRenderer.render();

		//renders hitboxes
		//b2dr.render(world, camera.combined);

		//set camera to use
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		player.draw(game.batch);
		game.batch.end();
	}
	public void restartGame(){
		dispose();
		game.changeScreen(1);//todo game over screen displaying score and saving to text file.
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
		mapSection.dispose();
		mapRenderer.dispose();
		world.dispose();
		b2dr.dispose();
		hud.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {//handle keydown inputs
		if (keycode == Input.Keys.ESCAPE){
			dispose();
			game.changeScreen(0);
		}
		if (keycode == Input.Keys.L) {
			movingLeftAction = true;
		}
		if (keycode == Input.Keys.GRAVE) {
			movingRightAction = true;
		}
		if (keycode == Input.Keys.P){
				if(player.getState() != Man.State.JUMPING && player.getState()!= Man.State.FALLING) {//apply upwards velocity to body (jump) if not currently jumping
					player.b2body.applyLinearImpulse(new Vector2(0, 5), player.b2body.getWorldCenter(), true);
				}
			}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {//handle keyup inputs
		if (keycode == Input.Keys.L)
			movingLeftAction = false;
		if (keycode == Input.Keys.GRAVE)
			movingRightAction = false;
		return false;
	}

	@Override
	public boolean keyTyped(char c) {
		return false;
	}//all unused atm

	@Override
	public boolean touchDown(int i, int i1, int i2, int i3) {
		return false;
	}

	@Override
	public boolean touchUp(int i, int i1, int i2, int i3) {
		return false;
	}

	@Override
	public boolean touchDragged(int i, int i1, int i2) {
		return false;
	}

	@Override
	public boolean mouseMoved(int i, int i1) {
		return false;
	}

	@Override
	public boolean scrolled(int i) {
		return false;
	}
}
