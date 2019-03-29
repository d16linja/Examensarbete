package Agents;

public class AgentFactory {
	
	// If we pass a block letter to this class it creates a new object in the list
	// At the position specified
	public AgentObject getAgentObject(char agentChar, int x, int y) {
		switch (agentChar) {
		case 'P':
			return new Player(x, y);
		case 'C':
			return new Coin(x, y);
		case 'R':
			return new Crawler(x, y);
		default:
			break;
		}
		System.err.println("Something went horribly wrong while generating the agents, this is what i know: char: [" + agentChar + "] x: [" + x + "] y: [" + y + "]");
		return null;
	}
}
