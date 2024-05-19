package models;

import enums.TransactionType;

import java.math.BigDecimal;

public class Transaction {
    private BigDecimal amount;
    private int originatingAccountId;
    private int resultingAccountId;
    private String transactionReason;
    private TransactionType transactionType;

    public Transaction(BigDecimal amount, int originatingAccountId, int resultingAccountId, String transactionReason, TransactionType transactionType) {
        this.amount = Account.formatNumber(amount);
        this.originatingAccountId = originatingAccountId;
        this.resultingAccountId = resultingAccountId;
        this.transactionReason = transactionReason;
        this.transactionType = transactionType;
    }

    public static void flatFee(Account account) {
        BigDecimal flatFee = new BigDecimal("5.00");
        account.setBalance(account.getBalance().subtract(flatFee));
    }

    public static void percentageFee(Account account, BigDecimal transferAmount, BigDecimal percentageFee1, BigDecimal percentageFee2) {
            account.setBalance(account.getBalance().subtract(transferAmount).subtract(calculateFee(transferAmount, percentageFee1, percentageFee2)));
    }

    public static BigDecimal calculateFee(BigDecimal transferAmount, BigDecimal percentageFee1, BigDecimal percentageFee2) {
        BigDecimal feeAmount = BigDecimal.ZERO;
        if (transferAmount.compareTo(BigDecimal.ZERO) > 0 && transferAmount.compareTo(BigDecimal.valueOf(5000.00)) < 0) {
            feeAmount = transferAmount.multiply(percentageFee2);
        } else if (transferAmount.compareTo(BigDecimal.valueOf(5000.00)) >= 0) {
            feeAmount = transferAmount.multiply(percentageFee1);
        }
        return feeAmount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "amount=" + amount +
                ", originatingAccountId=" + originatingAccountId +
                ", resultingAccountId=" + resultingAccountId +
                ", transactionReason='" + transactionReason + '\'' +
                ", transactionType=" + transactionType +
                '}';
    }

    public BigDecimal getAmount() {
        return Account.formatNumber(amount);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = Account.formatNumber(amount);
    }

    public int getOriginatingAccountId() {
        return originatingAccountId;
    }

    public void setOriginatingAccountId(int originatingAccountId) {
        this.originatingAccountId = originatingAccountId;
    }

    public int getResultingAccountId() {
        return resultingAccountId;
    }

    public void setResultingAccountId(int resultingAccountId) {
        this.resultingAccountId = resultingAccountId;
    }

    public String getTransactionReason() {
        return transactionReason;
    }

    public void setTransactionReason(String transactionReason) {
        this.transactionReason = transactionReason;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}