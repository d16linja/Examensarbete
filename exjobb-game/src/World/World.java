package World;

import Agents.Agent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class World {

	static List <WorldObject> worldBlock = new ArrayList<WorldObject>();
	private FileReader fileReader;
	private WorldFactory wFactory = new WorldFactory();
	private List <ArrayList> blockList = new ArrayList();

	// This class creates a static list of all worldobjects in the game from the text file specified
	// It uses the WorldFactory to do so. 
	public World() {
		generateBlock();

/*		BufferedReader bReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/res/world.txt")));

		for (int y = 0; y < 60; y++) {
			for (int x = 0; x < 60; x++) {
				try {
					wFactory.getWorldObject((char)bReader.read(), x, y);
				} catch (IOException e) {
					System.err.println("Error getting world data at: " + y + ", " + x);
				}
			}
		}*/
	}

	public List<ArrayList> getBlockList() {
		return blockList;
	}

	public static List<WorldObject> getBlocks() {
		return worldBlock;
	}

	public void generateBlock(){

		int blockOffset = getBlockList().size()*60;
		List <WorldObject> block = new ArrayList<WorldObject>();

		for (int i = 0; i < 60; i++){
			if (Math.random() > 0.8) {
				wFactory.getWorldObject('s', i + blockOffset, 58);
			} else {
				wFactory.getWorldObject('g', i + blockOffset, 58);
			}
		}
	}

}
