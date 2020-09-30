package com.mike.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mike.game.main;

public class BackgroundCreator {
	Group gameBackgroundGroup;
	Stage gameBackgroundStage;
	float backgroundCameraX;
	float backgroundCameraY;
	private Animation<TextureRegion> lightning;

	public BackgroundCreator(Viewport backgroundViewport) {
		gameBackgroundStage = new Stage(backgroundViewport);
		gameBackgroundGroup = new Group();
		Image gameBackground1 = new Image(new Texture(Gdx.files.internal("gameBackground.jpg")));
		Image gameBackground2 = new Image(new Texture(Gdx.files.internal("gameBackground.jpg")));
		gameBackground1.setName("b1");
		gameBackground2.setName("b2");
		gameBackground1.setSize(main.GAME_WORLD_WIDTH / main.PIXELS_PER_METER, main.GAME_WORLD_HEIGHT / main.PIXELS_PER_METER);
		gameBackground2.setSize(main.GAME_WORLD_WIDTH / main.PIXELS_PER_METER, main.GAME_WORLD_HEIGHT / main.PIXELS_PER_METER);
		gameBackgroundGroup.addActor(gameBackground1);
		gameBackgroundGroup.addActor(gameBackground2);
		gameBackground1.setPosition(-gameBackground1.getWidth() / 2, 0);
		gameBackground2.setPosition(-gameBackground1.getWidth() / 2 + gameBackground1.getWidth(), 0);
		gameBackgroundStage.addActor(gameBackgroundGroup);
	}
	public void updateBackground(boolean movingRight, boolean movingLeft, Camera backgroundCamera, boolean teleporting, float playerPos){//effect using 2 background images to give illusion of looping background
		Image bg1 = gameBackgroundGroup.findActor("b1");
		Image bg2 = gameBackgroundGroup.findActor("b2");
		if (movingRight) {
			if(!backgroundCamera.frustum.pointInFrustum(bg1.getX() + bg1.getWidth(), 0, 0)){
				bg1.setPosition(bg2.getX() + bg2.getWidth(), 0);
			}
			if(!backgroundCamera.frustum.pointInFrustum(bg2.getX() + bg2.getWidth(), 0, 0)){
				bg2.setPosition(bg1.getX() + bg1.getWidth(), 0);
			}
		}

		if (movingLeft) {
			if(!backgroundCamera.frustum.pointInFrustum(bg1.getX(), 0, 0)){
				bg1.setPosition(bg2.getX() - bg2.getWidth(), 0);
			}
			if(!backgroundCamera.frustum.pointInFrustum(bg2.getX(), 0, 0)){
				bg2.setPosition(bg1.getX() - bg1.getWidth(), 0);
			}
		}

		if(teleporting){//if teleporting do nothing atm
			Gdx.app.log(Float.toString(backgroundCameraX), Float.toString(backgroundCameraY));
			backgroundCamera.position.set(backgroundCameraX, backgroundCameraY, 0);
		}

		gameBackgroundStage.act(Gdx.graphics.getDeltaTime());
		gameBackgroundStage.draw();//draws updates to stage
	}
	public void lastCameraPos(float CameraX, float CameraY){//used to store last camera position
		backgroundCameraX = CameraX;
		backgroundCameraY = CameraY;
	}
}
