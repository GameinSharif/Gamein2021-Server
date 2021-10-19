package ir.sharif.gamein2021.core.util;

public class Enums
{
    public enum Country
    {
        France,
        Germany,
        UnitedKingdom,
        Netherlands,
        Belgium,
        Switzerland
    }

    public enum ProductType
    {
        RawMaterial,
        SemiFinished,
        Finished
    }

    public enum AuctionBidStatus
    {
        Active,
        Over
    }

    public enum ContractType
    {
        ONCE, LONGTERM
    }

    public enum NegotiationState
    {
        CLOSED, DEAL, IN_PROGRESS, PENDING
    }
}
