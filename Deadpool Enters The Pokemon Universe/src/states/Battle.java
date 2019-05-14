package states;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Arrays;

import images.*;
import pokemon.*;

public class Battle{
	
	//Battle variables
	public Pokemon enemy;
	public int enemyNum;
	public Pokemon[] player;
	public int currPoke = 0;
	//0 - atk, 1-acc
	private double[][] moveData = {{80,1},{120,1},{60,1},{70,1},{0,0.70},
			{80,1},{100,0.85},{0,1},{},{60,1},{120,1},{110,0.85},{60,1},{0,1},{60,0.8},{0,0}, {80,1}, {0,1}, {80,1}};
	private int turnNum;
	private int printSpeed = 50;
	private boolean moveOn = false;
	private boolean god = false;
	private boolean damaged = false;
	
	private String getName(int num) { //get actual name of move from move id
		String name = "";
		if(num == 0) {
			name = "Anti-Metal Claw";
		} else if (num == 1) {
			name = "Hyper Beam";
		} else if (num == 2) {
			name = "Quick Attack";
		} else if (num == 3) {
			name = "Thunderbolt";
		} else if (num == 4) {
			name = "String Shot";
		} else if (num == 5) {
			name = "Repulsor Blast";
		} else if (num == 6) {
			name = "Shield Throw";
		} else if (num == 7) {
			name = "Iron Defense";
		} else if (num == 8) {
			name = "Flash Cannon";
		} else if (num == 9) {
			name = "Taser Web";
		} else if (num == 10) {
			name = "High Jump Kick";
		} else if (num == 11) {
			name = "Energy Daggers";
		} else if (num == 12) {
			name = "Body Slam";
		} else if (num == 13) {
			name = "Recover";
		} else if (num == 14) {
			name = "Shield Bash";
		} else if (num == 15) {
			name = "SNAP";
		} else if (num == 16) {
			name = "Spinny Sword Attack";
		} else if (num == 17){
			name = "Plot Armor";
		} else if (num == 18){
			name = "Armour Piercing Rounds";
		}
		return name;
		
	}
	//Managing variables
	private boolean playing = true;
	private int counter = 0;
	private boolean lost;
	private GameManager gm;
	
	//Animating variables
	private int animX = 0;
	private int animY = 0;
	private int animX2 = 0;
	private int animY2 = 0;
	private int animating = 0;
	private String[] toPrint = {"","","",""};
	private int printNum = 0;
	private int printCount = 1;
	private int nextMove = 0;
	private boolean didTurn = false;
	private int currPrint = 0;
	

	//creates new battle object
	public Battle(int e, Pokemon[] p, GameManager g) {
		turnNum = 0;
		gm = g;
		enemy = new Pokemon(e);
		enemyNum = e;
		player = Arrays.copyOf(p, p.length);
		for(Pokemon i : player) {
			if(i == null) {
				continue;
			} else if (i.health < 1) {
				currPoke ++;
			}
		}

	}
	
	//updates
	public void update() {
		
		counter ++;
		
		if (!playing) {
			if(gm.rNum == 4) { //stops music
				Images.clip[4].stop();
				Images.clip[4].setMicrosecondPosition(0);
			} else {
				Images.clip[5].stop();
				Images.clip[5].setMicrosecondPosition(0);
			}
			gm.firstTime = false;
			if(lost) {
				gm.restart(); //sends you back to beginning
			} else {
				gm.setState(GameManager.room); //sends you back to the room you were in
				heal(); 
			}
		} else {
		
			if (animating == 0) { //idle bouncing
				if(counter == 10) {
					animY += 5;
					animY2 -= 5;
				} else if (counter == 20) {
					animY = 0;
					animY2 = 0;
					counter = 0;
				}
			} else{
				animate(counter, animating); //animates. animating variable controls what's being animated
			} 
	
		}
		
		
	}
	
	//handles click
	public void click(int x, int y) {
		boolean validClick = false;
		int moveNum = -1;
		if (x <= 259 && y > 300 && y <= 335) {
			moveNum = 0;
			validClick = true;
		} else if (x > 259 && y > 300 && y <= 335){
			moveNum = 1;
			validClick = true;
		} else if(x <= 259 && y > 335) {
			moveNum = 2;
			validClick = true;
		} else if (x > 259 && y > 335){
			moveNum = 3;
			validClick = true;
		}
		//checks if click is on a move
		if(validClick && playing && animating == 0) {
			int enemyMove = (int)(Math.random()*4);
			//gets move id
			int pMove = getMove(moveNum, player[currPoke]);
			int eMove = getMove(enemyMove, enemy);
			turnNum ++;
			
			animY = 0;
			animY2 = 0;
			if (eMove == 2 || eMove == 17) { //if enemy uses quick attack//plot armour
				move(eMove, enemy, player[currPoke],1);
				nextMove = pMove;
			} else { //otherwise player always goes first
				move(pMove, player[currPoke], enemy,2);
				nextMove = eMove;
			}
			
		}
		
		if ((animating == 1 || animating == 2) && counter > 10) {
			if(printCount/printSpeed == printNum + 1) {
				moveOn = true;
			} else {
				printNum ++;
				currPrint = printSpeed * (printNum+1);
			}
			
		}
	}
	
	private int getMove(int m, Pokemon user) { //gets move id from pokemon's set of 4 moves
		
		int moveNum;
		if (user.resting % 2 == 1) {
			user.resting ++;
			moveNum = -1; //if resting, no move is used
		} else {
			
			if (m == 0) {
				moveNum = user.move1;
				
			} else if(m == 1) {
				moveNum = user.move2;
				
				
			} else if(m == 2) {
				moveNum = user.move3;
				
			} else {
				moveNum = user.move4;
			}
			
		}
		
		return moveNum;
	}
	
	
	public void move(int m, Pokemon user, Pokemon target, int check) { //damage calculation
		toPrint[0] = user.name + " "; //for drawing strings
		printCount = 1;
		printNum = 0;
		damaged = false;
		
		double acc = Math.random(); //checks if move lands successfully
		
		//handles special cases
		if (turnNum == 5 && user.name.equals("IRONMITE")) { //IRONMITE's special ability
			toPrint[0] = "IRONMITE successfully scanned its opponent!";
			toPrint[1] = "... Countermeasures ready!";
			toPrint[2] = "IRONMITE's attack sharply rose!";
			printCount = 3;
			user.attack += (int)(user.attack*0.34);
			
		} else if (m == -1 || user.resting%2 == 1) {
			toPrint[0] += "is resting!";
			toPrint[1] = toPrint[0];
			toPrint[2] = toPrint[0];

		} else if(!(user.webbed%3 == 0)) {
			toPrint[0] += "is caught in a web!";
			toPrint[1] = toPrint[0];
			toPrint[2] = toPrint[0];
			user.webbed ++;
		} else if(!(user.stunned % 2 == 0)) {
			toPrint[0] += "is stunned!";
			toPrint[1] = toPrint[0];
			toPrint[2] = toPrint[0];
			user.stunned ++;
	    } else if (m == 15){
			toPrint[0] = "Hey " + gm.name + "! My pal cable taught me a new trick!";
			toPrint[1] = "*SNAP*";
			printCount = 3;
			if(target.name.equals("SPIDERLING") || target.name.equals("BLACK PANTHER")) {
				toPrint[2] = target.name + " doesn't feel so good!";
				target.health = 0;
			} else {
				toPrint[2] = target.name + " seems unaffected!";
			}
		}else {
			toPrint[0] += "used " + getName(m) + "!";
			toPrint[1] = toPrint[0];
			toPrint[2] = toPrint[0];
			
			if (acc < moveData[m][1]) { //checks if hit lands
				
				if(god && !(m == 13 || m == 7)){
					toPrint[1] = "DEADPOOL is unaffected!";
					target.damage = 0;
					target.webbed = 0;
					target.stunned = 0;
				} else {
					target.damage += (int)(user.attack*moveData[m][0]);
					System.out.println(target.damage);
					
					if (m == 0 || m == 18) {
						
						if (target.name.equals("IRONMITE") || target.name.equals("CAPTAIN AMERICA")){
							target.damage *= (int)(target.defense/150 + 1);
							toPrint[1] = "It's super effective!";
						} 
						
						
						
					} else if (m==1) {
						user.resting = 1;
					} else if(m == 4) {
						toPrint[1] = target.name + " is caught in a web!";
						target.webbed = 1;
						target.damage += 1;
					} else if(m == 7) {
						user.defense += (int)(user.defense*0.25);
						toPrint[1] = user.name + "'s defense sharply rose!";
						if (user.defense > user.origDef*3) {
							user.defense = user.origDef;
						}
					} else if (m==9) {
						if(target.name.equals("IRONMITE")) {
							target.damage *= 2;
							toPrint[1] = "It's super effective!";
						}
					} else if(m == 10) {
						user.health -= (int)(user.attack*0.1);
						toPrint[1] = "took damage as recoil!";
					} else if (m == 12) {
						if(target.name.equals("IRONMITE")) {
							target.damage /= 1.5;
							toPrint[1] = "It's not very effective...";
						}
					} else if (m == 13) {
						user.health += (int)(user.origHealth*0.34);
						toPrint[1] = user.name + " regained health!";
						if(user.health > user.origHealth) {
							user.health = user.origHealth;
						}
					} else if (m == 14) {
						target.stunned = 1;
						toPrint[1] = target.name + " was stunned!";
					} else if (m == 17){
						toPrint[1] = user.name + " can't be hurt!";
						god = true;
					}
				}
				
			} else {
				toPrint[1] = "...but it missed!";
			}
			
			acc = Math.random();
			if (target.name.equals("SPIDERLING")) {
				if (acc < 0.2 && (target.damage > 0 || m == 14)) {
					toPrint[1] = "SPIDERLING's spidey senses are tingling!";
					toPrint[2] = "SPIDERLING dodged the attack!";
					target.damage = 0;
					target.stunned = 0;
					printCount = 3;
				}
			} else if (target.name.equals("CAPTAIN AMERICA")) {
				if(acc < 0.2 && target.damage > 0 && !(m == 0)) {
					toPrint[1] = "CAPTAIN AMERICA blocked it!";
					target.damage = 0;
					target.webbed = 0;
					printCount = 2;
				}
			} else if (target.name.equals("IRONMITE") && turnNum >= 6 && acc < 0.34 && target.damage > 0) {
				toPrint[1] = "IRONMITE analyzed " + user.name + "'s fight pattern!";
				toPrint[2] = "IRONMITE countered the move!";
				printCount = 3;
				target.damage = 0;
				target.webbed = 0;
				target.stunned = 0;
			} 
			
			
			if(toPrint[0].equals(toPrint[1])) {
				printCount = 1;
			} else if (printCount == 1) {
				printCount = 2;
			}

			  	
			
			target.health -= target.damage/(target.defense);
			if(target.health < 0) {
				target.health = 0;
			}
		}
		
		//for animating
		if (target.damage > 1) {
			damaged = true;
		}
		printCount *= printSpeed;
		currPrint = printSpeed;
		counter = 0;
		animating = check;
	}
	
	//private Color white = new Color(248,248,248);
	//private Color grey = new Color(40,48,48);
	//private Color black = new Color(80,80,88);
	private Color green = new Color(0,158,1);
	private Color red = new Color(223,66,46);
	private int s = 2;
	
	public void blit(Graphics2D g) {	//draws onto frame	
		
		//move box
		

		g.drawImage(Images.backs[enemyNum], 0, 0, null);
		
		if(!(animating == 5)) {
			g.drawImage(Images.pokes[enemyNum], Images.coords[enemyNum][0] + animX2, Images.coords[enemyNum][1] + animY2, null);

		}
		
		if(animating == 7) {
			//nothing
		} else {
			
			g.drawImage(Images.userPokes[player[currPoke].id], 110 + animX, 192 + animY, null);

			
		}
		g.drawImage(Images.health, 256,194,null);

		
		g.setColor(green);
		//g.drawRect(400, 243, 98, 5);
		g.fillRect(400, 243, (96*(player[currPoke].health)/(player[currPoke].origHealth)), 6);
	
		
		g.drawImage(Images.eHealth, 0, 50, null);
		g.setColor(red);
		//g.drawRect(100, 100, 100, 20);
		g.fillRect(100,104,(96*(enemy.health)/(enemy.origHealth)),6);
		
		g.drawImage(Images.textboxes[enemyNum], 0, 0, null);
		
		//draws strings with shadow effect
		g.setFont(Images.font);
		blitString(Images.grey, s, g);
		blitString(Images.black, 0, g);
		
	}
	
	private void blitString(Color color, int s, Graphics2D g) {
		
		g.setColor(color);
		if(playing) {
			g.drawString(enemy.name, 42 + s, 91 + s);
			
			g.drawString(player[currPoke].name, 348 + s, 230 + s);
			g.drawString(player[currPoke].health + " / " + player[currPoke].origHealth, 404 + s, 270 + s);
		}
		if (currPoke < 4) {
			if(animating == 0 && !(player[currPoke] == null)) {
		
				g.drawString(getName(player[currPoke].move1), 40 + s, 330 + s);
				g.drawString(getName(player[currPoke].move2), 270 + s, 330 + s);
				g.drawString(getName(player[currPoke].move3), 40 + s, 360 + s);
				g.drawString(getName(player[currPoke].move4), 270 + s, 360 + s);
	
			} else {
				g.drawString(toPrint[printNum], 40 + s, 340 + s);
			}
		}
	}
	
	private void animate(int count, int check) {

		
		if(check == 2) { //user move
			if (count == 7 && damaged) {
				animX2 += 10;
			} else if (count == 14 && damaged) {
				animX2 -= 10;
			} else if (count == printCount || moveOn) {
				moveOn = false;
				counter = 0;
				if(!checkStatus()) {
					if (!didTurn) {
						didTurn = true;
						move(nextMove, enemy, player[currPoke], 1);
					} else {
						if(enemy.name.equals("DEADPOOL")) {
							if(enemy.health < enemy.origHealth) {
								animating = 6;
								printNum = 0;
								toPrint[0] = "DEADPOOL's Healing Factor activated!";
								toPrint[1] = "DEADPOOL regained health!";
							} else {
								animating = 0;
								didTurn = false;
								reset();
							}
						} else {
							animating = 0;
							didTurn = false;
							reset();
						}
						
					}
				}
								
			} else if (count == currPrint) {
				currPrint += printSpeed;
				printNum ++;
			}
		} else if (check == 1){ //enemy move
			if (count == 7 && damaged) {
				animX -= 10;
			} else if (count == 14 && damaged) {
				animX += 10;
			} else if (count == printCount || moveOn) {
				moveOn = false;
				counter = 0;
				if(!checkStatus()) {
					if (!didTurn) {
						move(nextMove, player[currPoke],enemy, 2);
						didTurn = true;
					} else {
						if(enemy.name.equals("DEADPOOL")) {
							if(enemy.health < enemy.origHealth) {
								animating = 6;
								printNum = 0;
								toPrint[0] = "DEADPOOL's Healing Factor activated!";
								toPrint[1] = "DEADPOOL regained health!";
							} else {
								animating = 0;
							}
						} else {
							animating = 0;
						}
						didTurn = false;
						reset();
					}
				}
			} else if (count == currPrint) {
				currPrint += printSpeed;
				printNum ++;
			}
		} else if (check == 3) { //user pokemon faints
			if (count == 10 || count == 20) {
				animY += 5;
			} else if (count == 30) {
				didTurn = false;
				counter = 0;
				
				turnNum = 0;
				if(currPoke+1 > 3) {
					animating = 7;
				} else if(player[currPoke+1] == null) {
					animating = 7;
				} else {
					animating = 0;
					currPoke ++;
				}
				animY -= 10;
				
			}
		} else if (check == 4) { //enemy pokemon faints
			if(count == 10 || count == 20) {
				animY2 += 5;
			} else if (count == 30) {
				if(enemyNum == 4) {
					System.out.println("wow you win");
					gm.end();
				} else if(Math.random() > 0.5 && player[enemyNum] == null) {
					animating = 5;
				} else {
					playing = false;
				}
			}
		} else if (check == 5) { //enemy pokemon joins your team
			toPrint[0] = enemy.name + " wants to help you out!";
			toPrint[1] = enemy.name + " joined your team!";
			gm.newPoke(enemyNum);
			if(count == (printSpeed*2)) {
				printNum ++;
			} else if (count == (printSpeed*3)) {
				playing = false;
			}
		} else if (check == 6) { //special case for DEADPOOL
			
			if(count == printSpeed) {
				printNum ++;
				enemy.health += enemy.origHealth*0.25;
				if(enemy.health > enemy.origHealth) {
					enemy.health = enemy.origHealth;
				}
			} else if (count == (printSpeed*2) - 1) {
				counter = 0;
				animating = 0;
				reset();

				didTurn = false;
			}
		} else if (check == 7) {
			toPrint[0] = "...";
			toPrint[1] = "... " + gm.name + " has run out of PKMN!";
			toPrint[2] = gm.name + " fainted!";
			if(count % printSpeed == 0) {
				printNum ++;
			} else if (count == printSpeed*3 - 1) {
				playing = false;
				lost = true;
			}
		}
	}
	
	
	private boolean checkStatus() { //checks if health is still over 0
		boolean died = false;
		if (player[currPoke].health <= 0) {
			toPrint[0] = player[currPoke].name + " fainted!";
			animating = 3;
			printNum = 0;
			died = true;
		} else if (enemy.health <= 0) {
			toPrint[0] = enemy.name + " fainted!";
			animating = 4;
			printNum = 0;
			died = true;
		}
		return died;
	}
	
	
	private void reset() { //resets damage taken, etc
		
		System.out.println("player " + player[currPoke].health);
		System.out.println("opponent " + enemy.health);

		player[currPoke].protect = false;
		player[currPoke].damage = 0;
		enemy.protect = false;
		enemy.damage = 0;
		god = false;
		
		
	}
	
	private void heal() { //heals pokemon at the end of the fight
		for(Pokemon p : player) {
			if (!(p==null) && p.health > 0) {
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
}
