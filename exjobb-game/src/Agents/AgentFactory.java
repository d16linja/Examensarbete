package Agents;

public class AgentFactory {
	
	// If we pass a block letter to this class it creates a new object in the list
	// At the position specified
	public void getAgentObject(char agentChar, int x, int y) {
		switch (agentChar) {
		case 'P':
			System.out.println("Made a player!");
			Agent.agentList.add(new Player(x, y));
			break;
		case 'C':
			System.out.println("Made a coin!");
			Agent.agentList.add(new Coin(x, y));
			break;
		case 'R':
			System.out.println("Made a cRawler!");
			Agent.agentList.add(new Crawler(x, y));
			break;
		default:
			break;
		}
	}
}
