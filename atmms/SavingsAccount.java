package atmms;

public class SavingsAccount extends Account {
	SavingsAccount(String accNo, String name, String pin, String phone, String cardNo, double balance) {
		super(accNo, name, pin, phone, cardNo, balance);
	}

	public String getAccountType() {
		return "Savings";
	}

	public void withdraw(double amount) throws Exception {
		if (amount <= 0) {
			throw new Exception("❌ Amount must be greater than 0.");
		}

		if (amount > 40000) {
			throw new Exception("❌ Maximum withdrawal limit is Rs.40000.");
		}

		if (balance - amount < 1000) {
			throw new Exception("❌ Savings Account must maintain minimum balance of Rs.1000.");
		}

		balance = balance - amount;
	}
}