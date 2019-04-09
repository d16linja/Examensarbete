package System;

import Agents.AgentFactory;
import Agents.AgentObject;
import Agents.Player;
import World.Grass;
import World.Stone;
import World.WorldFactory;
import World.WorldObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Block {
    private static List<Block> blockList = new ArrayList<Block>();
    static int gameBlockPosition;
    private List<WorldObject> worldList = new ArrayList<WorldObject>();
    private List<AgentObject> agentList = new ArrayList<AgentObject>();
    private static int context = 0;
    private static DataHandeler dataHandeler = new DataHandeler();

    public void setAgentList(List<AgentObject> agentList) {
        this.agentList = agentList;
    }

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
            if (Randomizer.get() > 0.3) {
                list.add(wf.getWorldObject('g', i, 39));
            } else {
                list.add(wf.getWorldObject('s', i, 39));
                int k = 1;
                for (double j = Randomizer.get(); j > 0.5 && k < 6; j = Randomizer.get()) {
                    list.add(wf.getWorldObject('s', i, 39-k));
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
                if (Randomizer.get() < 0.2) {
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
        Randomizer.clearData();
        long startTime = System.nanoTime();
        List <WorldObject> world = generateWorld();
        List <AgentObject> agents = generateAgents(world);
        new Block(world, agents);
        long endTime = System.nanoTime();
        dataHandeler.writeData(blockList.size()-1, Randomizer.getState(), Randomizer.getData(), endTime-startTime);
    }

    public static void resetBlocks(){
        blockList = new ArrayList<Block>();
    }
}
