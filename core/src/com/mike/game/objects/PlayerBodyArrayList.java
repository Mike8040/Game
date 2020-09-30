package com.mike.game.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.ArrayList;

public class PlayerBodyArrayList extends ArrayList {
	private static ArrayList<Body> playerArrayList = new ArrayList<Body>();
	public ArrayList getArray(){
		return playerArrayList;
	}
	public static void addToArray(Body b){
		playerArrayList.add(b);
	}
}
