package com.jiukon.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import com.jiukon.entities.Apple;
import com.jiukon.entities.Balls;
import com.jiukon.entities.Enemy;
import com.jiukon.entities.Entity;
import com.jiukon.entities.Player;
import com.jiukon.graficos.Spritesheet;
import com.jiukon.main.Game;

public class World {

	public static Tile[] tiles;
	public static int WIDTH,HEIGHT;
	public static final int TILE_SIZE = 16;
	public static Random rand = new Random();
	
	
	public World(String path){
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(),pixels, 0, map.getWidth());
			for(int xx = 0; xx < map.getWidth(); xx++){
				for(int yy = 0; yy < map.getHeight(); yy++){
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
					//Instanciar algo com 0xFF seguido do codigo hex
					if(pixelAtual == 0xFF000000){
						//Floor
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
					}else if(pixelAtual == 0xFFFFFFFF){
						//Wall
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*16,yy*16,Tile.TILE_WALL);
					}else if(pixelAtual == 0xFF0026FF) {
						//Player
						Game.player.setX(xx*16);
						Game.player.setY(yy*16);
					}else if(pixelAtual == 0xFF94fb00) {
						//Enemy 1
						int random = rand.nextInt(3);
						if(random == 0) {
						Enemy enemy = new Enemy(xx*16,yy*16,16,16,1,Entity.sprite_enemy1);
						enemy.Ghostsprite = Spritesheet.getSprite(64, 32, 16, 16) ;
						Game.entities.add(enemy);
						}else if(random == 1) {
							Enemy enemy = new Enemy(xx*16,yy*16,16,16,1,Entity.sprite_enemy2);
							enemy.Ghostsprite = Spritesheet.getSprite(80, 32, 16, 16) ;
							Game.entities.add(enemy);
						}else if(random == 2) {
							Enemy enemy = new Enemy(xx*16,yy*16,16,16,1,Entity.sprite_enemy3);
							enemy.Ghostsprite = Spritesheet.getSprite(0, 48, 16, 16) ;
							Game.entities.add(enemy);
						}else if(random == 3) {
							Enemy enemy = new Enemy(xx*16,yy*16,16,16,1,Entity.sprite_enemy4);
							enemy.Ghostsprite = Spritesheet.getSprite(16, 48, 16, 16) ;
							Game.entities.add(enemy);
						}
						
					}else if(pixelAtual == 0xFFff9d00){
						//Balls
						Balls balls = new Balls(xx*16,yy*16,16,16,0,Entity.sprite_balls);
						Game.entities.add(balls);
						Game.count ++;
					}else if(pixelAtual == 0xFFff0000) {
						//apple
						Apple apple = new Apple(xx*16,yy*16,16,16,0,Entity.sprite_apple);
						Game.entities.add(apple);
						Game.count ++;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isFree(int xnext,int ynext){
		
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		
		int x2 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		int x4 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y4 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		return !((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile) ||
				(tiles[x2 + (y2*World.WIDTH)] instanceof WallTile) ||
				(tiles[x3 + (y3*World.WIDTH)] instanceof WallTile) ||
				(tiles[x4 + (y4*World.WIDTH)] instanceof WallTile));
	}
	
	public void restartGame(){
		Game.player = new Player(0,0,16,16,0.8,Spritesheet.getSprite(32, 0,16,16));
		Game.entities.clear();
		Game.entities.add(Game.player);
		Game.count = 0;
		Game.nowcount = 0;
		Game.world = new World("/level1.png");
		return;
	}
	
	public void render(Graphics g){
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);
		
		for(int xx = xstart; xx <= xfinal; xx++) {
			for(int yy = ystart; yy <= yfinal; yy++) {
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
					continue;
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
			}
		}
	}
	
}
