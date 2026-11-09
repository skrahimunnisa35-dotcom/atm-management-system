package atmms;

import java.util.Scanner;

public class User {
	public static void user(Scanner sc, Account account) {
		while (true) {
			try {
				System.out.println("\n👤 ===== ATM menu =====");

				System.out.println("💰 1. Deposit");

				System.out.println("💸 2. Withdraw");

				System.out.println("💳 3. Check Balance");

				System.out.println("🔄 4. Fund Transfer");

				System.out.println("📜 5. Transaction History");

				System.out.println("🔐 6. Change PIN");

				System.out.println("⬅️ 7. Back");

				System.out.print("👉 Enter: ");

				int choice = sc.nextInt();

				switch (choice) {
				case 1:

					checkAccountType(sc, account);

					verifyPin(sc, account);

					System.out.print("💰 Enter Deposit Amount: Rs.");

					double deposit = sc.nextDouble();

					account.deposit(deposit);

					ATMDemo.saveAccounts();

					ATMDemo.saveTransaction("Deposit", account.accNo, deposit);

					System.out.println("✅ Deposit successful.");

					account.checkBalance();

					break;

				case 2:

					checkAccountType(sc, account);

					verifyPin(sc, account);

					withdraw(sc, account);

					break;

				case 3:

					verifyPin(sc, account);

					account.checkBalance();

					break;

				case 4:

					transfer(sc, account);

					break;

				case 5:

					ATMDemo.viewTransactions(account.accNo);

					break;

				case 6:

					changePin(sc, account);

					break;

				case 7:

					return;

				default:

					System.out.println("⚠️ Invalid choice.");
				}
			} catch (Exception e) {
				System.out.println("❌ Exception: " + e.getMessage());

				sc.nextLine();
			}
		}
	}

	static void checkAccountType(Scanner sc, Account account) {
		while (true) {
			try {
				System.out.println("🏦 1. Savings Account");

				System.out.println("🏦 2. Current Account");

				System.out.print("👉 Enter: ");

				int choice = sc.nextInt();

				if (choice == 1) {
					if (!account.getAccountType().equals("Savings")) {
						throw new Exception("Account type does not match.");
					}

					return;
				}

				if (choice == 2) {
					if (!account.getAccountType().equals("Current")) {
						throw new Exception("Account type does not match.");
					}

					return;
				}

				throw new Exception("Invalid account type.");
			} catch (Exception e) {
				System.out.println("❌ Exception: " + e.getMessage());
			}
		}
	}

	static void verifyPin(Scanner sc, Account account) {
		while (true) {
			try {
				System.out.print("🔐 Enter PIN: ");

				String pin = sc.next();

				if (!pin.matches("\\d{6}")) {
					throw new Exception("PIN must contain exactly 6 digits.");
				}

				if (!pin.equals(account.pin)) {
					throw new Exception("Wrong PIN.");
				}

				return;
			} catch (Exception e) {
				System.out.println("❌ Exception: " + e.getMessage());

				System.out.println("🔁 Please enter PIN again.");
			}
		}
	}

	static void withdraw(Scanner sc, Account account) {
		while (true) {
			try {
				int used = ATMDemo.getTodayWithdrawals(account.accNo);

				int remaining = 3 - used;

				if (remaining <= 0) {
					throw new Exception("Daily withdrawal limit of 3 has been reached.");
				}

				System.out.println("🔢 Withdrawals remaining today: " + remaining);

				System.out.print("💸 Enter Withdraw Amount: Rs.");

				double amount = sc.nextDouble();

				account.withdraw(amount);

				ATMDemo.saveAccounts();

				ATMDemo.saveTransaction("Withdraw", account.accNo, amount);

				int left = remaining - 1;

				System.out.println("✅ Withdraw successful.");

				account.checkBalance();

				System.out.println("🔢 Withdrawals remaining today: " + left);

				break;
			} catch (Exception e) {
				System.out.println("❌ Exception: " + e.getMessage());

				int used = ATMDemo.getTodayWithdrawals(account.accNo);

				System.out.println("🔢 Withdrawals remaining today: " + (3 - used));

				break;
			}
		}
	}

	static void transfer(Scanner sc, Account from) {
		verifyPin(sc, from);

		Account to;

		while (true) {
			try {
				System.out.print("🔢 Enter Receiver Account Number: ");

				String toNo = sc.next();

				if (!toNo.matches("\\d{12}")) {
					throw new Exception("Account Number must contain exactly 12 digits.");
				}

				to = ATMDemo.findAccount(toNo);

				if (to == null) {
					throw new Exception("Receiver Account Number not found.");
				}

				if (from.accNo.equals(to.accNo)) {
					throw new Exception("Sender and Receiver cannot be same.");
				}

				break;
			} catch (Exception e) {
				System.out.println("❌ Exception: " + e.getMessage());

				System.out.println("🔁 Please enter Receiver Account Number again.");
			}
		}

		while (true) {
			try {
				int used = ATMDemo.getTodayWithdrawals(from.accNo);

				if (used >= 3) {
					throw new Exception("Daily withdrawal limit of 3 has been reached.");
				}

				System.out.println("🔢 Withdrawals remaining today: " + (3 - used));

				System.out.print("💸 Enter Transfer Amount: Rs.");

				double amount = sc.nextDouble();

				from.withdraw(amount);

				to.deposit(amount);

				ATMDemo.saveAccounts();

				ATMDemo.saveTransfer(from.accNo, to.accNo, amount);

				System.out.println("✅ Fund transfer successful.");

				System.out.println("🔢 Withdrawals remaining today: " + (2 - used));

				break;
			} catch (Exception e) {
				System.out.println("❌ Exception: " + e.getMessage());

				break;
			}
		}
	}

	static void changePin(Scanner sc, Account account) {
		while (true) {
			try {
				System.out.print("🔐 Enter Old PIN: ");

				String oldPin = sc.next();

				if (!oldPin.matches("\\d{6}")) {
					throw new Exception("PIN must contain exactly 6 digits.");
				}

				if (!oldPin.equals(account.pin)) {
					throw new Exception("Wrong PIN.");
				}

				System.out.print("🔑 Enter New 6-digit PIN: ");

				String newPin = sc.next();

				if (!newPin.matches("\\d{6}")) {
					throw new Exception("New PIN must contain exactly 6 digits.");
				}

				System.out.print("🔁 Confirm New PIN: ");

				String confirmPin = sc.next();

				if (!newPin.equals(confirmPin)) {
					throw new Exception("New PIN and Confirm PIN do not match.");
				}

				account.pin = newPin;

				ATMDemo.saveAccounts();

				System.out.println("✅ PIN changed successfully.");

				break;
			} catch (Exception e) {
				System.out.println("❌ Exception: " + e.getMessage());
			}
		}
	}
}