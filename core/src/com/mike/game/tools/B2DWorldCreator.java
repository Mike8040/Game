package com.mike.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mike.game.main;
import com.mike.game.objects.DamagingBodyArrayList;

public class B2DWorldCreator extends Sprite {
	BodyDef bdef = new BodyDef();
	PolygonShape shape = new PolygonShape();
	FixtureDef fdef = new FixtureDef();
	Body body;
	public B2DWorldCreator(World world, TiledMap map){
		for(int i = 1; i < 6; i++) {// only one layer in new map; increase second number for each addition layer in map

			for (MapObject object : map.getLayers().get(i).getObjects()) {
				if(object instanceof RectangleMapObject){
					Rectangle rect = ((RectangleMapObject) object).getRectangle();

					bdef.type = BodyDef.BodyType.KinematicBody;
					bdef.position.set((rect.getX() + rect.getWidth() / 2)/ main.PIXELS_PER_METER,
							(rect.getY() + rect.getHeight() / 2)/main.PIXELS_PER_METER);
					body = world.createBody(bdef);

					shape.setAsBox((rect.getWidth() / 2)/main.PIXELS_PER_METER, (rect.getHeight() / 2)/main.PIXELS_PER_METER);
					fdef.shape = shape;
					body.createFixture(fdef);
					if(i == 1) {
						DamagingBodyArrayList.addToArray(this.body);
					}

				}
				if(object instanceof PolygonMapObject){//****doesnt work/ no longer needed - WILL ALWAYS RETURN FALSE
					Polygon poly = ((PolygonMapObject) object).getPolygon();
					/*Array<Vector2> verts = new Array<Vector2>();
					float[] vertices = poly.getTransformedVertices();
					for (int x = 0; x < vertices.length; x++) {
						// fill tmp with the vertex
						float vertX = vertices[i * 2];
						float vertY = vertices[i * 2 + 1];
						verts.add(new Vector2(vertX, vertY));
						bdef.type = BodyDef.BodyType.StaticBody;
						bdef.position.set(poly.getX() + poly.getBoundingRectangle().getWidth()/2, poly.getY() + poly.getBoundingRectangle().getHeight()/2);
						body = world.createBody(bdef);
						shape.set(verts);*/

					bdef.type = BodyDef.BodyType.KinematicBody;
					bdef.position.set(poly.getX() + poly.getBoundingRectangle().getWidth() / 2, poly.getY() + poly.getBoundingRectangle().getHeight() / 2);
					body = world.createBody(bdef);
					Vector2 center = new Vector2();
					poly.getBoundingRectangle().getCenter(center);

					shape.setAsBox(poly.getBoundingRectangle().getWidth() / 2, poly.getBoundingRectangle().getHeight() / 2,
							center, 45);
					fdef.shape = shape;
					body.createFixture(fdef);



				}

			}
		}
	}public Body mapBody(){//return body for use with collisions
		return body;
	}
}
