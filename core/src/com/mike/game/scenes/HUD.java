package com.mike.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mike.game.main;
import com.mike.game.screens.GameScreen;

public class HUD implements Disposable{
	public Stage HUDstage;
	private Viewport viewport;
	private String formattedTime;
	private String formattedMinutes;
	private String formattedSeconds;

	private int levelTimer;
	public int currentScore;
	private long startTime;
	private long elapsedTime;
	private long elapsedSeconds;
	private long secondsDisplay;
	private long elapsedMinutes;
	//private int highScore; todo if time allows
	private int speed;
	private String userName = "Divock Origi"; // todo if time allows

	Label scoreLabel;
	Label countDownLabel;
	Label timeLabel;
	Label timeWordLabel;
	Label difficultyLabel;
	Label difficultyWordLabel;
	Label speedLabel;
	Label usernameLabel;
	Label usernameWordLabel;
	Label scoreWordLabel;

	public HUD(SpriteBatch batch){
		startTime = System.currentTimeMillis();
		long elapsedTime = System.currentTimeMillis() - startTime;
		long elapsedSeconds = elapsedTime / 1000;
		long secondsDisplay = elapsedSeconds % 60;
		long elapsedMinutes = elapsedSeconds / 60;
		formattedTime = (elapsedMinutes + "0:0" + elapsedSeconds);
		currentScore = GameScreen.score;

		//create stage for hud
		viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		HUDstage = new Stage(viewport, batch);

		//table for formatting hud
		Table table = new Table();
		table.top();
		table.setFillParent(true);

		//create and format labels for hud
		scoreLabel = new Label(String.format("%08d", currentScore), new Label.LabelStyle(new BitmapFont(), Color.SALMON));
		scoreLabel.setFontScale(5);
		usernameWordLabel = new Label("NAME", new Label.LabelStyle(new BitmapFont(), Color.SALMON));
		usernameLabel = new Label("Divock", new Label.LabelStyle(new BitmapFont(), Color.SALMON));
		//countDownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.SALMON));
		scoreWordLabel = new Label("SCORE", new Label.LabelStyle(new BitmapFont(), Color.SALMON));
		scoreWordLabel.setFontScale(3);
		timeLabel = new Label(String.format("%02d", elapsedMinutes) + (":") + (String.format("%02d", elapsedSeconds)), new Label.LabelStyle(new BitmapFont(), Color.SALMON));
		timeLabel.setFontScale(5);
		timeWordLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.SALMON));
		timeWordLabel.setFontScale(3);
		difficultyWordLabel = new Label("DIFFICULTY", new Label.LabelStyle(new BitmapFont(), Color.SALMON));
		difficultyWordLabel.setFontScale(3);
		difficultyLabel = new Label("EASY", new Label.LabelStyle(new BitmapFont(), Color.SALMON));
		difficultyLabel.setFontScale(5);
		speedLabel = new Label("1", new Label.LabelStyle(new BitmapFont(), Color.SALMON));

		//add labels to table
		table.add(timeWordLabel).expandX().padTop(10);
		table.add(scoreWordLabel).expandX().padTop(10);
		table.add(difficultyWordLabel).expandX().padTop(10);
		table.row();
		table.add(timeLabel);
		table.add(scoreLabel).expandX();
		table.add(difficultyLabel).expandX();

		//add table to stage
		HUDstage.addActor(table);

	}

	public void updateHUD(){//called every frame to update the hud
		scoreLabel.setText(String.format("%08d", currentScore));
		long elapsedTime = System.currentTimeMillis() - startTime;
		long elapsedSeconds = elapsedTime / 1000;
		long secondsDisplay = elapsedSeconds % 60;
		long elapsedMinutes = elapsedSeconds / 60;
		formattedTime = String.format("%02d", elapsedMinutes) + (":") + (String.format("%02d", secondsDisplay));
		timeLabel.setText(formattedTime);
	}


	@Override
	public void dispose() {
		HUDstage.dispose();
	}
}
