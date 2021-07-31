package ir.sharif.gamein2021.core.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int id;
    private double cash;
    private long stocksScore;
    private List<Integer> landIds = new ArrayList();
    private int loanDebt;
    private int loanPayment;
    private int rank;
    private int fundraiseShare;
    private long assetScore;
    private long productScore;
    private int loanDeadline = -1;
    private int successfulRfqs = 0;
    private int totalBoughtContracts = 0;

    public int getLoanDebt() {
        return loanDebt;
    }

    public void setLoanDebt(int loanDebt) {
        this.loanDebt = loanDebt;
    }

    public double calculateNetWorth() {
        return 0;
    }

    public void buyLand(int landId) {
    }

    public void buyMachine(int landId, MachineType machineType) {
    }

    public void MakePeer2PeerOffer() {
    }

    public void MakePeer2PeerDemand() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getLandIds() {
        return landIds;
    }

    public void setLandIds(List<Integer> landIds) {
        this.landIds = landIds;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public int getFundraiseShare() {
        return fundraiseShare;
    }

    public int getLoanPayment() {
        return loanPayment;
    }

    public void setLoanPayment(int loanPayment) {
        this.loanPayment = loanPayment;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public long getStocksScore() {
        return stocksScore;
    }

    public void setStocksScore(long stocksScore) {
        this.stocksScore = stocksScore;
    }

    public long getAssetScore() {
        return assetScore;
    }

    public void setAssetScore(long assetScore) {
        this.assetScore = assetScore;
    }

    public long getProductScore() {
        return productScore;
    }

    public void setProductScore(long productScore) {
        this.productScore = productScore;
    }

    public void setFundraiseShare(int fundraiseShare) {
        this.fundraiseShare = fundraiseShare;
    }

    public long getTotalScore() {
        return assetScore + productScore + Math.round(cash);
    }

    public int getLoanDeadline() {
        return loanDeadline;
    }

    public void setLoanDeadline(int loanDeadline) {
        this.loanDeadline = loanDeadline;
    }

    public int getSuccessfulRfqs() {
        return successfulRfqs;
    }

    public void setSuccessfulRfqs(int successfulRfqs) {
        this.successfulRfqs = successfulRfqs;
    }

    public int getTotalBoughtContracts() {
        return totalBoughtContracts;
    }

    public void setTotalBoughtContracts(int totalBoughtContracts) {
        this.totalBoughtContracts = totalBoughtContracts;
    }
}
