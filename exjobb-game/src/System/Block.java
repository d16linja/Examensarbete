package System;

import Agents.AgentFactory;
import Agents.AgentObject;
import Agents.Player;
import World.Grass;
import World.World;
import World.WorldFactory;
import World.WorldObject;

import java.util.ArrayList;
import java.util.List;

public class Block {
    private static List<Block> blockList = new ArrayList<Block>();
    static int gameBlockPosition;
    private List<WorldObject> worldList = new ArrayList<WorldObject>();
    private List<AgentObject> agentList = new ArrayList<AgentObject>();
    private static int context = 0;

    public Block() {
    }

    public Block(List<WorldObject> wo, List<AgentObject> ao) {
        this.worldList = wo;
        this.agentList = ao;
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
                for (double j = Math.random(); j > 0.5 && k < 6; j = Math.random()) {
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
                    list.add(af.getAgentObject('R', worldList.get(i).getX() / 16, worldList.get(i).getY() / 16 - 1));
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

    public static List<AgentObject> conAgents(){
        List<AgentObject> tempList = new ArrayList<>();
        if (context == 0 || context-1 < 0) {
            tempList.addAll(blockList.get(context).getAgents());
        } else {
            for (int i = -1; i <= 1; i++){
                tempList.addAll(blockList.get(context+i).getAgents());
            }
        }
        return tempList;
    }

    public static List<WorldObject> conWorld(){
        List<WorldObject> tempList = new ArrayList<>();
        if (context == 0 || context-1 < 0 ) {
            tempList.addAll(blockList.get(context).getWorld());
        } else {
            for (int i = -1; i <= 1; i++){
                tempList.addAll(blockList.get(context+i).getWorld());
            }
        }

        return tempList;
    }

    public static List<Block> getBlocks(){
        return blockList;
    }

    public static int getContext() {
        return context;
    }

    public static void setContext(int context) {
        Block.context = context;
    }

    public static void removeAgent(AgentObject ao) {
        for (int i = 0; i < Block.getBlocks().size(); i++) {
            if (Block.getBlocks().get(i).getAgents().remove(ao)) return;
        }
    }

    public void generateNewBlock(){
        List <WorldObject> world = generateWorld();
        List <AgentObject> agents = generateAgents(world);
        new Block(world, agents);
    }
}
