package System;

import Agents.AgentFactory;
import Agents.AgentObject;
import World.Grass;
import World.Stone;
import World.WorldFactory;
import World.WorldObject;

import java.util.ArrayList;
import java.util.List;

public class Chunk {
    private static List<Chunk> chunkList = new ArrayList<Chunk>();
    static int gameBlockPosition;
    private List<WorldObject> worldList = new ArrayList<WorldObject>();
    private List<AgentObject> agentList = new ArrayList<AgentObject>();
    private static int context = 0;
    private static DataHandeler dataHandeler = new DataHandeler();

    public void setAgentList(List<AgentObject> agentList) {
        this.agentList = agentList;
    }

    public Chunk() {
    }

    public Chunk(List<WorldObject> wo, List<AgentObject> ao) {
        this.worldList = wo;
        this.agentList = ao;
        chunkList.add(this);
    }

    private List<WorldObject> generateWorld() {
        List<WorldObject> list = new ArrayList<WorldObject>();
        WorldFactory wf = new WorldFactory();

        if (chunkList.size() == 0) {
            for (int i = 9; i <= 11; i++) {
                list.add(wf.getWorldObject('s', i, 25));
            }
        }

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
                    list.add(af.getAgentObject('R', (worldList.get(i).getX()- chunkList.size()*960) / 16, worldList.get(i).getY() / 16 - 1));
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
                    list.add(af.getAgentObject('C', (worldList.get(i).getX()- chunkList.size()*960) / 16, worldList.get(i).getY() / 16 - 4));
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
            if (i >= Chunk.getChunks().size()) break;
            tempList.addAll(Chunk.getChunks().get(i).getAgents());
        }

        return tempList;
    }

    public static List<WorldObject> conWorld(){
        List<WorldObject> tempList = new ArrayList<>();
        for (int i = context-1; i <= context+1; i++) {
            if (i < 0 ) continue;
            if (i >= Chunk.getChunks().size()) break;
            tempList.addAll(Chunk.getChunks().get(i).getWorld());
        }

        return tempList;
    }

    public static List<Chunk> getChunks(){
        return chunkList;
    }

    public static int getContext() {
        return context;
    }

    public static void setContext(int context) {
        Chunk.context = context;
    }

    public static void removeAgent(AgentObject ao) {
        for (int i = 0; i < Chunk.getChunks().size(); i++) {
            if (Chunk.getChunks().get(i).getAgents().remove(ao)) return;
        }
    }

    public void generateNewChunk(){
        Randomizer.clearData();
        long startTime = System.nanoTime();
        List <WorldObject> world = generateWorld();
        List <AgentObject> agents = generateAgents(world);
        new Chunk(world, agents);
        long endTime = System.nanoTime();
        dataHandeler.writeData(chunkList.size()-1, Randomizer.getState(), Randomizer.getData(), endTime-startTime);
    }

    public static void resetBlocks(){
        chunkList = new ArrayList<Chunk>();
    }
}
