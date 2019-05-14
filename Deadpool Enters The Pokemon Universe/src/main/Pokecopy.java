package main;

//Samuel Liu
//March 23, 2018
//Culminating Assignment
//ICS3U Ms. Strelkovska

import javax.swing.*;

import javax.sound.sampled.Clip;

import images.*;
import states.*;

import java.awt.*;
import java.awt.image.BufferedImage;


public class Pokecopy extends JPanel implements Runnable{ //used threads to control frames
	
	//Frame variables
	public static final int LENGTH = 380;
	public static final int WIDTH = 512;
	public static final String TITLE = "Deadpool Enters The Pokemon Universe";
	
	private Thread thread;
	public static boolean gameLoop;
	private UserInput input;
	
	private final int FPS = 30;
	private final int TARGET_TIME = 1000 / FPS;
	private GameManager gm;
	
	private BufferedImage image;
	private Graphics2D g;	
	private static Pokecopy game = new Pokecopy();
	private static JFrame frame = new JFrame(TITLE);
		
	public static void main(String[] args){
			
	
		
		
		frame.add(game);
		
		//had an issue with the jframe not actually having the specified dimensions
		//frame.setPreferredSize(new Dimension(WIDTH,LENGTH));
		//frame.setMinimumSize(new Dimension(WIDTH, LENGTH));
		frame.pack();
		Insets ins = frame.getInsets();
		frame.setMinimumSize(new Dimension(WIDTH + ins.right,LENGTH+ins.top));
		frame.pack();
		
		//System.out.println(frame.getContentPane().getSize());
		//System.out.println(frame.getSize());
		//frame.setMinimumSize(new frame.getInsets().top + frame.getInsets().bottom);
		
		
		
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setFocusable(true);
		
		
		game.start();
		
		
	}
	
	public Pokecopy() {
	}
	
	public void start() {
		if(gameLoop) {
			return;
		}
		Images.load();
		Images.clip[0].loop(Clip.LOOP_CONTINUOUSLY);
		gameLoop = true;
		thread = new Thread(this);
		thread.start();
		System.out.println("Working");
	}
	
	public void blit() {

		gm.blit(g);
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, WIDTH,LENGTH, null);
		g2.dispose();
		
		
	}
	public void end() {
		gameLoop = false;
		System.exit(0);
	}
	
	

	public void run() {
		
		gm = new GameManager(this);
		gm.setState(0);
		image = new BufferedImage(WIDTH,LENGTH, 1);
		g = (Graphics2D) image.getGraphics();
		gm.init();
		input = new UserInput(gm);
		addMouseListener(input);
		addMouseMotionListener(input);
		requestFocusInWindow();
		addKeyListener(input);

		
		long start, wait, elapsed;
		while(gameLoop) {
			
			
			start = System.nanoTime();
			
			gm.update();	
			blit();
			
			elapsed = System.nanoTime() - start;
			
			wait = TARGET_TIME - elapsed / 1000000;
			
			if(wait < 0) {
				wait = TARGET_TIME;
			}
			
			try {
				Thread.sleep(wait); //found this online
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
		
}