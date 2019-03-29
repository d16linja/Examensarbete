package System;

import Agents.AgentFactory;
import Agents.AgentObject;
import Agents.Player;
import World.Grass;
import World.WorldFactory;
import World.WorldObject;

import java.util.ArrayList;
import java.util.List;

public class Block {
    private static List<Block> blockList = new ArrayList<Block>();
    static int gameBlockPosition;
    private List<WorldObject> worldList = new ArrayList<WorldObject>();
    private List<AgentObject> agentList = new ArrayList<AgentObject>();

    public Block() {
        generateBlock();
    }


    private void generateBlock() {
        worldList = generateWorld();
        agentList = generateAgents(worldList);
        agentList.add(new Player(30, 30));
        blockList.add(this);
    }

    private List<WorldObject> generateWorld() {
        List<WorldObject> list = new ArrayList<WorldObject>();
        WorldFactory wf = new WorldFactory();

        for (int i = 0; i < 60 ; i++) {
            if (Math.random() > 0.3) {
                list.add(wf.getWorldObject('g', i, 59));
            } else {
                list.add(wf.getWorldObject('s', i, 59));
                int k = 1;
                for (double j = Math.random(); j > 0.5 && k < 5; j = Math.random()) {
                    list.add(wf.getWorldObject('s', i, 59-k));
                    k++;
                }
            }
        }

        return list;
    }

    private List<AgentObject> generateAgents(List<WorldObject> worldList) {
        List<AgentObject> list = new ArrayList<AgentObject>();
        AgentFactory af = new AgentFactory();

        for (int i = 0; i < worldList.size(); i++){
            if (worldList.get(i).getClass() == Grass.class) {
                if (Math.random() < 0.2) {
                    list.add(af.getAgentObject('R', worldList.get(i).getX() / 16, worldList.get(i).getY() / 16 + 1));
                    System.out.println("yay!");
                }
            }
        }

        return list;
    }

    public List<WorldObject> getWorld(){
        return this.worldList;
    }

    public List<AgentObject> getAgents(){
        return this.agentList;
    }

    public List<Block> getBlocks(){
        return blockList;
    }
}
