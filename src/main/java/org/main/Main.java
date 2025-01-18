package org.main;

import org.apache.commons.cli.*;
import org.config.parser.Parser;
import org.generator.GameProcessor;

import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        var options = new Options();

        options.addOption("c", "config", true, "Path to the configuration file");
        options.addOption("b", "betting-amount", true, "Betting amount");

        var parser = new DefaultParser();
        var formatter = new HelpFormatter();

        try {
            var cmd = parser.parse(options, args);

            var configPath = cmd.getOptionValue("config");
            var betAmountStr = cmd.getOptionValue("betting-amount");

            if (configPath == null || betAmountStr == null) {
                throw new ParseException("Both --config and --betting-amount are required");
            }

            double betAmount;
            try {
                betAmount = Double.parseDouble(betAmountStr);
                if (betAmount <= 0) {
                    throw new IllegalArgumentException("Bet amount must be greater than zero.");
                }
            } catch (NumberFormatException e) {
                throw new ParseException("Betting amount must be a valid number");
            }

            var config = Parser.parseConfig(configPath);
            var gameProcessor = new GameProcessor();
            var matrix = gameProcessor.generateMatrix(config);

            System.out.println("Generated Matrix:");
            for (String[] row : matrix) {
                System.out.println(Arrays.toString(row));
            }


        } catch (ParseException e) {
            System.err.println("Error parsing arguments: " + e.getMessage());
            formatter.printHelp("Main", options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}