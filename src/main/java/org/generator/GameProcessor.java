package org.generator;

import org.config.model.GameConfig;

import java.util.*;

public class GameProcessor {

    /**
     * We assume that bonus symbol possibility is 10% because it is not mentioned in TD
     */
    private static final int BONUS_SYMBOL_POSSIBILITY = 10;
    private final Random random = new Random();

    public String[][] generateMatrix(GameConfig config) {
        var rows = config.getRows();
        var columns = config.getColumns();
        var matrix = new String[rows][columns];

        var standardSymbols = config.getProbabilities().getStandardSymbols();
        var bonusSymbols = config.getProbabilities().getBonusSymbols().getSymbols();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                if (isBonus(random)) {
                    matrix[row][col] = getRandomBonusSymbol(bonusSymbols);
                } else {
                    matrix[row][col] = getRandomStandardSymbol(standardSymbols, row, col);
                }
            }
        }
        return matrix;
    }

    public Map<String, List<String>> checkWinningCombinations(String[][] matrix, GameConfig config) {
        Map<String, List<String>> winningCombinations = new HashMap<>();

        checkSameSymbols(matrix, config, winningCombinations);
        checkLinearSymbols(matrix, config, winningCombinations);

        return winningCombinations;
    }

    public double calculateReward(String[][] matrix, double betAmount, Map<String, List<String>> winningCombinations, GameConfig config) {
        if (betAmount <= 0) {
            throw new IllegalArgumentException("Bet amount must be greater than zero.");
        }

        double totalReward = 0;

        for (Map.Entry<String, List<String>> entry : winningCombinations.entrySet()) {
            var symbol = entry.getKey();
            var combinations = entry.getValue();

            var configSymbol = config.getSymbols().get(symbol);
            if (configSymbol == null) {
                continue;
            }

            var symbolReward = betAmount * configSymbol.getRewardMultiplier();
            for (String combinationName : combinations) {
                GameConfig.WinCombination winCombination = config.getWinCombinations().get(combinationName);
                if (winCombination != null) {
                    symbolReward *= winCombination.getRewardMultiplier();
                }
            }

            totalReward += symbolReward;
        }

        // Bonus symbols are only effective when there are at least one winning combinations matches with the generated matrix
        if (!winningCombinations.isEmpty()) {
            totalReward = applyBonusSymbols(totalReward, matrix, config);
        }

        return totalReward;
    }

    private void checkSameSymbols(String[][] matrix, GameConfig config, Map<String, List<String>> winningCombinations) {
        Map<String, Integer> symbolCounts = new HashMap<>();

        for (String[] row : matrix) {
            for (String cell : row) {
                symbolCounts.put(cell, symbolCounts.getOrDefault(cell, 0) + 1);
            }
        }

        Map<String, String> maxCombinations = new HashMap<>();

        for (Map.Entry<String, GameConfig.WinCombination> entry : config.getWinCombinations().entrySet()) {
            GameConfig.WinCombination combination = entry.getValue();
            if ("same_symbols".equals(combination.getWhen())) {
                for (Map.Entry<String, Integer> symbolCount : symbolCounts.entrySet()) {
                    String symbol = symbolCount.getKey();
                    int count = symbolCount.getValue();

                    if (count >= combination.getCount() && (!maxCombinations.containsKey(symbol) || config.getWinCombinations().get(maxCombinations.get(symbol)).getCount() < combination.getCount())) {
                            maxCombinations.put(symbol, entry.getKey());
                        }
                }
            }
        }

        for (Map.Entry<String, String> entry : maxCombinations.entrySet()) {
            winningCombinations.putIfAbsent(entry.getKey(), new ArrayList<>());
            winningCombinations.get(entry.getKey()).add(entry.getValue());
        }
    }

    private void checkLinearSymbols(String[][] matrix, GameConfig config, Map<String, List<String>> winningCombinations) {
        for (Map.Entry<String, GameConfig.WinCombination> entry : config.getWinCombinations().entrySet()) {
            GameConfig.WinCombination combination = entry.getValue();
            if ("linear_symbols".equals(combination.getWhen())) {
                for (List<String> area : combination.getCoveredAreas()) {
                    if (isWinningArea(matrix, area)) {
                        String symbol = getSymbolAt(matrix, area.getFirst());
                        winningCombinations.putIfAbsent(symbol, new ArrayList<>());
                        winningCombinations.get(symbol).add(entry.getKey());
                    }
                }
            }
        }
    }

    private String getRandomStandardSymbol(List<GameConfig.StandardSymbol> symbols, int row, int col) {
        for (GameConfig.StandardSymbol symbolConfig : symbols) {
            if (symbolConfig.getRow() == row && symbolConfig.getColumn() == col) {
                var symbolWeights = symbolConfig.getSymbols();
                var totalWeight = symbolWeights.values().stream().mapToInt(Integer::intValue).sum();
                var randomValue = random.nextInt(totalWeight);

                var cumulativeWeight = 0;
                for (Map.Entry<String, Integer> entry : symbolWeights.entrySet()) {
                    cumulativeWeight += entry.getValue();
                    if (randomValue < cumulativeWeight) {
                        return entry.getKey();
                    }
                }
            }
        }
        return null;
    }

    private String getRandomBonusSymbol(Map<String, Integer> bonusSymbols) {
        var totalWeight = bonusSymbols.values().stream().mapToInt(Integer::intValue).sum();
        if (totalWeight == 0) {
            return "MISS";
        }

        var randomValue = random.nextInt(totalWeight);
        var cumulativeWeight = 0;

        for (Map.Entry<String, Integer> entry : bonusSymbols.entrySet()) {
            cumulativeWeight += entry.getValue();
            if (randomValue < cumulativeWeight) {
                return entry.getKey();
            }
        }
        return "MISS";
    }

    private boolean isBonus(Random random) {
        return random.nextInt(100) < BONUS_SYMBOL_POSSIBILITY;
    }

    private String getSymbolAt(String[][] matrix, String cell) {
        var parts = cell.split(":");
        var row = Integer.parseInt(parts[0]);
        var col = Integer.parseInt(parts[1]);
        return matrix[row][col];
    }

    private boolean isWinningArea(String[][] matrix, List<String> area) {
        String firstSymbol = null;
        for (String cell : area) {
            var parts = cell.split(":");
            var row = Integer.parseInt(parts[0]);
            var col = Integer.parseInt(parts[1]);
            var currentSymbol = matrix[row][col];

            if (firstSymbol == null) {
                firstSymbol = currentSymbol;
            } else if (!firstSymbol.equals(currentSymbol)) {
                return false;
            }
        }
        return true;
    }

    private double applyBonusSymbols(double reward, String[][] matrix, GameConfig config) {
        for (String[] row : matrix) {
            for (String cell : row) {
                GameConfig.Symbol symbol = config.getSymbols().get(cell);
                if (symbol != null && "bonus".equals(symbol.getType())) {
                    switch (symbol.getImpact()) {
                        case "multiply_reward":
                            reward *= symbol.getRewardMultiplier();
                            break;
                        case "extra_bonus":
                            reward += symbol.getExtra();
                            break;
                        case "miss":
                            break;
                        default:
                            throw new IllegalArgumentException("unknown impact: " + symbol.getImpact());
                    }
                }
            }
        }
        return reward;
    }
}
