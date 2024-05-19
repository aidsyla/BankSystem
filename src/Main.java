import models.Account;
import models.Bank;
import models.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static models.Account.findAccountByUsername;
import static models.Account.loginScreen;
import static models.Bank.findBankByName;

public class Main {
    public static void main(String[] args) {
        List<Bank> banks = new ArrayList<>();
        Bank bank = new Bank();
        List<Account> accounts = new ArrayList<>();
        List<Transaction> transactions = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {

            System.out.println("Welcome to the Bank System");
            System.out.println("1. Create Bank");
            System.out.println("2. See Banks");
            System.out.println("3. Create Account");
            System.out.println("4. Log in");
            System.out.println("5. Exit");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Enter the name of the bank");
                    String name = sc.next();
                    System.out.println("Enter total transaction fee amount");
                    BigDecimal totalFee = sc.nextBigDecimal();
                    System.out.println("Enter total transfer amount");
                    BigDecimal totalTransfer = sc.nextBigDecimal();
                    System.out.println("Enter flat fee");
                    BigDecimal flatFee = sc.nextBigDecimal();
                    System.out.println("Enter percent fee");
                    BigDecimal percentFee = sc.nextBigDecimal();
                    bank = new Bank(name, accounts, totalFee, totalTransfer, flatFee, percentFee);
                    banks.add(bank);
                    System.out.println("Bank created with details: \n" + bank);
                    break;
                case 2:
                    System.out.println("All registered banks");
                    System.out.println(banks);
                    boolean searchMode = true;
                    while (searchMode) {
                        System.out.println("1. Search Bank Accounts");
                        System.out.println("2. Exit");
                        int choice2 = sc.nextInt();
                        switch (choice2) {
                            case 1:
                                System.out.println("Enter the name of the bank");
                                String bankName = sc.next();
                                Bank foundBank = findBankByName(banks, bankName);
                                assert foundBank != null;
                                System.out.println(foundBank.getAccounts());
                                break;
                            case 2:
                                searchMode = false;
                                break;
                        }
                    }
                    break;
                case 3:
                    System.out.println("Enter your username");
                    String username = sc.next();
                    System.out.println("Enter your balance");
                    BigDecimal balance = sc.nextBigDecimal();
                    System.out.println("Enter your bank");
                    bank.setName(sc.next());
                    Account account = new Account(accounts.size() + 1, username, balance, transactions, bank);
                    accounts.add(account);
                    System.out.println("Account created with details: \n" + account);
                    break;
                case 4:
                    System.out.println("Enter your username to login");
                    String user = sc.next();
                    Account accountSearched = findAccountByUsername(accounts, user);
                    if (accountSearched == null) {
                        System.out.println("User does not exist");
                    } else {
                        System.out.println("Logged in");
                        loginScreen(accounts, accountSearched);
                    }
                    break;
                case 5:
                    exit = true;
            }
        }
    }
}