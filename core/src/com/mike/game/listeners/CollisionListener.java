package com.mike.game.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.*;
import com.mike.game.objects.DamagingBodyArrayList;
import com.mike.game.objects.PlayerBodyArrayList;
import com.mike.game.screens.GameScreen;

import java.util.ArrayList;

import static com.mike.game.screens.GameScreen.game;

public class CollisionListener implements ContactListener {
	private Sound death = Gdx.audio.newSound(Gdx.files.internal("classic_hurt.mp3"));

	private GameScreen gameScreen;

	private PlayerBodyArrayList playerArray;
	private ArrayList playerArrayList;

	private DamagingBodyArrayList damagingArray;
	private ArrayList damagingArrayList;

	@Override
	public void beginContact(Contact contact) {
		playerArray = new PlayerBodyArrayList();
		playerArrayList = playerArray.getArray();

		damagingArray = new DamagingBodyArrayList();
		damagingArrayList = damagingArray.getArray();
		/*if(contact.getFixtureA().getBody().getUserData().toString().contains("Man")){
			//Gdx.app.log(contact.getFixtureA().toString(), "Man'");
		}
		if(contact.getFixtureB().getBody().getUserData().toString().contains("Sword")){
			//Gdx.app.log(contact.getFixtureB().toString(), "Sword");
		}*/
		for (int i = 0; i < playerArrayList.size(); i++) {//loop player body arraylist
			Body b = (Body) playerArrayList.get(i);
			if(b == contact.getFixtureB().getBody()){//if player collided
				Gdx.app.log("finally","");
				for (int z = 0; z < damagingArrayList.size(); z++) {//loop damaging body arraylist
					Body b2 = (Body) damagingArrayList.get(z);
					Gdx.app.log(b2.toString(),contact.getFixtureA().getBody().toString());
					if(b2 == contact.getFixtureA().getBody()){//if collided with damaging body
						death.play(0.1f);
						gameScreen = new GameScreen(game);
						gameScreen.restartGame();
					}
				}
			}

		}
		//Gdx.app.log(contact.getFixtureA().getBody().getUserData().toString(), "");
		//Gdx.app.log(contact.getFixtureB().getBody().getUserData().toString(), "");
	}

	@Override
	public void endContact(Contact contact) {

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}
}
