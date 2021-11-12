package ir.sharif.gamein2021.core.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class GameConstants {
    public static GameConstants Instance = new GameConstants();

    public static final int ChatMaxMessagesCount = 20;

    public static final LocalDate startDate = LocalDate.of(2000,1, 1);

    public final int AuctionStartValue = 1000;
    public final int AuctionInitialStepValue = 100;
    public final int AuctionRoundDurationSeconds = 180;
    public final LocalDateTime[] AuctionRoundsStartTime = new LocalDateTime[]{
            LocalDateTime.of(2021, 11, 6, 22, 10, 0),
            LocalDateTime.of(2021, 11, 6, 22, 13, 0),
            LocalDateTime.of(2021, 11, 6, 22, 16, 0),
    };
    public final int rawMaterialCapacity = 2000000;
    public final int semiFinishedProductCapacity = 2000000;
    public final int finishedProductCapacity = 2000000;

    public static final float CrushProbability = 0.01f;

    public static final int AuctionRoundLengthInSeconds = 300;

    public static final int DistanceConstant = 100;

    public static GameConstants getInstance() {
        return Instance;
    }
    public static boolean IsGameStarted = true;
}
