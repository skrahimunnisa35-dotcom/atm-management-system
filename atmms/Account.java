package atmms;

public abstract class Account {
	String accNo;
	String name;
	String pin;
	String phone;
	String cardNo;
	double balance;

	Account(String accNo, String name, String pin, String phone, String cardNo, double balance) {
		this.accNo = accNo;
		this.name = name;
		this.pin = pin;
		this.phone = phone;
		this.cardNo = cardNo;
		this.balance = balance;
	}

	public abstract String getAccountType();

	public abstract void withdraw(double amount) throws Exception;

	public void deposit(double amount) throws Exception {
		if (amount <= 0) {
			throw new Exception("❌ Amount must be greater than 0.");
		}
		

		balance = balance + amount;
	}

	public void checkBalance() {
		System.out.println("💰 Current Balance: Rs." + balance);
	}
}