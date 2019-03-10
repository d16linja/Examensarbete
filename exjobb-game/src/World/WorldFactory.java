package World;

public class WorldFactory {

	// If we pass a letter to this class it creates a new object in the list
	// At the position specified
	
	public void getWorldObject(char worldChar, int x, int y) {
		switch (worldChar) {
		case 'g':
			World.worldList.add(new Grass(x, y));
			break;
		case 's':
			World.worldList.add(new Stone(x, y));
		default:
			break;
		}
	}

}
