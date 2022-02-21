package com.jiukon.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.jiukon.graficos.Spritesheet;
import com.jiukon.main.Game;
import com.jiukon.world.Camera;
import com.jiukon.world.World;

public class Player extends Entity{
	
	public boolean right,up,left,down;
	public int dir = 1;
	
	public static BufferedImage sprite_right ;
	public BufferedImage sprite_left ;
	public BufferedImage sprite_up ;
	public BufferedImage sprite_down;
	
	public Player(int x, int y, int width, int height,double speed,BufferedImage sprite) {
		super(x, y, width, height,speed,sprite);
		sprite_right = Spritesheet.getSprite(32, 0,16,16);
		sprite_left = Spritesheet.getSprite(48, 0, 16, 16);
		sprite_up = Spritesheet.getSprite(64, 0, 16, 16);
		sprite_down = Spritesheet.getSprite(80, 0, 16, 16);
	}
	
	public void tick(){
		depth = 1;
		
		if(right && World.isFree((int)(x+speed),this.getY())) {
			x+=speed;
			dir = 1;
		}else if(left && World.isFree((int)(x-speed),this.getY())) {
			x-=speed;
			dir = -1;
		}else if(up && World.isFree(this.getX(),(int)(y-speed))){
			y-=speed;
			dir = 2;
		}else if(down && World.isFree(this.getX(),(int)(y+speed))){
			y+=speed;
			dir = -2;
		}
		//functions
		TakeBalls();
		TakeApple();
		EnemyColide();
		
	}

	public void TakeBalls() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity current = Game.entities.get(i);
			if(current instanceof Balls) {
				if(Entity.isColidding(this, current)) {
					Game.entities.remove(i);
					Game.nowcount ++;
					return;
				}
			}
		}
	}
	
	public void TakeApple() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity current = Game.entities.get(i);
			if(current instanceof Apple) {
				if(Entity.isColidding(this, current)) {
					Game.entities.remove(i);
					Game.nowcount ++;
					return;
				}
			}
		}
	}
	
	public void EnemyColide() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity current = Game.entities.get(i);
			if(current instanceof Enemy) {
				if(Entity.isColidding(this, current)) {
					Game.world.restartGame();
					return;
				}
			}
		}
	}
	
	public void render(Graphics g) {
		if(dir == 1) {
			super.render(g);
		}else if(dir == -1) {
			g.drawImage(sprite_left,this.getX() - Camera.x,this.getY() - Camera.y,null);
		}else if(dir == 2) {
			g.drawImage(sprite_up,this.getX() - Camera.x,this.getY() - Camera.y,null);
		}else if(dir == -2){
			g.drawImage(sprite_down,this.getX() - Camera.x,this.getY() - Camera.y,null);
		}
	}


}
