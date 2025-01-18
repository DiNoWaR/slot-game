package org.generator;

import org.config.model.GameConfig;
import org.config.parser.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameProcessorTest {

    private GameConfig config;
    private GameProcessor processor;
    private Double betAmount;

    @BeforeEach
    void setUp() {
        try {
            config = Parser.parseConfig("config.json");
            processor = new GameProcessor();
            betAmount = 1000.0;
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
    }

    @Test
    void testCheckWinningCombinationsWithSameSymbols() {
        String[][] matrix = {
                {"A", "A", "A"},
                {"B", "C", "D"},
                {"E", "F", "G"}
        };

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

        var result = processor.checkWinningCombinations(matrix, config);
        var expected = Map.of("A", List.of("same_symbol_3_times", "same_symbols_diagonally_left_to_right"));

        assertEquals(expected, result, "");
    }

    @Test
    void testCheckWinningCombinationsMultipleSymbols() {
        String[][] matrix = {
                {"A", "A", "A"},
                {"B", "B", "B"},
                {"C", "D", "E"}
        };

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

        var result = processor.checkWinningCombinations(matrix, config);
        var expected = Map.of("A", List.of("same_symbol_3_times", "same_symbols_horizontally"));

        assertEquals(expected, result, "");
    }

    @Test
    void testCheckWinningCombinationsWithFourSameSymbols() {
        String[][] matrix = {
                {"C", "F", "F"},
                {"E", "F", "F"},
                {"A", "D", "E"}
        };

        var result = processor.checkWinningCombinations(matrix, config);
        var expected = Map.of(
                "F", List.of("same_symbol_4_times")
        );

        assertEquals(expected, result, "");
    }

    @Test
    void testCheckWinningCombinationsFourDifferentSymbols() {
        String[][] matrix = {
                {"F", "D", "E"},
                {"C", "B", "E"},
                {"F", "E", "F"}
        };

        var result = processor.checkWinningCombinations(matrix, config);
        var expected = Map.of(
                "F", List.of("same_symbol_3_times"),
                "E", List.of("same_symbol_3_times")
        );

        assertEquals(expected, result, "");
    }

    @Test
    void testCheckWinningCombinationsOfThree() {
        String[][] matrix = {
                {"E", "E", "F"},
                {"F", "F", "F"},
                {"10x", "F", "F"}
        };

        var result = processor.checkWinningCombinations(matrix, config);
        var expected = Map.of("F", List.of("same_symbol_6_times", "same_symbols_horizontally", "same_symbols_vertically"));

        assertEquals(result.keySet(), expected.keySet(), "");

        for (String key : expected.keySet()) {
            assertEquals(
                    Set.copyOf(expected.get(key)),
                    Set.copyOf(result.get(key)),
                    ""
            );
        }
    }

    @Test
    void testCheckEmptyWinningCombinations() {
        String[][] matrix = {
                {"E", "C", "E"},
                {"A", "B", "A"},
                {"10x", "F", "F"}
        };

        var result = processor.checkWinningCombinations(matrix, config);
        var expected = Map.of();

        assertEquals(result.keySet(), expected.keySet(), "");
    }
}
