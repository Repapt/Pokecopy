package main;

import java.awt.event.KeyEvent;

import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import states.*;

//main class for handling user input

public class UserInput implements MouseListener, MouseMotionListener, KeyListener{
	
	public GameManager gm;
	
	public UserInput(GameManager gm) {
		this.gm = gm;
	}
	
	

	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		System.out.println(e.getX() + " " + e.getY());
		gm.click(e.getX(),e.getY());
	}

	public void mouseReleased(MouseEvent e) {
		
	}
	
	public void mouseMoved(MouseEvent e) {
		if(gm.currState == GameManager.room) {
			//System.out.println(e.getX() + " " + e.getY());
			gm.move(e.getX(),e.getY());
		}
		
	}



	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void keyPressed(KeyEvent e) {
		
	}



	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		if(gm.currState == GameManager.menu) {
			gm.typed(e.getKeyChar());
		}
		
	}
	
}