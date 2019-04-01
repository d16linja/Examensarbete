package System;

import Agents.AgentFactory;
import Agents.AgentObject;
import Agents.Player;
import World.Grass;
import World.Stone;
import World.WorldFactory;
import World.WorldObject;

import java.util.ArrayList;
import java.util.List;

public class Block {
    private static List<Block> blockList = new ArrayList<Block>();
    static int gameBlockPosition;
    private List<WorldObject> worldList = new ArrayList<WorldObject>();

    public void setAgentList(List<AgentObject> agentList) {
        this.agentList = agentList;
    }

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
        boolean emptyAbove;

        for (int i = 0; i < worldList.size(); i++){
            if (worldList.get(i).getClass() == Grass.class) {
                if (Math.random() < 0.2) {
                    list.add(af.getAgentObject('R', (worldList.get(i).getX()-blockList.size()*960) / 16, worldList.get(i).getY() / 16 - 1));
                }
            } else if (worldList.get(i).getClass() == Stone.class) {
                emptyAbove = true;
                for (int j = 0; j < worldList.size(); j++) {
                    if (worldList.get(i).getX() == worldList.get(j).getX() && worldList.get(i).getY() == worldList.get(j).getY()+16) {
                        emptyAbove = false;
                        break;
                    }
                }

                if (emptyAbove) {
                    list.add(af.getAgentObject('C', (worldList.get(i).getX()-blockList.size()*960) / 16, worldList.get(i).getY() / 16 - 4));
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
        for (int i = context-1; i <= context+1; i++) {
            if (i < 0 ) continue;
            if (i >= Block.getBlocks().size()) break;
            tempList.addAll(Block.getBlocks().get(i).getAgents());
        }

        return tempList;
    }

    public static List<WorldObject> conWorld(){
        List<WorldObject> tempList = new ArrayList<>();
        for (int i = context-1; i <= context+1; i++) {
            if (i < 0 ) continue;
            if (i >= Block.getBlocks().size()) break;
            tempList.addAll(Block.getBlocks().get(i).getWorld());
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
