package states;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.sound.sampled.Clip;

import images.Images;
import pokemon.Pokemon;

public class Room {
	
	private GameManager gm;
	
	private boolean newBattle = false;
	public int num, p;
	
	//animating variables
	private int animating = 0;
	private int animCount = 0;
	private int x = 0;
	//array of stuff to print
	private String[][] toPrint = {{},{"...", "...The door appears to be jammed!", "Perhaps IRONMITE might be able to blast it open!", "This is his lab; try to find him.", "If you fight him enough, he'll (eventually)...", "be willing to help you out!", "(Hint: Repeatedly mouse around the room)"}
	,{"...", "...You need a passcode to open the door!", "Perhaps CAPTAIN AMERICA has it!"},{"...The door is sealed with Wakandan technology!", "Only a Wakandan can open it..."},{},{"...","The door wouldn't budge!"}};
	//private int[] printLens = {0,7,3,2,0};
	private int printNum = 0;
	
	public Room(GameManager g, int r, boolean first) {
		gm = g;
		num = r;
		p = num;
		if(!first || r == 4) {
			animating = -1;
		}
		
	}
	
	public void click(int x, int y) {
		//moves you to the next room
		if(num == 4){
			Images.clip[4].loop(Clip.LOOP_CONTINUOUSLY);
			animating = 1;
		} else if(animating == 0) {
			if (printNum < toPrint[p].length -1 ) {
				printNum ++;
				animCount = 0;
			} else if (printNum == toPrint[p].length - 1) {
				animating = -1;
				animCount = 0;
			}
			
		} else if(x > 218 && x < 290 && y > 35 && y < 88) {
			if (gm.canMoveOn) {
				Images.clip[num].stop();
				gm.rNum ++;
				gm.firstTime = true;
				gm.setState(GameManager.room);
				gm.heal();
				gm.canMoveOn = false;
			} else if (animating == -1) {
				animating = 0;
				p = 5;
				animCount = 0;
				printNum = 0;
			}
		}
	}
	public void move(int x, int y) {
		//creates a random number out of 300 every time you move
		//1 in 300 chance to start a battle
		if(animating == -1 && !gm.canMoveOn && num < 4) {
			int chance = (int)(Math.random()*300);
			if(chance == 0) {
				animating = 1;
				Images.clip[num].stop();
				if(num == 4) {
					Images.clip[4].loop(Clip.LOOP_CONTINUOUSLY);
				} else {
					Images.clip[5].loop(Clip.LOOP_CONTINUOUSLY);
				}
			}
		}
		
	}


	private int s = 2;
	
	public void blit(Graphics2D g) { //draws onto frame
		g.drawImage(Images.rooms[num], 0, 0, null);
		if (num == 4) {
			g.drawImage(Images.pokes[4], 190, 140, null);
		}
		if(animating == 0) { 
			g.drawImage(Images.textboxes[num], 0, 0, null);
			g.setFont(Images.font);
			blitString(Images.grey, s, g);
			blitString(Images.black, 0, g);
		} else if(animating == 1) { //battle start animation
			g.setColor(Color.black);
			g.fillRect(0, 0, x, 95);
			g.fillRect(512-x, 95, x, 95);
			g.fillRect(0, 190, x, 95);
			g.fillRect(512-x, 285, x, 95);
		}
		
	}
	
	private void blitString(Color c, int s, Graphics2D g) { //draws string with shadow
		g.setColor(c);
		g.drawString(toPrint[p][printNum], 40 + s, 340 + s);
	}

	public void update() {
		if(newBattle) {
			gm.nBattle = null;
			gm.setState(GameManager.battle);
			newBattle = false;
		}
		if(animating == 0) { //displays text
			animCount ++;
			if(printNum < toPrint[p].length - 1) {
				if(animCount % 100 == 0) {
					printNum ++;
					animCount = 0;
				} 
			}else if (printNum == toPrint[p].length - 1 && animCount == 101){
				animCount = 0;
				animating = -1;
			}
		} else if(animating == 1) { //animation
			animCount ++;
			if(animCount == 1 || animCount == 7) {
				x = 512;
			} else if (animCount == 4 || animCount == 10) {
				x = 0;
			} else if (animCount > 17 && animCount %2 == 0) {
				x += 32;
			} else if (animCount == 51) {
				newBattle = true;
			}
		}
	}
}
