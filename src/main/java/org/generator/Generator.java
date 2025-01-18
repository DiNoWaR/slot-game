package org.generator;

import org.config.model.GameConfig;

import java.util.*;

public class Generator {
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
}
