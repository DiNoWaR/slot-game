package org.main;

import org.apache.commons.cli.*;
import org.config.parser.Parser;
import org.generator.Generator;

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
            var bettingAmountStr = cmd.getOptionValue("betting-amount");

            if (configPath == null || bettingAmountStr == null) {
                throw new ParseException("Both --config and --betting-amount are required");
            }

            double bettingAmount;
            try {
                bettingAmount = Double.parseDouble(bettingAmountStr);
            } catch (NumberFormatException e) {
                throw new ParseException("Betting amount must be a valid number");
            }

            var config = Parser.parseConfig(configPath);
            var generator = new Generator();
            var matrix = generator.generateMatrix(config);

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