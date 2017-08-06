package com.mimacom.test.main.input;

import com.mimacom.test.main.ProcessorConfiguration;
import com.mimacom.test.main.output.OutputProcessor;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Reader class for reading lines from input file
 */
public class InputReader {

    private File configuration;
    private File input;
    private File output;

    public InputReader(ProcessorConfiguration processorConfiguration, File inputFile) {
        this.configuration = processorConfiguration.getConfiguration();
        this.output = processorConfiguration.getOutput();
        this.input = inputFile;
    }

    /**
     * Reads lines from input file
     */
    public void read() {
        try {
            FileReader fileReader = new FileReader(input);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            List<String> lines = bufferedReader.lines().collect(Collectors.toList());

            lines.forEach(line -> processLine(line));
        }
        catch (FileNotFoundException e) {
            //TODO handle exception
            e.printStackTrace();
        }
    }

    /**
     * Checks lines against existing configuration rules. Based on configuration it writes lines to output files.
     * If configuration is not present (no rule can be matched), it writes to undefined.
     *
     * @param line
     */
    private void processLine(final String line) {
        boolean matchFound = false;

        List<ConfigurationRule> configurationRuleList = getConfigurationRules();

        for (ConfigurationRule rule : configurationRuleList) {
            Pattern configPattern = Pattern.compile(rule.getExpression());
            Matcher matcher = configPattern.matcher(line);

            if (matcher.matches()) {
                OutputProcessor outputProcessor = new OutputProcessor(output, rule.getFileName());
                outputProcessor.process(line);

                matchFound = true;
            }
        }

        if (!matchFound) {
            OutputProcessor outputProcessor = new OutputProcessor(output, "undefined");
            outputProcessor.process(line);
        }
    }

    /**
     * Retrieves configuration rules from configuration file.
     *
     * @return
     */
    private List<ConfigurationRule> getConfigurationRules() {
        List<ConfigurationRule> configurationRuleList = new ArrayList<>();
        try {
            if (configuration.exists() && configuration.isFile()) {
                FileReader fileReader = new FileReader(configuration);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                List<String> lines = bufferedReader.lines().collect(Collectors.toList());

                lines.forEach(line -> {
                    String[] lineRule = line.split(":");
                    ConfigurationRule configurationRule = new ConfigurationRule();
                    configurationRule.setFileName(lineRule[0].trim());
                    configurationRule.setExpression(lineRule[1].trim());

                    configurationRuleList.add(configurationRule);
                });

                return configurationRuleList;
            }
        }
        catch (FileNotFoundException e) {
            //TODO handle exception
            e.printStackTrace();
        }

        return Collections.EMPTY_LIST;
    }
}
