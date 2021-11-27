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

    public static final float ShareAllocationAlpha = 1f;
    public static final float ShareAllocationBeta = 0.01f;

    public static final int brandMax = 100;
    public static final int brandMin = 1;
    public static final float brandDailyDecrease = -0.2f;
    public static final float brandIncreaseAfterDeal = 0.05f;

    public final int AuctionStartValue = 1000;
    public final int AuctionInitialStepValue = 100;
    public final int AuctionRoundDurationSeconds = 180;
    public final LocalDateTime[] AuctionRoundsStartTime = new LocalDateTime[]{
            LocalDateTime.of(2021, 11, 13, 22, 40, 0),
            LocalDateTime.of(2021, 11, 13, 22, 43, 0),
            LocalDateTime.of(2021, 11, 13, 22, 46, 0),
    };
    public final int rawMaterialCapacity = 2000000;
    public final int semiFinishedProductCapacity = 2000000;
    public final int finishedProductCapacity = 2000000;
    public final float insuranceCostFactor = 0.05f;
    public final int distanceConstant = 100;

    public static final float CrushProbability = 0.01f;

    public static boolean IsGameStarted = true;
    public static GameStatus gameStatus = GameStatus.PAUSED;
}
