package ir.sharif.gamein2021.core.util;

public class GameConstants {
    public static final int turnTime = 1000 * 60;
    public static double loanProfit = 1.5;
    public static double[] finalProductDemandSlope = {-0.5, -0.35};
    public static double[] countryFinalProductDemandIntercept = new double[]{7000, 8000, 7400, 7200, 7600, 7800, 6000, 7000, 6400, 6200, 6600, 6800};
    public static int fundraisingPart = 10;
    public static int machineBuildTime = 5;
    public static double machineUpgradeCostCoeff = 0.4;
    public static double machineUpgradeRate = 1.5;
    public static double landSellPrice = 0.8;
    public static double rfqPenalty = 0.35;
    public static int rfqValidTime = 3;
    public static int acceptRFQWindow = 2;
    public static double warehouseMaintenanceCost = 0.001;
    public static int maxFundraisingShare = 30;
    public static int fundraisingValidTime = 5;
    public static int finalProductHighlimit = 5000;
    public static double coronaVirusCoeff = 1.5;
    public static int coronaVirusCost = 200000;
    public static int maxLand = 5;
    public static double loanLimit = 0.1;
    public static int loanMonthDuration = 6;
    public static int gameEndTurn = 720;
    public static double fundraiseMinCoeff = 0.5;
    public static double fundraiseMaxCoeff = 2;
    public static int buyProductOffset = 5;
    public static double machineSellCoeff = 0.6;
    public static int bonusMaxPrizes = 20;
    public static int bonusBuyRfqs = 5000000;
    public static int bonusSellContracts = 10;
    public static int rfqMaxDeadline = 30;
    public static double[] countryShocks = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0};

    public static void setFinalProductDemandSlope(double[] finalProductDemandSlope) {
        GameConstants.finalProductDemandSlope = finalProductDemandSlope;
    }

}
