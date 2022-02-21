package com.jiukon.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.jiukon.main.Game;
import com.jiukon.world.AStar;
import com.jiukon.world.Camera;
import com.jiukon.world.Vector2i;



public class Enemy extends Entity{
	
	public boolean GhostMode = false;
	public int GhostFrames = 0;
	public int NextTime = Entity.rand.nextInt(60*5 - 60*3) + 60*3;
	public BufferedImage Ghostsprite;
	
	public Enemy(int x, int y, int width, int height,int speed, BufferedImage sprite) {
		super(x, y, width, height,speed,sprite);
	}

	public void tick(){
		depth = 0;
		if(GhostMode == false) {
		if(path == null || path.size() == 0) {
				Vector2i start = new Vector2i(((int)(x/16)),((int)(y/16)));
				Vector2i end = new Vector2i(((int)(Game.player.x/16)),((int)(Game.player.y/16)));
				path = AStar.findPath(Game.world, start, end);
			   }
		
			if(new Random().nextInt(100) < 50)
				followPath(path);
			
			if(x % 16 == 0 && y % 16 == 0) {
				if(new Random().nextInt(100) < 10) {
					Vector2i start = new Vector2i(((int)(x/16)),((int)(y/16)));
					Vector2i end = new Vector2i(((int)(Game.player.x/16)),((int)(Game.player.y/16)));
					path = AStar.findPath(Game.world, start, end);
				   }
			     }
		}
		
			GhostFrames ++;
			if(GhostFrames == NextTime) {
				NextTime = Entity.rand.nextInt(60*6 - 60*3) + 60*2;
				GhostFrames = 0;
				if(GhostMode == false) {
					GhostMode = true;	
				}else {
					GhostMode = false;
				}
			}
		
	}
	

	
	public void render(Graphics g) {
		if(GhostMode == false) {
		super.render(g);
		}else {
			g.drawImage(Ghostsprite,this.getX() - Camera.x,this.getY() - Camera.y,null);
		}
	}
	
	
}
