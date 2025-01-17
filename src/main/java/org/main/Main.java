package org.main;

import org.apache.commons.cli.*;
import org.config.model.GameConfig;
import org.config.parser.Parser;
import org.generator.Generator;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
            String bettingAmountStr = cmd.getOptionValue("betting-amount");

            if (configPath == null || bettingAmountStr == null) {
                throw new ParseException("Both --config and --betting-amount are required");
            }

            double bettingAmount;
            try {
                bettingAmount = Double.parseDouble(bettingAmountStr);
            } catch (NumberFormatException e) {
                throw new ParseException("Betting amount must be a valid number");
            }

            GameConfig config = Parser.parseConfig(configPath);
            var generator = new Generator();
            String[][] matrix = generator.generateMatrix(config);

            System.out.println("Generated Matrix:");
            for (String[] row : matrix) {
                System.out.println(Arrays.toString(row));
            }

            Map<String, List<String>> winningCombinations = generator.checkWinningCombinations(matrix, config);
            double reward = generator.calculateReward(bettingAmount, winningCombinations, config);
            reward = generator.applyBonus(reward, matrix, config);

            System.out.println("Final Reward: " + reward);

        } catch (ParseException e) {
            System.err.println("Error parsing arguments: " + e.getMessage());
            formatter.printHelp("Main", options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}