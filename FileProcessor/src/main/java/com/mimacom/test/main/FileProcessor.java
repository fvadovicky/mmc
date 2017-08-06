package com.mimacom.test.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * File processor main class
 */
public class FileProcessor {

    private ProcessorConfiguration processorConfiguration;

    public FileProcessor(ProcessorConfiguration processorConfiguration) {
        this.processorConfiguration = processorConfiguration;
    }

    /**
     * Method gets all files inside input directory and for each file it creates FileProcessorTask.
     * Tasks are submitted to ExecutorService
     *
     */
    public void doProcess() {
        ExecutorService executorService = null;
        try {
            executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

            List<FileProcessorTask> fileTaskList = new ArrayList<>();

            if (processorConfiguration.getInput().exists() && processorConfiguration.getInput().isDirectory()) {
                Arrays.stream(processorConfiguration.getInput().listFiles()).forEach(file -> {
                    if (file.isFile()) {
                        fileTaskList.add(new FileProcessorTask(processorConfiguration, file));
                    }
                });
            }

            List<Future<String>> results = executorService.invokeAll(fileTaskList);

            //handle results
            results.forEach(result -> {
                printResults(result);
            });
        }
        catch (InterruptedException e) {
            //TODO handle exception
            e.printStackTrace();
        }
        finally {
            if (executorService != null) {
                executorService.shutdown();
            }
        }
    }

    /**
     * Method prints results of tasks submitted to executor service.
     * Prints processed file name and isDone result.
     *
     * @param result
     */
    private void printResults(Future<String> result) {
        try {
            System.out.println(String.format("Finished reading file %s: isDone=%s", result.get(), result.isDone()));
        }
        catch (InterruptedException e) {
            //TODO handle exception
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            //TODO handle exception
            e.printStackTrace();
        }
    }

    /**
     * Main method - gets arguments and creates FileProcessor instance
     *
     * @param argv
     */
    public static void main(String[] argv) {
        ProcessorConfiguration processorConfiguration = parseInputArguments(argv);

        FileProcessor fileProcessor = new FileProcessor(processorConfiguration);
        fileProcessor.doProcess();
    }

    /**
     * Reads input arguments from command line
     *
     * @param argv
     * @return
     */
    private static ProcessorConfiguration parseInputArguments(String[] argv) {
        try {
            String configuration = Arrays.stream(argv).filter(argument -> argument.startsWith("--configuration")).findFirst().orElseThrow(IllegalArgumentException::new);

            String[] split = configuration.trim().split("\\=");
            if (split.length == 2) {
                configuration = split[1];
            }

            String input = Arrays.stream(argv).filter(argument -> argument.startsWith("--input")).findFirst().orElseThrow(IllegalArgumentException::new);
            split = input.trim().split("\\=");
            if (split.length == 2) {
                input = split[1];
            }

            String output = Arrays.stream(argv).filter(argument -> argument.startsWith("--output")).findFirst().orElseThrow(IllegalArgumentException::new);
            split = output.trim().split("\\=");
            if (split.length == 2) {
                output = split[1];
            }

            return new ProcessorConfiguration(configuration, input, output);
        }
        catch (IllegalArgumentException e) {
            System.out.println(String.format("Missing arguments. Valid arguments are: --configuration=\"path_to_configuration_file\" --input=\"path_to_input_directory\" --output=\"path_to_output_directory\""));

            throw e;
        }
    }
}





