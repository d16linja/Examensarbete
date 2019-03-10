package System;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

// A static class that holds all the resources used in the game

public class Resources {
	static public BufferedImage player, heartBig, heartSmall, crawler, coin;

	static void loadResources() {
		try {
			player = ImageIO.read(ClassLoader.getSystemResource("res/player.png"));
			heartBig = ImageIO.read(ClassLoader.getSystemResource("res/heartBig.png"));
			heartSmall = ImageIO.read(ClassLoader.getSystemResource("res/heartSmall.png"));
			crawler = ImageIO.read(ClassLoader.getSystemResource("res/crawler.png"));
			coin = ImageIO.read(ClassLoader.getSystemResource("res/coin.png"));
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

}
