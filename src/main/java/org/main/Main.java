package org.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.cli.*;
import org.config.parser.Parser;
import org.generator.GameProcessor;
import org.generator.Result;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        var options = new Options();

        options.addOption("c", "config", true, "config file which is described top of the document");
        options.addOption("b", "betting-amount", true, "betting amount");

        var parser = new DefaultParser();
        var formatter = new HelpFormatter();
        var gameResult = new Result();

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
            var matrix = gameProcessor.generateMatrix(config, gameResult);

            var winCombinations = gameProcessor.checkWinningCombinations(matrix, config);
            var reward = gameProcessor.calculateReward(matrix, betAmount, winCombinations, config);

            gameResult.setReward(reward);
            gameResult.setMatrix(matrix);
            gameResult.setAppliedWinningCombinations(winCombinations);

            if (reward == 0) {
                gameResult.clearBonusSymbols();
            }

            var mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            String jsonOutput = mapper.writeValueAsString(gameResult);
            System.out.println(jsonOutput);

        } catch (ParseException err) {
            System.err.println("Error parsing arguments: " + err.getMessage());
            formatter.printHelp("Main", options);
        } catch (IOException err) {
            err.printStackTrace();
        }
    }
}