package com.mimacom.test.main;

import java.io.File;

public class ProcessorConfiguration {

    private File configuration;
    private File input;
    private File output;

    public ProcessorConfiguration() {

    }

    public ProcessorConfiguration(String fileConfigPath, String inputFilePath, String outputFilePath) {
        configuration = new File(fileConfigPath);
        input = new File(inputFilePath);
        output = new File(outputFilePath);
    }

    public File getConfiguration() {
        return configuration;
    }

    public File getInput() {
        return input;
    }

    public File getOutput() {
        return output;
    }

    public void setConfiguration(File configuration) {
        this.configuration = configuration;
    }

    public void setInput(File input) {
        this.input = input;
    }

    public void setOutput(File output) {
        this.output = output;
    }
}
