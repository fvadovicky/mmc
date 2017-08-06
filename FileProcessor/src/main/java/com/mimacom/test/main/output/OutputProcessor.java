package com.mimacom.test.main.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * Writer class for writing lines to files inside output directory
 */
public class OutputProcessor {
    private File output;
    private String fileName;

    public OutputProcessor(File output, String fileName) {
        this.output = output;
        this.fileName = fileName;
    }

    /**
     * Writes line to file - file is appended, if it does not exist it is created first
     *
     * @param line
     */
    public void process(String line) {
        if (output.exists() && output.isDirectory()) {
            Optional<File> fileOptional = Arrays.stream(output.listFiles()).filter(file -> file.getName().equals(fileName)).findFirst();
            File file;
            if (fileOptional.isPresent()) {
                file = fileOptional.get();
            }
            else {
                file = new File(output.getAbsolutePath() + "\\" + fileName);
            }
            FileWriter fileWriter = null;
            BufferedWriter bufferedWriter = null;
            try {
                fileWriter = new FileWriter(file, true);
                bufferedWriter = new BufferedWriter(fileWriter);

                bufferedWriter.write(line);
                bufferedWriter.newLine();

                bufferedWriter.flush();
            }
            catch (IOException e) {
                //TODO handle exception
                e.printStackTrace();
            }
            finally {
                try {
                    if (fileWriter != null) {
                        fileWriter.close();
                    }
                    if (bufferedWriter != null) {
                        bufferedWriter.close();
                    }
                }
                catch (IOException e) {
                    //TODO handle exception
                    e.printStackTrace();
                }
            }
        }
    }
}
