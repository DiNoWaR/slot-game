package org.main;

import org.apache.commons.cli.*;
import org.config.model.GameConfig;
import org.config.parser.Parser;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Options options = new Options();

        options.addOption("c", "config", true, "Path to the configuration file");
        options.addOption("b", "betting-amount", true, "Betting amount");

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);

            String configPath = cmd.getOptionValue("config");
            String bettingAmount = cmd.getOptionValue("betting-amount");

            if (configPath == null || bettingAmount == null) {
                throw new ParseException("Both --config and --betting-amount are required");
            }

            System.out.println("Configuration file path: " + configPath);
            System.out.println("Betting amount: " + bettingAmount);

            GameConfig config = Parser.parseConfig(configPath);
            System.out.println(config.getProbabilities());

        } catch (ParseException e) {
            System.err.println("Error parsing arguments: " + e.getMessage());
            formatter.printHelp("Main", options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}