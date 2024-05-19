package models;

import java.math.BigDecimal;
import java.util.List;

public class Bank {
    private String name;
    private List<Account> accounts;
    private BigDecimal totalTransactionFeeAmount;
    private BigDecimal totalTransferAmount;
    private BigDecimal flatFee;
    private BigDecimal percentageFee;

    public Bank() {

    }

    @Override
    public String toString() {
        return "Bank{" +
                "name='" + name + '\'' +
                ", accounts=" + accounts +
                ", totalTransactionFeeAmount=" + totalTransactionFeeAmount +
                ", totalTransferAmount=" + totalTransferAmount +
                ", flatFee=" + flatFee +
                ", percentageFee=" + percentageFee +
                '}';
    }

    public Bank(String name, List<Account> accounts, BigDecimal totalTransactionFeeAmount, BigDecimal totalTransferAmount, BigDecimal flatFee, BigDecimal percentageFee) {
        this.name = name;
        this.accounts = accounts;
        this.totalTransactionFeeAmount = totalTransactionFeeAmount;
        this.totalTransferAmount = totalTransferAmount;
        this.flatFee = flatFee;
        this.percentageFee = percentageFee;
    }

    public static Bank findBankByName(List<Bank> banks, String name) {
        for (Bank bank : banks) {
            if (bank.getName().equalsIgnoreCase(name)) {
                return bank;
            }
        }
        return null;
    }

    public void addTransactionFee(BigDecimal fee) {
        this.totalTransactionFeeAmount = this.totalTransactionFeeAmount.add(fee);
    }

    public void addTransferAmount(BigDecimal amount) {
        this.totalTransferAmount = this.totalTransferAmount.add(amount);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public BigDecimal getTotalTransactionFeeAmount() {
        return totalTransactionFeeAmount;
    }

    public void setTotalTransactionFeeAmount(BigDecimal totalTransactionFeeAmount) {
        this.totalTransactionFeeAmount = Account.formatNumber(totalTransactionFeeAmount);
    }

    public BigDecimal getTotalTransferAmount() {
        return totalTransferAmount;
    }

    public void setTotalTransferAmount(BigDecimal totalTransferAmount) {
        this.totalTransferAmount = totalTransferAmount;
    }

    public BigDecimal getFlatFee() {
        return flatFee;
    }

    public void setFlatFee(BigDecimal flatFee) {
        this.flatFee = flatFee;
    }

    public BigDecimal getPercentFee() {
        return percentageFee;
    }

    public void setPercentFee(BigDecimal percentageFee) {
        this.percentageFee = percentageFee;
    }
}
