package org.config.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GameConfig {
    @JsonProperty("columns")
    private int columns;

    @JsonProperty("rows")
    private int rows;

    @JsonProperty("symbols")
    private Map<String, Symbol> symbols;

    @JsonProperty("probabilities")
    private Probabilities probabilities;

    @JsonProperty("win_combinations")
    private Map<String, WinCombination> winCombinations;

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public Probabilities getProbabilities() {
        return probabilities;
    }


    public Map<String, Symbol> getSymbols() {
        return symbols;
    }

    public Map<String, WinCombination> getWinCombinations() {
        return winCombinations;
    }

    public void setWinCombinations(Map<String, WinCombination> winCombinations) {
        this.winCombinations = winCombinations;
    }

    public static class Symbol {
        @JsonProperty("reward_multiplier")
        private double rewardMultiplier;

        @JsonProperty("type")
        private String type;

        @JsonProperty("impact")
        private String impact;

        @JsonProperty("extra")
        private Integer extra;

        public double getRewardMultiplier() {
            return rewardMultiplier;
        }

        public String getType() {
            return type;
        }

        public String getImpact() {
            return impact;
        }

        public Integer getExtra() {
            return extra;
        }
    }

    public static class Probabilities {

        @JsonProperty("standard_symbols")
        private List<StandardSymbol> standardSymbols;

        @JsonProperty("bonus_symbols")
        private BonusSymbol bonusSymbols;

        public List<StandardSymbol> getStandardSymbols() {
            return standardSymbols;
        }

        public void setStandardSymbols(List<StandardSymbol> standardSymbols) {
            this.standardSymbols = standardSymbols;
        }

        public BonusSymbol getBonusSymbols() {
            return bonusSymbols;
        }
    }

    public static class StandardSymbol {
        @JsonProperty("column")
        private Integer column;

        @JsonProperty("row")
        private Integer row;

        @JsonProperty("symbols")
        private Map<String, Integer> symbols;

        public Integer getColumn() {
            return column;
        }

        public Integer getRow() {
            return row;
        }

        public Map<String, Integer> getSymbols() {
            return symbols;
        }
    }

    public static class BonusSymbol {
        @JsonProperty("symbols")
        private Map<String, Integer> symbols;

        public Map<String, Integer> getSymbols() {
            return symbols;
        }

    }

    public static class WinCombination {
        @JsonProperty("reward_multiplier")
        private Integer rewardMultiplier;

        @JsonProperty("when")
        private String when;

        @JsonProperty("count")
        private Integer count;

        @JsonProperty("group")
        private String group;

        @JsonProperty("covered_areas")
        private List<List<String>> coveredAreas;


        public WinCombination(
                @JsonProperty("reward_multiplier") Integer rewardMultiplier,
                @JsonProperty("when") String when,
                @JsonProperty("count") Integer count,
                @JsonProperty("group") String group,
                @JsonProperty("covered_areas") List<List<String>> coveredAreas) {
            this.rewardMultiplier = rewardMultiplier;
            this.when = when;
            this.count = count;
            this.group = group;
            this.coveredAreas = coveredAreas;
        }

        public Integer getRewardMultiplier() {
            return rewardMultiplier;
        }

        public String getWhen() {
            return when;
        }

        public Integer getCount() {
            return count;
        }

        public String getGroup() {
            return group;
        }

        public List<List<String>> getCoveredAreas() {
            return coveredAreas;
        }

    }

}