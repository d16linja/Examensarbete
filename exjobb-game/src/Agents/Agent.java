package Agents;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import World.World;

//Since object is a reserved class, we call it Agent instead
public class Agent {

	static List <AgentObject> agentList = new ArrayList<AgentObject>();
	private FileReader fileReader;
	AgentFactory aFactory = new AgentFactory();

	
	// This class creates a static list of all objects (agents) in the game from the text file specified
	// It uses the AgentFactory to do so. 
	public Agent() {

		BufferedReader bReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/res/world.txt")));
		
		for (int y = 0; y < 60; y++) {
			for (int x = 0; x < 60; x++) {
				try {
					aFactory.getAgentObject((char)bReader.read(), x, y);
				} catch (IOException e) {
					System.err.println("Error getting world data at: " + y + ", " + x);
				}
			}
		}
	}

	public static List<AgentObject> getAgentList() {
		return agentList;
	}

	
	

}
