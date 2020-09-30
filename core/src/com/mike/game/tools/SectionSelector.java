package com.mike.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mike.game.main;
import com.mike.game.screens.GameScreen;
import com.mike.game.sprites.Man;
import java.util.Random;

public class SectionSelector {
	//player character
	private Man player;
	//level data
	private int nextLevel;
	private int currentLevel;
	private Vector2 vector;

	private Array<Float> SectionSelector(){//Creates array storing the location of each map section start point. todo adjust height to see if smooths transitions
		Array<Float> XY = new Array<Float>();
		nextLevel = new Random().nextInt(4) + 1;
		if(nextLevel == 1){
			XY.clear();
			XY.add(32*16/main.PIXELS_PER_METER, 416*32/main.PIXELS_PER_METER);
		}else if(nextLevel == 2){
			XY.clear();
			XY.add(32*16/main.PIXELS_PER_METER, 385*32/main.PIXELS_PER_METER);

		}else if(nextLevel == 3){
			XY.clear();
			XY.add(32*16/main.PIXELS_PER_METER, 347*32/main.PIXELS_PER_METER);

		}else if(nextLevel == 4){
			XY.clear();
			XY.add(32*16/main.PIXELS_PER_METER, 325*32/main.PIXELS_PER_METER);

		}
		return XY;

	}
	public Vector2 getNextLevel(Vector2 position){//picks next section of map to go to
		if (position.x > 32*85/main.PIXELS_PER_METER){
			GameScreen.isPlayerTeleporting = true;
			Array<Float>XY = SectionSelector();
			vector = new Vector2(XY.get(0), XY.get(1));
			return vector;
		}else{
			GameScreen.isPlayerTeleporting = false;
		}
		return null;
	}
}
