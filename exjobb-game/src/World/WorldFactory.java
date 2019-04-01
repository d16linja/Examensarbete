package World;

public class WorldFactory {

	// If we pass a letter to this class it creates a new object in the list
	// At the position specified
	
	public WorldObject getWorldObject(char worldChar, int x, int y) {
		switch (worldChar) {
		case 'g':
			return new Grass(x, y);
		case 's':
			return new Stone(x, y);
		default:
			break;
		}
		System.err.println("Something went horribly wrong while generating the world, this is what i know: char: [" + worldChar + "] x: [" + x + "] y: [" + y + "]");
		return null;
	}
}
