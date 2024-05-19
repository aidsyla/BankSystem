package models;

import enums.TransactionType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static models.Transaction.*;

public class Account {
    public static final BigDecimal PERCENTAGE_FEE1 = new BigDecimal("0.035");
    public static final BigDecimal PERCENTAGE_FEE2 = new BigDecimal("0.025");

    private int id;
    private String username;
    private BigDecimal balance;
    private List<Transaction> transactions;
    private Bank bank;

    public Account(int id, String username, BigDecimal balance) {
        this.id = id;
        this.username = username;
        this.balance = formatNumber(balance);
    }

    public Account(int id, String username, BigDecimal balance, List<Transaction> transactions, Bank bank) {
        this.id = id;
        this.username = username;
        this.balance = balance;
        this.transactions = transactions;
        this.bank = bank;
    }

    public static void checkBalance(Account loggedInAccount, Scanner sc) {
        System.out.println("Your balance is: " + loggedInAccount.getBalanceString());
        System.out.println("Press Enter to go back");
        sc.nextLine();
        sc.nextLine();
    }
    public static void withdrawMoney(Account loggedInAccount, List<Transaction> transactions, Scanner sc) {
        System.out.println("Current balance: $" + formatNumber(loggedInAccount.getBalance()));
        System.out.println("Enter amount you want to withdraw");
        BigDecimal withdrawAmount = formatNumber(sc.nextBigDecimal());
        if (withdrawAmount.compareTo(loggedInAccount.getBalance()) < 0) {
            BigDecimal previewWithdraw = formatNumber(loggedInAccount.getBalance().subtract(withdrawAmount));
            System.out.println("Balance after withdrawal excluding fees $" + previewWithdraw);
            System.out.println("Balance after $5 fee: $" + previewWithdraw.subtract(BigDecimal.valueOf(5.00)));
            System.out.println("Are you sure you want to withdraw: $" + withdrawAmount + ", Yes/No");
            if (sc.next().equalsIgnoreCase("yes")) {
                loggedInAccount.setBalance(loggedInAccount.getBalance().subtract(withdrawAmount));
                flatFee(loggedInAccount);
                System.out.println("Withdrew $" + formatNumber(withdrawAmount) + " successfully");
                System.out.println("Now your balance is: " + loggedInAccount.getBalanceString());
                loggedInAccount.getBank().setTotalTransferAmount(loggedInAccount.getBank().getTotalTransferAmount().add(withdrawAmount));
                loggedInAccount.getBank().setTotalTransactionFeeAmount(loggedInAccount.getBank().getTotalTransactionFeeAmount().add(BigDecimal.valueOf(5.00)));
                transactions.add(new Transaction(withdrawAmount, loggedInAccount.getId(), loggedInAccount.getId(), "Other", TransactionType.WITHDRAWAL));
            }
            System.out.println("Press enter to go back");
            sc.nextLine();
            sc.nextLine();
        } else {
            System.out.println("You don't have enough funds, you have: " + loggedInAccount.getBalanceString());
        }
    }

    public Bank getBank(){
        return bank;
    }

    public static void depositMoney(Account loggedInAccount, List<Transaction> transactions, Scanner sc) {
        System.out.println("Current balance: $" + formatNumber(loggedInAccount.getBalance()));
        System.out.println("Enter amount you want to deposit");
        BigDecimal depositAmount = formatNumber(sc.nextBigDecimal());
        if (depositAmount.compareTo(BigDecimal.ZERO) >= 1) {
            System.out.println("Your balance after the deposit, including $5 fee will be: $" + formatNumber(loggedInAccount.getBalance().add(depositAmount).subtract(BigDecimal.valueOf(5.00))));
            System.out.println("Proceed with deposit? Yes/No");
            if (sc.next().equalsIgnoreCase("yes")) {
                loggedInAccount.setBalance(loggedInAccount.getBalance().add(depositAmount));
                flatFee(loggedInAccount);
                System.out.println("Your balance now is: $" + formatNumber(loggedInAccount.getBalance()));
                loggedInAccount.getBank().setTotalTransferAmount(loggedInAccount.getBank().getTotalTransferAmount().add(depositAmount));
                loggedInAccount.getBank().setTotalTransactionFeeAmount(loggedInAccount.getBank().getTotalTransactionFeeAmount().add(BigDecimal.valueOf(5.00)));
                transactions.add(new Transaction(depositAmount, loggedInAccount.getId(), loggedInAccount.getId(), "Other", TransactionType.DEPOSIT));
            }
            System.out.println("Press enter to go back");
            sc.nextLine();
            sc.nextLine();
        } else {
            System.out.println("You can't add 0 or negative numbers");
        }
    }

    public static void transferTo(List<Account> accounts, Account loggedInAccount, List<Transaction> transactions, Scanner sc) {
        System.out.println("Select which account you want to transfer to");

        List<Account> everyoneButTheOneLoggedIn = accounts.stream()
                .filter(p -> !p.getUsername().equalsIgnoreCase(loggedInAccount.getUsername())).toList();
        everyoneButTheOneLoggedIn.forEach(System.out::println);
        System.out.println("Type the id or the username of the person you want to transfer to");
        String input = sc.next();
        if (isNumeric(input)) {
            int id = Integer.parseInt(input);
            Account receivingAccount = findAccountById(accounts, id);
            transferMoney(loggedInAccount, sc, receivingAccount, transactions);
        } else {
            Account receivingAccount = findAccountByUsername(accounts, input);
            transferMoney(loggedInAccount, sc, receivingAccount, transactions);
        }
    }

    public static void transferMoney(Account loggedInAccount, Scanner sc, Account receivingAccount, List<Transaction> transactions) {
        if (receivingAccount == null) {
            System.out.println("Account does not exist");
        } else {
            System.out.println("You currently have: $" + formatNumber(loggedInAccount.getBalance()));
            System.out.println("Enter amount you want to transfer");
            BigDecimal transferAmount = sc.nextBigDecimal();
            if (loggedInAccount.getBalance().compareTo(transferAmount) >= 1) {
                BigDecimal previewTransfer = loggedInAccount.getBalance().subtract(transferAmount);
                BigDecimal feeValue = calculateFee(transferAmount, PERCENTAGE_FEE1, PERCENTAGE_FEE2);
                System.out.println("You want to transfer: $" + formatNumber(transferAmount) + " to " +  receivingAccount +
                        ".");
                System.out.println("Your balance after the transfer excluding fees: $" + formatNumber(previewTransfer));
                System.out.println("Fees: $" + formatNumber(feeValue));
                System.out.println("Final Balance: $" + formatNumber(previewTransfer.subtract(feeValue)));
                System.out.println("Proceed with transfer? Yes/No");
                if (sc.next().equalsIgnoreCase("yes")) {
                    percentageFee(loggedInAccount, transferAmount, PERCENTAGE_FEE1, PERCENTAGE_FEE2);
                    receivingAccount.setBalance(receivingAccount.getBalance().add(transferAmount));
                    loggedInAccount.getBank().setTotalTransferAmount(loggedInAccount.getBank().getTotalTransferAmount().add(transferAmount));
                    loggedInAccount.getBank().setTotalTransactionFeeAmount(loggedInAccount.getBank().getTotalTransactionFeeAmount().add(feeValue));
                    transactions.add(new Transaction(transferAmount, loggedInAccount.getId(), receivingAccount.getId(), "Other", TransactionType.TRANSFER));
                    System.out.println("Transferred $" + transferAmount + " successfully, you now have: " + loggedInAccount.getBalanceString());
                } else if (sc.next().equalsIgnoreCase("no")) {
                    System.out.println("Press enter to go back");
                    sc.nextLine();
                    sc.nextLine();
                }
            } else {
                System.out.println("You don't have enough funds to transfer");
            }
        }
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public static Account findAccountByUsername(List<Account> accounts, String username) {
        for (Account account : accounts) {
            if (account.getUsername().equalsIgnoreCase(username)) {
                return account;
            }
        }
        return null;
    }

    public static Account findAccountById(List<Account> accounts, int id) {
        for (Account account : accounts) {
            if (account.getId() == id) {
                return account;
            }
        }
        return null;
    }

    public static void transactionHistory(Account loggedInAccount, List<Transaction> transactions, Scanner sc) {
        System.out.println("Here you can see your transaction history");
        List<Transaction> transactionList = transactions.stream()
                .filter(p -> p.getOriginatingAccountId() == loggedInAccount.getId()).toList();
        transactionList.forEach(System.out::println);
        System.out.println("Press enter to exit");
        sc.nextLine();
        sc.nextLine();
    }

    public static void loginScreen(List<Account> accounts, Account loggedInAccount) {
        List<Transaction> transactions = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        boolean loggedIn = true;
        while (loggedIn) {

            System.out.println("Welcome to our home page");
            System.out.println("1. Check balance");
            System.out.println("2. Withdraw money");
            System.out.println("3. Deposit money");
            System.out.println("4. Transfer to another account");
            System.out.println("5. See transaction history");
            System.out.println("6. Log out");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    checkBalance(loggedInAccount, sc);
                    break;
                case 2:
                    withdrawMoney(loggedInAccount, transactions, sc);
                    break;
                case 3:
                    depositMoney(loggedInAccount, transactions, sc);
                    break;
                case 4:
                    transferTo(accounts, loggedInAccount, transactions, sc);
                    break;
                case 5:
                    transactionHistory(loggedInAccount, transactions, sc);
                    break;
                case 6:
                    System.out.println("Logged out");
                    loggedIn = false;
            }
        }
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getBalance() {
        return formatNumber(balance);
    }

    public void setBalance(BigDecimal balance) {
        this.balance = formatNumber(balance);
    }

    public String getBalanceString() {
        return "$" + balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", balance=" + balance +
                ", transactions=" + transactions +
                ", bank=" + (bank != null ? bank.getName() : "null") +
                '}';
    }

    public static BigDecimal formatNumber(BigDecimal amount){
        return amount.setScale(2, RoundingMode.HALF_DOWN);
    }
}