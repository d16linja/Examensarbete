package System;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class DataHandeler {
    String dataBasePath = System.getProperty("user.dir") + "/collected";
    File file =  new File(dataBasePath);
    String currentRuntimePath;
    int testNr = 0;
    final int runtime;

    public DataHandeler() {

        if (!file.exists()) {
            file.mkdirs();
        }

        String[] directories = file.list((current, name) -> new File(current, name).isDirectory());

        runtime = directories.length;

        currentRuntimePath = dataBasePath + "/" + runtime;

        file = new File(currentRuntimePath);

        file.mkdirs();
    }

    public int getRuntime() {
        return runtime;
    }

    public void writeData(int block, Randomizer.State state, List<Double> dataList, long time) {

        boolean newState = true;
        String[] directories = file.list((current, name) -> new File(current, name).isDirectory());

        if (directories.length > 0) {
            for (String file:directories) {

                if (file.contains(state.toString())) newState = false;
            }
        }

        if (newState) testNr ++;


        File dataFile = new File(currentRuntimePath + "/" + testNr + ". " + state + "/");
        dataFile.mkdirs();

        dataFile = new File(file.getAbsolutePath() + "/" + testNr + ". " + state + "/" + block + ".txt");
        dataFile.setWritable(true);


        try {
            dataFile.createNewFile();
            FileWriter writer = new FileWriter(dataFile);
            writer.write((int)time + "\n");
            for (Double data : dataList) {
                writer.write(data + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.err.println(e);
            System.err.println("Error writing testdata, needs manual correction at:");
            System.err.print(file.getAbsolutePath() + "/" + state + "/" + block + ".txt");
            System.exit(0);
        }
    }
}
