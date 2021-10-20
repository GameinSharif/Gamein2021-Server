package ir.sharif.gamein2021.core.util;

import java.time.LocalDateTime;

public class GameConstants
{
    public static GameConstants Instance = new GameConstants();

    public static final int ChatMaxMessagesCount = 2;

    public final int AuctionStartValue = 1000;
    public final int AuctionStepValue = 100;
    public final LocalDateTime[] AuctionRoundsStartTime = new LocalDateTime[]{
            LocalDateTime.of(2021, 11, 25, 9, 0, 0),
            LocalDateTime.of(2021, 11, 25, 9, 5, 0),
            LocalDateTime.of(2021, 11, 25, 9, 10, 0)
    };
    public static final int AuctionRoundLengthInSeconds = 300;
}