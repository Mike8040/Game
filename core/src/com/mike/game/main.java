package com.mike.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mike.game.screens.GameScreen;
import com.mike.game.screens.MenuScreen;

public class main extends Game {//todo add difficaulty buttons after play button and do some shit with the HUD
	public SpriteBatch batch;
	public static int activeScreen = 0;//sets menu screen as landing screen
	public static final float GAME_WORLD_WIDTH = 3840; //1024;	Resolution defined here
	public static final float GAME_WORLD_HEIGHT = 2160; //768;
	public static final float PIXELS_PER_METER = 104;


	@Override
	public void create() {
		batch = new SpriteBatch();
		setScreen(new MenuScreen(this));
	}
	public void changeScreen(Integer screenNumber){//function to change screens
		activeScreen = screenNumber;
		if(screenNumber == 0){
			super.setScreen(new MenuScreen(this));
			super.render();
		}else if(screenNumber == 1){
			super.setScreen(new GameScreen(this));
			super.render();
		}

	}
	@Override
	public void render(){
		super.render();
	}
}
