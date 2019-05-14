 package states;

import java.awt.Color;
import java.awt.Graphics2D;

import images.Images;

public class Menu {
	
	private GameManager gm;
	
	private String[] toPrint = {"Hey! PLAYER! What's your name?: ", ", huh?","My name is Nick Fury, Director of S.H.I.E.L.D.", "I've come to you with a mission.", "The mercenary DEADPOOL has escaped custody!",
			"Your task is to help me catch him.","You'll have to fight your way to him through...", 
			"a series of rooms, using your PKMN, SPIDERLING!", 
			"In case you don't know, each PKMN has four moves.", "Each move does something a little different...", "so you'd better know what you're doing.", "Some PKMN also have passive abilities.",
			"Your SPIDERLING can occasionally dodge attacks", "due to his Spidey-Sense.", "That is all, ","...","... Why are you still standing here?", "Move your ass, ", "                  Click anywhere to continue."};
	private int printNum;
	private boolean cont = false;
	public boolean typeName = false;
	private boolean begin = true;
	private int count = 0;
	
	public Menu(GameManager g) {
		gm = g;
	}
	
	
	public void click(int x, int y) {
		if(cont) {
			Images.clip[0].stop();
			gm.setState(GameManager.room);
		} else if(!begin && printNum < toPrint.length - 1){
			printNum ++;
			count = 0;
		}
	}
	
	private int s = 2;
	public void blit(Graphics2D g) {
		g.drawImage(Images.rooms[0], 0, 0, null);
		
		
		g.drawImage(Images.textboxes[0], 0, 0, null);
		g.drawImage(Images.fury, 0, 0, null);
		
		g.setFont(Images.font);
		blitString(Images.grey, s, g);
		blitString(Images.black, 0, g);
	
	}
	
	private void blitString(Color c, int s, Graphics2D g) {
		g.setColor(c);
		g.drawString(toPrint[printNum], 40 + s, 324 + s);
		if(printNum == 0) {
			g.drawString(gm.name, 290 + s, 324 + s);
		}
	}
	
	public void type(char c) {
		if(c == '\n') {
			System.out.println("done");
			begin = false;
			count = 0;
			typeName = false;
			printNum ++;
			toPrint[1] = gm.name + toPrint[1];
			toPrint[toPrint.length-2] = toPrint[toPrint.length-2] + gm.name + "!";
			toPrint[toPrint.length-5] = toPrint[toPrint.length-5] + gm.name + ".";
		} else if (c == '') {
			if(gm.name.length() > 0) {
				gm.name = gm.name.substring(0, gm.name.length()-1);
			}
		} else {
			if(gm.name.length() < 16) {
				gm.name += c;
			}
		}
	}
	
	public void update() {
		count ++;
		if(begin) {
			if(count == 10) {
				typeName = true;
			}
		} else {
			
			if(printNum < toPrint.length - 1){
				if(count%80 == 0){
					printNum ++;
					count = 0;
					
				}
			} else{
				cont = true;
			}
		}
	}
}
