package ir.sharif.gamein2021.core.util;

import ir.sharif.gamein2021.core.util.models.GameStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class GameConstants {
    public static GameConstants Instance = new GameConstants();

    public static final int ChatMaxMessagesCount = 20;

    public static final LocalDate startDate = LocalDate.of(2000,1, 1);

    public static final int ConstantOneWeekSupplyPrice = 2000;
    public static final float ConstantTwoWeekSupplyPrice = 0.05f;

    public static final float ShareAllocationAlphaSoda = 0.001f;
    public static final float ShareAllocationAlphaBeer = 0.002f;
    public static final float ShareAllocationAlphaJuice = 0.002f;
    public static final float ShareAllocationAlphaEnergy = 0.003f;
    public static final float ShareAllocationBeta = 0.005f;

    public static final int brandMax = 100;
    public static final int brandMin = 1;
    public static final float brandWeeklyDecrease = -0.2f;
    public static final float brandTerminateContractPenaltyDecrease = -0.2f;
    public static final float brandLostSaleContractPenaltyDecrease = -0.2f;
    public static final float brandIncreaseAfterDeal = 0.05f;
    public static final float brandIncreaseAfterFinalizeContractWithCustomer = 0.05f;

    public static final float terminatePenalty = 0.49f;
    public static final float lostSalePenalty = 0.51f;
    public static final float noMoneyPenalty = 0.51f;

    public final int AuctionStartValue = 1000;
    public final int AuctionInitialStepValue = 100;
    public final int AuctionRoundDurationSeconds = 180;
    public final LocalDateTime[] AuctionRoundsStartTime = new LocalDateTime[]{
            LocalDateTime.of(2021, 12, 12, 14, 30, 0),
            LocalDateTime.of(2021, 12, 12, 14, 33, 0),
            LocalDateTime.of(2021, 12, 12, 14, 36, 0),
    };
    public final int rawMaterialCapacity = 2000000;
    public final int semiFinishedProductCapacity = 2000000;
    public final int finishedProductCapacity = 2000000;
    public final float insuranceCostFactor = 0.05f;
    public final int distanceConstant = 100;

    public static final float CrushProbability = 0.01f;

    public static GameStatus gameStatus = GameStatus.RUNNING;

    public static float getAlpha(Integer productId)
    {
        switch (productId)
        {
            case 28:
            case 29:
            case 30:
                return ShareAllocationAlphaSoda;
            case 31:
            case 32:
                return ShareAllocationAlphaBeer;
            case 33:
            case 34:
            case 35:
            case 36:
                return ShareAllocationAlphaJuice;
            case 37:
                return ShareAllocationAlphaEnergy;
        }
        return 0;
    }
}
