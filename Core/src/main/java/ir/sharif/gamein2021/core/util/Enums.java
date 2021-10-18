package ir.sharif.gamein2021.core.util;

public class Enums
{
    public enum ContractType
    {
        ONCE, LONGTERM
    }

    public enum NegotiationState
    {
        CLOSED, DEAL, IN_PROGRESS, PENDING
    }

    public enum VehicleType
    {
        AIRPLANE, TRUCK, TRAIN, VANET
    }

    public enum TransportNodeType
    {
        SUPPLIER, GAMEIN_CUSTOMER, DC, COMPANY
    }

    public enum TransportState
    {
        SUCCESSFUL, IN_WAY, CRUSHED, PENDING
    }
}
