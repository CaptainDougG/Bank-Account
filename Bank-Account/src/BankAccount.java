import java.time.*;
import java.util.ArrayList;

public class BankAccount {

	//Variables
	String accountName;
	double balance;
	double withdrawalFee;
	double annualInterestRate;
	int accountID;
	static int nextAccountID = 1000000;
	ArrayList <Transaction> transactions = new ArrayList <Transaction>();


	//Constructors
	public BankAccount() {
		this("");
	}

	public BankAccount(String accountName) {
		this(accountName, 0);
	}

	public BankAccount(String accountName, double balance) {
		this(accountName, balance, 0);
	}

	public BankAccount(String accountName, double balance, double withdrawalFee) {
		this(accountName, balance, withdrawalFee, 0);
	}

	public BankAccount(String accountName, double balance, double withdrawalFee,double annualInterestRate) {
		if (accountName.length() <= 20) {
			this.accountName = accountName;
		}
		else {
			this.accountName = accountName.substring(0, 20);
		}
		this.balance = balance;
		this.withdrawalFee = withdrawalFee;
		this.annualInterestRate = annualInterestRate;
		this.accountID = nextAccountID;
		nextAccountID = nextAccountID + 1;
	}


	//Accessors
	public String getAccountName() {
		return accountName;
	}

	public double getBalance() {
		return balance;
	}

	public double getWithdrawalFee() {
		return withdrawalFee;
	}

	public double getAnnualInterestRate() {
		return annualInterestRate;
	}

	public int getAccountID() {
		return accountID;
	}

	public static int getNextAccountID() {
		return nextAccountID;
	}

	//Mutators
	public void setWithdrawalFee(double withdrawalFee) {
		this.withdrawalFee = withdrawalFee;
	}

	public void setAnnualInterestRate(double annualInterestRate) {
		this.annualInterestRate = annualInterestRate;
	}


	//Methods
	public void deposit() {
		this.deposit(0);
	}

	public void deposit(double amount) {
		this.deposit(amount,"");
	}

	public void deposit(double amount, String description) {
		this.deposit(LocalDateTime.now(),amount,"");
	}

	public void deposit(LocalDateTime transactionTime, double amount, String description) {
		balance = balance + amount;
		Transaction deposit = new Transaction(transactionTime, amount, description);
		insertTransaction(deposit);
	}


	public void withdraw() {
		this.withdraw(0);
	}

	public void withdraw(double amount) {
		this.withdraw(amount,"");
	}

	public void withdraw(double amount, String description) {
		this.withdraw(LocalDateTime.now(),amount,"");
	}

	public void withdraw(LocalDateTime transactionTime, double amount, String description) {
		balance = (balance - amount) - withdrawalFee;
		Transaction withdrawl = new Transaction(transactionTime, amount, description);
		insertTransaction(withdrawl);		
	}

	public void insertTransaction(Transaction transaction) {
		boolean inserted = false;
		for(int i = 0; i < transactions.size(); i++) { 
			if(transactions.get(i).getTransactionTime().isAfter(transaction.getTransactionTime())) {
				transactions.add(i, transaction);
				inserted = true;
				break;
			}
		}
		if (inserted == false) {
			transactions.add(transaction);
		}
	}


	public boolean isOverDrawn() {
		if (balance < 0) {
			return true;
		}
		else {
			return false;
		}
	}

	public double addAnnualInterest() {
		if (balance > 0) {
			balance = balance + ((balance * annualInterestRate) / 100);
		}
		return balance;
	}

	public String toString() {
		if (isOverDrawn() == false) {
			return String.format ("BankAccount: name = '" + accountName  + "'; "+ "balance = $" + "%.2f",balance);
		}
		else {
			return String.format ("BankAccount: name = '" + accountName  + "'; "+ "balance = ($" + "%.2f" + ")",(balance * (-1)));
		}
	}

	public ArrayList <Transaction> getTransactions(LocalDateTime startTime, LocalDateTime endTime){

		ArrayList<Transaction> range = new ArrayList<Transaction>();

		for(int i = 0; i < transactions.size(); i++) {
			boolean inLowerBound = (startTime == null || transactions.get(i).getTransactionTime().isBefore(startTime) == false);
			boolean inUperBound = (endTime == null || transactions.get(i).getTransactionTime().isAfter(endTime) == false);
			
			if (inLowerBound == true && inUperBound == true) {
				range.add(transactions.get(i));
			}
		}

		return range;
	}
}

