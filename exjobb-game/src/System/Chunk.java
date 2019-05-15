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
    final private static DataHandeler dataHandeler = new DataHandeler();
    private Randomizer randomizer;
    WorldFactory wf = new WorldFactory();
    AgentFactory af = new AgentFactory();
    public void setAgentList(List<AgentObject> agentList) {
        this.agentList = agentList;
    }

    public Chunk() {

        if (dataHandeler.getRuntime()%2 == 0){
            randomizer = new Randomizer(Randomizer.State.CRYPTO);
        }
        else {
            randomizer = new Randomizer(Randomizer.State.NORMAL);
        }
    }

    public Chunk(List<WorldObject> wo, List<AgentObject> ao) {
        this.worldList = wo;
        this.agentList = ao;
        chunkList.add(this);
    }

    public void addRespawnToChunk(int chunk){
        for (int i = 9; i <= 11; i++) {
            chunkList.get(chunk).worldList.add(wf.getWorldObject('s', i, 25));
            chunkList.get(chunk).worldList.get(chunkList.get(chunk).worldList.size()-1).setX(i*16 + chunk * 960);
        }
    }

    private List<WorldObject> generateWorld() {
        List<WorldObject> list = new ArrayList<WorldObject>();

        boolean makeHole = false, holePossible = true;

        if (chunkList.size() == 0) {
            for (int i = 9; i <= 11; i++) {
                list.add(wf.getWorldObject('s', i, 25));
            }
            for (int i = 0; i < 40; i++) {
                list.add(wf.getWorldObject('s', 0, i));
            }
        }

        for (int i = 0; i < 60 ; i++) {
            if (chunkList.size() == 0 && i == 0) continue;

            if ((Randomizer.get() > 0.95 || makeHole) && holePossible) {
                if (makeHole) {
                    makeHole = false;
                    holePossible = false;
                    continue;
                } else {
                    makeHole = true;
                    continue;
                }
            }

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

            holePossible = true;
        }

        return list;
    }

    private List<AgentObject> generateAgents(List<WorldObject> worldList) {
        List<AgentObject> list = new ArrayList<AgentObject>();
        boolean emptyAbove;

        for (int i = 0; i < worldList.size(); i++){
            if (worldList.get(i).getClass() == Grass.class) {
                if (Randomizer.get() < 0.1) {
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

                if (emptyAbove && Randomizer.get() < 0.5) {
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

    public static void resetChunks(){
        chunkList = new ArrayList<Chunk>();
    }
}
