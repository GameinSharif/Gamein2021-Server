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

    public enum OfferStatus {
        ACTIVE,
        ACCEPTED,
        TERMINATED,
        PASSED_DEADLINE
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
        CLOSED, DEAL, IN_PROGRESS
    }

    public enum ProductionLineStatus
    {
        ACTIVE, SCRAPPED, IN_CONSTRUCTION
    }

    public enum VehicleType
    {
        AIRPLANE, TRUCK, TRAIN, VANET
    }

    public enum TransportNodeType
    {
        SUPPLIER, GAMEIN_CUSTOMER, DC, FACTORY
    }

    public enum TransportState
    {
        SUCCESSFUL, IN_WAY, CRUSHED, PENDING, TERMINATED
    }

    public enum ProviderState
    {
        ACTIVE, TERMINATED
    }

    public enum DCType
    {
        SemiFinished, Finished
    }

    public enum StorageType
    {
        DC, FACTORY
    }
}
