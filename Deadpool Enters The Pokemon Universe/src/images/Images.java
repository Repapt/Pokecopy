package images;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;

//class with all my resources
public class Images {

    public static BufferedImage[] pokes = new BufferedImage[5];
    public static BufferedImage[] rooms = new BufferedImage[5];
    public static BufferedImage[] backs = new BufferedImage[5];
    public static int[][] coords = {{},{320,80},{312,70},{300,80},{318,70}};
    public static BufferedImage[] userPokes = new BufferedImage[5];
    public static BufferedImage[] textboxes = new BufferedImage[5];
    public static BufferedImage health;
    public static BufferedImage eHealth;
    public static BufferedImage fury;
    public static Font font;
    public static Color black;
    public static Color grey;
  //  public static AudioStream music;
    public static AudioInputStream input;
    public static Clip[] clip = new Clip[6];

    public static void load() {
    	
    	try {
    		black = new Color(16,23,34);
    		grey = new Color(160,160,160);

        	fury = ImageIO.read(Images.class.getResourceAsStream("/images/resources/fury.PNG"));
    		font = Font.createFont(Font.TRUETYPE_FONT, Images.class.getResourceAsStream("/images/resources/pokefont.ttf"));
        	font = font.deriveFont(Font.PLAIN, 24);
        	health = ImageIO.read(Images.class.getResourceAsStream("/images/resources/healthbar.PNG"));
        	eHealth = ImageIO.read(Images.class.getResourceAsStream("/images/resources/healthbar2.PNG"));
    		
    		for (int i = 0; i <= 5; i++) {
    			input = AudioSystem.getAudioInputStream(Images.class.getResourceAsStream("/images/resources/music" + i + ".wav"));
            	clip[i] = AudioSystem.getClip();
            	clip[i].open(input);
    		}
	    	
	        for (int i = 0; i <= 4; i++) {
            	
            	
                pokes[i] = ImageIO.read(Images.class.getResourceAsStream("/images/resources/pokemon" + i + ".PNG"));
                userPokes[i] = ImageIO.read(Images.class.getResourceAsStream("/images/resources/back" + i + ".PNG"));
                backs[i] = ImageIO.read(Images.class.getResourceAsStream("/images/resources/battleback" + i + ".PNG"));
                rooms[i] = ImageIO.read(Images.class.getResourceAsStream("/images/resources/room" + i + ".PNG"));
                textboxes[i] = ImageIO.read(Images.class.getResourceAsStream("/images/resources/textbox" + i + ".PNG"));
	        }
    	} catch (Exception e) {
            e.printStackTrace();
        }
        
      
    }
      


}