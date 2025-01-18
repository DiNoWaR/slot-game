package org.generator;

import org.config.model.GameConfig;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameProcessorTest {

    @Test
    void testCheckWinningCombinationsWithSameSymbols() {
        String[][] matrix = {
                {"A", "A", "A"},
                {"B", "C", "D"},
                {"E", "F", "G"}
        };

        var config = new GameConfig();
        config.setWinCombinations(Map.of(
                "same_symbol_3_times",
                new GameConfig.WinCombination(
                        1,
                        "same_symbols",
                        3,
                        "same_symbols",
                        null
                ),
                "same_symbols_horizontally",
                new GameConfig.WinCombination(
                        2,
                        "linear_symbols",
                        null,
                        "horizontally_linear_symbols",
                        List.of(List.of("0:0", "0:1", "0:2"), List.of("1:0", "1:1", "1:2"), List.of("2:0", "2:1", "2:2")))
        ));

        var processor = new GameProcessor();
        var result = processor.checkWinningCombinations(matrix, config);
        var expected = Map.of("A", List.of("same_symbol_3_times", "same_symbols_horizontally"));

        assertEquals(expected, result, "");
    }

    @Test
    void testCheckWinningCombinationsNoMatches() {
        String[][] matrix = {
                {"A", "B", "C"},
                {"D", "E", "F"},
                {"G", "H", "I"}
        };

        var config = new GameConfig();
        config.setWinCombinations(Map.of(
                "same_symbol_3_times",
                new GameConfig.WinCombination(
                        1,
                        "same_symbols",
                        3,
                        "same_symbols",
                        null
                ),
                "same_symbols_horizontally",
                new GameConfig.WinCombination(
                        2,
                        "linear_symbols",
                        null,
                        "horizontally_linear_symbols",
                        List.of(List.of("0:0", "0:1", "0:2"), List.of("1:0", "1:1", "1:2"), List.of("2:0", "2:1", "2:2")))
        ));

        var processor = new GameProcessor();
        var result = processor.checkWinningCombinations(matrix, config);

        var expected = Map.of();
        assertEquals(expected, result, "");
    }

    @Test
    void testCheckWinningCombinationsVerticalMatch() {
        String[][] matrix = {
                {"A", "B", "C"},
                {"A", "E", "F"},
                {"A", "H", "I"}
        };

        var config = new GameConfig();
        config.setWinCombinations(Map.of(
                "same_symbol_3_times",
                new GameConfig.WinCombination(
                        1,
                        "same_symbols",
                        3,
                        "same_symbols",
                        null
                ),
                "same_symbols_vertically",
                new GameConfig.WinCombination(
                        2,
                        "linear_symbols",
                        null,
                        "vertically_linear_symbols",
                        List.of(List.of("0:0", "1:0", "2:0"), List.of("0:1", "1:1", "2:1"), List.of("0:2", "1:2", "2:2")))
        ));

        var processor = new GameProcessor();
        var result = processor.checkWinningCombinations(matrix, config);
        var expected = Map.of("A", List.of("same_symbol_3_times", "same_symbols_vertically"));

        assertEquals(expected, result, "");
    }

    @Test
    void testCheckWinningCombinationsDiagonalMatch() {
        String[][] matrix = {
                {"A", "B", "C"},
                {"D", "A", "F"},
                {"G", "H", "A"}
        };

        var config = new GameConfig();
        config.setWinCombinations(Map.of(
                "same_symbol_3_times",
                new GameConfig.WinCombination(
                        1,
                        "same_symbols",
                        3,
                        "same_symbols",
                        null
                ),
                "same_symbols_diagonally",
                new GameConfig.WinCombination(
                        5,
                        "linear_symbols",
                        null,
                        "diagonal_linear_symbols",
                        List.of(List.of("0:0", "1:1", "2:2"), List.of("0:2", "1:1", "2:0")))
        ));

        var processor = new GameProcessor();

        var result = processor.checkWinningCombinations(matrix, config);
        var expected = Map.of("A", List.of("same_symbol_3_times", "same_symbols_diagonally"));

        assertEquals(expected, result, "");
    }

    @Test
    void testCheckWinningCombinationsMultipleSymbols() {
        String[][] matrix = {
                {"A", "A", "A"},
                {"B", "B", "B"},
                {"C", "D", "E"}
        };

        var config = new GameConfig();
        config.setWinCombinations(Map.of(
                "same_symbol_3_times",
                new GameConfig.WinCombination(
                        1,
                        "same_symbols",
                        3,
                        "same_symbols",
                        null
                ),
                "same_symbols_horizontally",
                new GameConfig.WinCombination(
                        2,
                        "linear_symbols",
                        null,
                        "horizontally_linear_symbols",
                        List.of(List.of("0:0", "0:1", "0:2"), List.of("1:0", "1:1", "1:2"), List.of("2:0", "2:1", "2:2")))
        ));

        var processor = new GameProcessor();

        var result = processor.checkWinningCombinations(matrix, config);
        var expected = Map.of(
                "A", List.of("same_symbol_3_times", "same_symbols_horizontally"),
                "B", List.of("same_symbol_3_times", "same_symbols_horizontally")
        );

        assertEquals(expected, result, "");
    }

    @Test
    void testCheckWinningCombinationsWithBonusSymbol() {
        String[][] matrix = {
                {"A", "A", "A"},
                {"B", "+500", "C"},
                {"D", "E", "F"}
        };

        var config = new GameConfig();
        config.setWinCombinations(Map.of(
                "same_symbol_3_times",
                new GameConfig.WinCombination(
                        1,
                        "same_symbols",
                        3,
                        "same_symbols",
                        null
                ),
                "same_symbols_horizontally",
                new GameConfig.WinCombination(
                        2,
                        "linear_symbols",
                        null,
                        "horizontally_linear_symbols",
                        List.of(List.of("0:0", "0:1", "0:2"), List.of("1:0", "1:1", "1:2"), List.of("2:0", "2:1", "2:2")))
        ));

        var processor = new GameProcessor();

        var result = processor.checkWinningCombinations(matrix, config);
        var expected = Map.of("A", List.of("same_symbol_3_times", "same_symbols_horizontally"));

        assertEquals(expected, result, "");
    }

}
