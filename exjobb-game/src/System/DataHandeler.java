package System;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DataHandeler {
    String dataBasePath = System.getProperty("user.dir") + "/collected";
    File file =  new File(dataBasePath);
    String currentRuntimePath;

    public DataHandeler() {

        if (!file.exists()) {
            file.mkdirs();
        }

        String[] directories = file.list((current, name) -> new File(current, name).isDirectory());

        currentRuntimePath = dataBasePath + "/" + directories.length;

        file = new File(currentRuntimePath);

        file.mkdirs();
    }

    public void writeData(int block, Randomizer.State state, List<Double> dataList, long time) {
        File dataFile = new File(currentRuntimePath + "/" + state + "/");
        dataFile.mkdirs();

        dataFile = new File(file.getAbsolutePath() + "/" + state + "/" + block + ".txt");
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
