package states;

import java.awt.*;
import java.awt.image.BufferedImage;

import images.*;
import main.Pokecopy;
import main.UserInput;
import pokemon.*;
import javax.sound.sampled.*;


//main class for handling game states
public class GameManager{
	public static int menu = 0;
	public static int battle = 1;
	public static int room = 2;
	public int currState;
	
	public Pokemon[] player;
	public Battle nBattle;
	public Room nRoom;
	public int rNum = 1;
	public Menu nMenu;
	public boolean canMoveOn = false;
	public boolean firstTime = true;
	public String name = "";
	public Pokecopy game;
	
	
	public GameManager(Pokecopy pk) { //empty constructor
		game = pk;
	}
	
	public void update() { //updates game
		if (currState == battle) {
			//System.out.println(player[0].name);
			nBattle.update();
		} else if (currState == room) {
			//System.out.println("room");
			nRoom.update();
		} else if (currState == menu) {
			nMenu.update();
		}
	}
	
	public void setState(int state) { //changes state between menu, battle, and room
		if (state == menu) {
			currState = menu;
			nMenu = new Menu(this);
		} else if (state == battle) {
			currState = battle;
			nBattle = new Battle(rNum, player, this);
		} else if (state == room) {
			if(!(rNum == 4)) {
				Images.clip[rNum].loop(Clip.LOOP_CONTINUOUSLY);
			}
			currState = room;
			nRoom = new Room(this, rNum, firstTime);
			
		}
	}
	
	public void init() { //you start with SPIDERLING
		player = new Pokemon[4];
		
		player[0] = new Pokemon(0);
		//player[1] = new Pokemon(2);
		setState(menu);
		//devTools();
	}
	
	public void blit(Graphics2D g) {
		//displays on screen
		//System.out.println(nBattle);
		if(currState == battle) {
			nBattle.blit(g);
		} else if (currState == room) {
			nRoom.blit(g);
		} else if (currState == menu) {
			nMenu.blit(g);
		}
		
	}
	
	public void typed(char c) {
		//for entering name
		if(nMenu.typeName) {
			nMenu.type(c);
		}
	}
	
	public void click(int x, int y) { //handles clicking
		if (currState == battle) {
			nBattle.click(x, y);
		} else if (currState == room) {
			nRoom.click(x, y);
		} else if (currState == menu) {
			nMenu.click(x, y);
		}
	}
	
	public void move(int x, int y) { //handles mouse movement
		if(currState == room) {
			nRoom.move(x,y);
		}
	}
	
	public void newPoke(int num) { //adds new pokemon to your party
		player[num] = new Pokemon(num);
		canMoveOn = true;
	}

	public void heal() { //heals pokemon
		for (Pokemon p : player) {
			if (!(p==null)) {
				
				p.health = p.origHealth;
				p.attack = p.origAtk;
				p.defense = p.origDef;
				p.damage = 0;
				p.resting = 0;
				p.webbed = 0;
				p.stunned = 0;

			}
		}
	}
	
	public void restart() { //sends you back to beginning
		rNum = 1;
		player = new Pokemon[4];
		player[0] = new Pokemon(0);
		setState(room);
		
	}
	public void devTools(){
		player[1] = new Pokemon(1);
		player[2] = new Pokemon(2);
		player[3] = new Pokemon(3);
		rNum = 4;
		setState(room);
		Images.clip[0].stop();
	}
	public void end() {
		game.end();
	}
	
}
