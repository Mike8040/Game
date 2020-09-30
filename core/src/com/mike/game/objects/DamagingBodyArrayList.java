package com.mike.game.objects;

import com.badlogic.gdx.physics.box2d.Body;

import java.util.ArrayList;

public class DamagingBodyArrayList extends ArrayList {
	private static ArrayList<Body> damagingArrayList = new ArrayList<Body>();
	public ArrayList getArray(){
		return damagingArrayList;
	}
	public static void addToArray(Body b){
		damagingArrayList.add(b);
	}
}
