package org.generator;

import org.config.model.GameConfig;

import java.util.*;

public class Generator {
    public String[][] generateMatrix(GameConfig config) {
        var random = new Random();
        var rows = config.getRows();
        var columns = config.getColumns();
        var matrix = new String[rows][columns];

        var standardSymbols = config.getProbabilities().getStandardSymbols();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                matrix[row][col] = getRandomSymbol(standardSymbols, random);
            }
        }
        return matrix;
    }

    public Map<String, List<String>> checkWinningCombinations(String[][] matrix, GameConfig config) {
        Map<String, List<String>> appliedCombinations = new HashMap<>();

        for (Map.Entry<String, GameConfig.WinCombination> entry : config.getWinCombinations().entrySet()) {
            GameConfig.WinCombination combination = entry.getValue();

            if (combination.getWhen().equals("same_symbols")) {
                String symbol = findMatchingSymbols(matrix, combination.getCount());
                if (symbol != null) {
                    appliedCombinations.putIfAbsent(symbol, new ArrayList<>());
                    appliedCombinations.get(symbol).add(entry.getKey());
                }
            }
        }
        return appliedCombinations;
    }

    public double calculateReward(double betAmount, Map<String, List<String>> winningCombinations, GameConfig config) {
        double reward = 0;

        for (Map.Entry<String, List<String>> entry : winningCombinations.entrySet()) {
            String symbol = entry.getKey();
            List<String> combinations = entry.getValue();

            GameConfig.Symbol configSymbol = config.getSymbols().get(symbol);
            double symbolReward = betAmount * configSymbol.getRewardMultiplier();

            for (String combination : combinations) {
                GameConfig.WinCombination winCombination = config.getWinCombinations().get(combination);
                symbolReward *= winCombination.getRewardMultiplier();
            }

            reward += symbolReward;
        }
        return reward;
    }

    public double applyBonus(double reward, String[][] matrix, GameConfig config) {
        for (String[] row : matrix) {
            for (String cell : row) {
                GameConfig.Symbol symbol = config.getSymbols().get(cell);
                if (symbol != null && symbol.getType().equals("bonus")) {
                    if (symbol.getImpact().equals("multiply_reward")) {
                        reward *= symbol.getRewardMultiplier();
                    } else if (symbol.getImpact().equals("extra_bonus")) {
                        reward += symbol.getExtra();
                    }
                }
            }
        }
        return reward;
    }

    private String getRandomSymbol(List<GameConfig.StandardSymbol> symbols, Random random) {
        int totalWeight = symbols.stream().mapToInt(s -> s.getSymbols().values().stream().mapToInt(Integer::intValue).sum()).sum();
        int randomValue = random.nextInt(totalWeight);

        int cumulativeWeight = 0;
        for (GameConfig.StandardSymbol symbol : symbols) {
            for (Map.Entry<String, Integer> entry : symbol.getSymbols().entrySet()) {
                cumulativeWeight += entry.getValue();
                if (randomValue < cumulativeWeight) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    private String findMatchingSymbols(String[][] matrix, int count) {
        Map<String, Integer> symbolCounts = new HashMap<>();
        for (String[] row : matrix) {
            for (String cell : row) {
                symbolCounts.put(cell, symbolCounts.getOrDefault(cell, 0) + 1);
            }
        }

        for (Map.Entry<String, Integer> entry : symbolCounts.entrySet()) {
            if (entry.getValue() >= count) {
                return entry.getKey();
            }
        }
        return null;
    }
}
