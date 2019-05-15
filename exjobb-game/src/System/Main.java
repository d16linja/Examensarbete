package System;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.TimeUnit;


import javax.swing.JFrame;
import javax.swing.JPanel;

import Agents.AgentObject;
import Agents.Player;
import World.WorldObject;


public class Main extends JPanel implements Runnable, KeyListener {
	private Graphics2D g2d;
    private Chunk chunk;
    private Player player;
    private int coinCount = 0;
    private double cameraPan = 0;
    private int runNr = 0;
	private long testRuntime = 30000;
	private long countdownTime = 5000;
	private long startTime, stopTime, timeLeft;
    
    // Creates all the necessary objects for the Game
    public Main() {
    	Resources.loadResources();
        chunk = new Chunk();
		startTime = System.currentTimeMillis();
		stopTime = startTime + testRuntime + countdownTime;
		timeLeft = stopTime - System.currentTimeMillis();
		player = new Player(10, 10);
		cameraPan = Chunk.getContext()*960;

		for (int i = 0; i < 3; i++) {
			chunk.generateNewChunk();
		}
        
    	// Get the players spawn point and remove the object from the list of objects
        // Also count all the coins so that we can compare it to the score later
    	for (int i = 0; i < chunk.getAgents().size(); i++) {
    		if (chunk.getAgents().get(i).getClass() == player.getClass()) {
    			player.setX(chunk.getAgents().get(i).getX());
    			player.setY(chunk.getAgents().get(i).getY());
				chunk.getAgents().remove(i);
    		}
    	}
    }
    // Creates the window, keylistener and starts a thread
    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Sample Frame");

        Main main = new Main();
        main.setPreferredSize(new Dimension(60*16,40*16));
        frame.setContentPane(main);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setFocusable(true);
        main.addKeyListener(main);
        main.requestFocusInWindow();
        Thread thread = new Thread(main);
        thread.start();
    }
    
    // Prints the graphics to the JPane
    public void paint(Graphics g) {
        super.paint(g);

		if ((player.getX() - cameraPan) > 485) {
			cameraPan += 0.01 * (player.getX() - cameraPan - 480);
		}

        g2d = (Graphics2D) g;
        g2d.setColor(new Color(150,150,255));
        g2d.fillRect(0, 0, 60*16, 60*16);

        if (Chunk.getChunks().size() > 0) {
			for (int i = 0; i < Chunk.conWorld().size(); i++) {
				g2d.drawImage(Chunk.conWorld().get(i).getImg(),(int) (Chunk.conWorld().get(i).getX()-cameraPan), Chunk.conWorld().get(i).getY(), null);
			}

			for (int i = 0; i < Chunk.conAgents().size(); i++) {
				g2d.drawImage(Chunk.conAgents().get(i).getImg(),(int) (Chunk.conAgents().get(i).getX()-cameraPan),(int) Chunk.conAgents().get(i).getY(), null);
			}

			for (int i = 0; i < player.getHealth(); i++) {
				g2d.drawImage(Resources.heartBig, 20+40*i,20,null);
			}
		}

        // Print the timer for the user

		if (timeLeft > 0) {
			String timeString = "";
			g2d.setFont(new Font("Monospaced", Font.BOLD, 40));
			if (timeLeft > testRuntime) {
				g2d.setColor(Color.RED);
				timeString = "" + TimeUnit.MILLISECONDS.toSeconds(timeLeft - testRuntime);
			} else {
				cameraPan+=0.5;
				switch (Randomizer.getState()) {
					case NORMAL:
						g2d.setColor(Color.GREEN);
						break;
					case CRYPTO:
						g2d.setColor(Color.BLUE);
						break;
				}
				int minutes = (int) (timeLeft / 1000) / 60;
				int seconds = (int) (timeLeft / 1000) % 60;
				if (seconds < 10) {
					timeString = minutes + ":0" + seconds;
				} else {
					timeString = minutes + ":" + seconds;
				}
			}
			g2d.drawString(timeString, 20, 100);
		}

//        // If we win, we show a winning screen
//        if (player.getScore() == coinCount) {
//        	g2d.setColor(Color.GREEN);
//        	g2d.setFont(new Font("Monospaced", Font.BOLD, 60));
//        	g2d.drawString("You win!", 100, 100);
//        	g2d.setFont(new Font("Monospaced", Font.BOLD, 30));
//        	g2d.drawString("Press ESC to exit", 100, 150);
//        }
//
        
        g2d.drawImage(player.getImg(),(int) (player.getX()- (int) cameraPan),(int) player.getY(), null);


        
    }

    public void restart(){
    	if (runNr >= 1) {
			System.out.println("Bye!");
    		System.exit(0);
		}
    	runNr++;
		Randomizer.changeState();
		Chunk.resetChunks();
		player = new Player(10, 10);
		cameraPan = 0;
	}

	public void respawn() {
    	player = new Player(10,10);
    	player.setX(Chunk.getContext()*960+160);
    	chunk.addRespawnToChunk(Chunk.getContext());
    	testRuntime = timeLeft;
    	stopTime += countdownTime;
    	cameraPan = Chunk.getContext()*960;
	}
    
    // Runs the game loop
	public void run() {

        while (true) {
			if (timeLeft <= 0) {
				restart();
				startTime = System.currentTimeMillis();
				testRuntime = 30000;
				stopTime = startTime + testRuntime + countdownTime;
				timeLeft = stopTime - System.currentTimeMillis();
			}

			timeLeft = stopTime - System.currentTimeMillis();

        	try {
				Thread.sleep(7);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            Chunk.setContext((int) player.getX() / 960);
            while (Chunk.getContext()+4 >= Chunk.getChunks().size()) {
				chunk.generateNewChunk();
			}

            for (WorldObject w : Chunk.conWorld()) {
            	w.update();
			}

			for (AgentObject a : Chunk.conAgents()) {
            	a.update();
            }
            player.update();
            if(player.getHealth()<=0 || player.getY() > 640 || player.getX() < cameraPan) {
 				respawn();
            }
            repaint();
        }		
	}

	// Sets the players booleans to true if the correct key is pressed
	@Override
	public void keyPressed(KeyEvent e) {

    	if (timeLeft > testRuntime) {
			// We are counting down to start the level
		} else {

			if (e.getKeyCode() == 65 || e.getKeyCode() == 37) {
				player.setLeft(true);
			}

			if (e.getKeyCode() == 68 || e.getKeyCode() == 39) {
				player.setRight(true);
			}

			if (e.getKeyCode() == 87 || e.getKeyCode() == 38) {
				player.setUp(true);
			}

			if (e.getKeyCode() == 83 || e.getKeyCode() == 40) {
				player.setDown(true);
			}

			if (e.getKeyCode() == 32 || e.getKeyCode() == 69) {
				player.setUse(true);
			}

			if (e.getKeyCode() == 27) {
				System.exit(0);
			}
		}
	}

	// Sets the players booleans to false if the key is released
	@Override
	public void keyReleased(KeyEvent e) {
		
		if (e.getKeyCode() == 65 || e.getKeyCode() == 37) {
			player.setLeft(false);
		}
		
		if (e.getKeyCode() == 68 || e.getKeyCode() == 39) {
			player.setRight(false);
		}
		
		if (e.getKeyCode() == 87 || e.getKeyCode() == 38) {
			player.setUp(false);
		}
		
		if (e.getKeyCode() == 83 || e.getKeyCode() == 40) {
			player.setDown(false);
		}

		if (e.getKeyCode() == 32 || e.getKeyCode() == 69) {
			player.setUse(false);
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}