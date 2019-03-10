package World;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class World {

	static List <WorldObject> worldList = new ArrayList<WorldObject>();
	private FileReader fileReader;
	private WorldFactory wFactory = new WorldFactory();

	
	// This class creates a static list of all worldobjects in the game from the text file specified
	// It uses the WorldFactory to do so. 
	public World() {

		BufferedReader bReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/res/world.txt")));
		
		for (int y = 0; y < 60; y++) {
			for (int x = 0; x < 60; x++) {
				try {
					wFactory.getWorldObject((char)bReader.read(), x, y);
				} catch (IOException e) {
					System.err.println("Error getting world data at: " + y + ", " + x);
				}
			}
		}
	}

	public static List<WorldObject> getWorldList() {
		return worldList;
	}



}
