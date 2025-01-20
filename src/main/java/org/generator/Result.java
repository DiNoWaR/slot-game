package org.generator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result {
    @JsonProperty("matrix")
    private String[][] matrix;

    @JsonProperty("reward")
    private double reward;

    @JsonProperty("applied_winning_combinations")
    private Map<String, List<String>> appliedWinningCombinations = new HashMap<>();

    @JsonProperty("applied_bonus_symbols")
    private List<String> appliedBonusSymbols = new ArrayList<>();

    public void setMatrix(String[][] matrix) {
        this.matrix = matrix;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public void setAppliedWinningCombinations(Map<String, List<String>> appliedWinningCombinations) {
        this.appliedWinningCombinations = appliedWinningCombinations;
    }

    public void addBonusSymbol(String bonusSymbol) {
        this.appliedBonusSymbols.add(bonusSymbol);
    }

    public void clearBonusSymbols() {
        this.appliedBonusSymbols.clear();
    }
}

