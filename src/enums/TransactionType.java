package enums;

public enum TransactionType {
    WITHDRAWAL("Withdrawal"),
    TRANSFER("Transfer"),
    DEPOSIT("Deposit");

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}