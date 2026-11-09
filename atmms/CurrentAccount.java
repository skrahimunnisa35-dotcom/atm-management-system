package atmms;

public class CurrentAccount extends Account {
	CurrentAccount(String accNo, String name, String pin, String phone, String cardNo, double balance) {
		super(accNo, name, pin, phone, cardNo, balance);
	}

	public String getAccountType() {
		return "Current";
	}

	public void withdraw(double amount) throws Exception {
		if (amount <= 0) {
			throw new Exception("❌ Amount must be greater than 0.");
		}

		if (amount > 40000) {
			throw new Exception("❌ Maximum withdrawal limit is Rs.40000.");
		}

		if (balance - amount < 5000) {
			throw new Exception("❌ Current Account must maintain minimum balance of Rs.5000.");
		}

		balance = balance - amount;
	}
}