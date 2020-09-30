package com.mike.game.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mike.game.main;
import com.mike.game.objects.PlayerBodyArrayList;
import com.mike.game.screens.GameScreen;

import java.util.Arrays;
import java.util.List;

public class Man extends Sprite {
	public enum State {FALLING, JUMPING, IDLE, RUNNING, DEAD}
	private List<String> aliases = Arrays.asList("Climb_", "Dead_", "Idle_", "Jump_", "Run_");
	public State currentState;
	public State previousState;
	private int playerWidth = 25;
	private int playerHeight = 40;
	public World world;
	public Body b2body;
	private TextureRegion playerIdle;
	private Animation<TextureRegion> playerRun;
	private Animation<TextureRegion> playerJump;
	private Animation<TextureRegion> playerClimb;
	private Animation<TextureRegion> playerDead;
	private Animation<TextureRegion> playerIdleAnimation;
	private float stateTimer;
	private boolean runningRight;

	public Man(World world, GameScreen screen){//defines/ initiates man class
		super(screen.getAtlas().findRegion("Idle_"));//sets default animation to idle
		this.world = world;
		currentState = State.IDLE;
		previousState = State.IDLE;
		stateTimer = 0;
		runningRight = true;

		//adds all frames to array
		Array<TextureRegion> frames = new Array<TextureRegion>();
		for(int z = 0; z < 5; z++){
			for(int i = 0; i < 10; i++){
				frames.add(screen.getAtlas().findRegion(aliases.get(z), i*1));
			}
			if(z == 0){
				playerClimb = new Animation(0.1f, frames);
			}
			if(z == 1){
				playerDead = new Animation(0.1f, frames);
			}
			if(z == 2){
				playerIdleAnimation = new Animation(0.1f, frames);
			}
			if(z == 3){
				playerJump = new Animation(0.1f, frames);
			}
			if(z == 4){
				playerRun = new Animation(0.1f, frames);
			}
			frames.clear();
		}


		defineMan();
		playerIdle = new TextureRegion(getTexture(), 731, 27, 232, 439);
		setBounds(0, 0, (playerWidth)/main.PIXELS_PER_METER, playerHeight/main.PIXELS_PER_METER);
		setRegion(playerIdle);
	}
	public void defineMan(){//defines body of man
		BodyDef bdef = new BodyDef();
		bdef.position.set(32*17/main.PIXELS_PER_METER, 420*32/main.PIXELS_PER_METER);
		bdef.type = BodyDef.BodyType.DynamicBody;
		//b2body.setUserData(playerObject);
		b2body = world.createBody(bdef);

		FixtureDef fdef = new FixtureDef();

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(playerWidth/2/main.PIXELS_PER_METER, playerHeight/2/main.PIXELS_PER_METER);

		fdef.shape = shape;
		b2body.createFixture(fdef);

		PlayerBodyArrayList.addToArray(this.b2body);
	}

	public void update(float dt){//update position of sprite
		setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2);
		setRegion(getFrame(dt));
	}

	public TextureRegion getFrame(float dt){//set animation based off the state of sprite
		currentState = getState();

		TextureRegion region;
		switch(currentState){
			case DEAD:
				region = playerDead.getKeyFrame(stateTimer);
				break;
			case JUMPING:
				region = playerJump.getKeyFrame(stateTimer);
				break;
			case RUNNING:
				region = playerRun.getKeyFrame(stateTimer, true);
				break;
			case IDLE:
			case FALLING:
			default:
				region = playerIdleAnimation.getKeyFrame(stateTimer, true);
				break;

		}
		if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){// set direction of texture
			region.flip(true, false);
			runningRight = false;
		}
		else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
			region.flip(true, false);
			runningRight = true;
		}
		//if current state == previous state then state timer =+ dt else equal to 0
		stateTimer = currentState == previousState ? stateTimer + dt : 0;
		previousState = currentState;
		return region;
	}

	public State getState(){//get state of sprite
		if(b2body.getPosition().y < -10){
			return State.DEAD;
		}
		else if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)){
			return State.JUMPING;
		}
		else if(b2body.getLinearVelocity().y < 0){
			return State.FALLING;
		}
		else if(b2body.getLinearVelocity().x != 0){
			return State.RUNNING;
		}
		else return State.IDLE;
	}

	public void moveMan(Vector2 vector){
		if(vector !=null) {
			b2body.setTransform(vector, 0);
		}
	}
}

