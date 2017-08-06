package com.mimacom.test.main;

import com.mimacom.test.main.input.InputReader;

import java.io.File;
import java.util.concurrent.Callable;


public class FileProcessorTask implements Callable<String> {

    private ProcessorConfiguration processorConfiguration;
    private File inputFile;

    public FileProcessorTask(ProcessorConfiguration processorConfiguration, File inputFile) {
        this.processorConfiguration = processorConfiguration;
        this.inputFile = inputFile;
    }

    @Override
    public String call() throws Exception {
        InputReader inputReader = new InputReader(processorConfiguration, inputFile);
        inputReader.read();

        return inputFile.getName();
    }
}
