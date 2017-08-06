package com.mimacom.test.main;

import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class FileProcessorTest {

    private final File CONFIGURATION = new File("src\\test\\resources\\configuration");
    private final File INPUT = new File("src\\test\\resources\\input");
    private final File OUTPUT = new File("src\\test\\resources\\output");

    @Test
    public void testDoProcess() {
        ProcessorConfiguration processorConfiguration = new ProcessorConfiguration();

        processorConfiguration.setConfiguration(CONFIGURATION);
        processorConfiguration.setInput(INPUT);
        processorConfiguration.setOutput(OUTPUT);

        FileProcessor fileProcessor = new FileProcessor(processorConfiguration);

        fileProcessor.doProcess();

        assertOutput();
    }

    private void assertOutput() {
        if (OUTPUT.exists() && OUTPUT.isDirectory()) {
            List<File> fileList = Arrays.stream(OUTPUT.listFiles()).collect(Collectors.toList());

            assertNotNull(fileList);
            assertTrue(!fileList.isEmpty());

            fileList.sort((File f1, File f2) -> f1.getName().compareTo(f2.getName()));

            assertEquals("internal", fileList.get(0).getName());;
            assertEquals("notfound", fileList.get(1).getName());;
            assertEquals("undefined", fileList.get(2).getName());;
        }
    }
}
