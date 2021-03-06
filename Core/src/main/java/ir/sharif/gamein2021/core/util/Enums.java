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
        TERMINATED
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
        SUPPLIER (0),
        GAMEIN_CUSTOMER (1),
        DC (2),
        FACTORY (3);

        private int value;

        TransportNodeType(int value)
        {
            this.value = value;
        }
    }

    public enum TransportState
    {
        SUCCESSFUL, IN_WAY, CRUSHED
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

    public enum NewsType
    {
        COMMON, SERIOUS
    }
}
