package atmms;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ATMDemo {
	static Scanner sc = new Scanner(System.in);

	static Account[] acc = new Account[100];

	static int count = 0;

	static Account currentAccount = null;

	static String accountFile = "accounts.txt";

	static String transactionFile = "transactions.txt";

	public static Account findAccount(String accNo) {
		for (int i = 0; i < count; i++) {
			if (acc[i].accNo.equals(accNo)) {
				return acc[i];
			}
		}

		return null;
	}

	public static Account findCard(String cardNo) {
		for (int i = 0; i < count; i++) {
			if (acc[i].cardNo.equals(cardNo)) {
				return acc[i];
			}
		}

		return null;
	}

	public static void displayAccount(Account account) {
		System.out.println("📋 ------------------------------");

		System.out.println("🏦 Account Type: " + account.getAccountType());

		System.out.println("🔢 Account Number: " + account.accNo);

		System.out.println("👤 Name: " + account.name);

		System.out.println("🔐 PIN: " + account.pin);

		System.out.println("📱 Phone Number: " + account.phone);

		System.out.println("💳 Card Number: " + account.cardNo);

		System.out.println("💰 Balance: Rs." + account.balance);

		System.out.println("📋 ------------------------------");
	}

	public static void newline(FileWriter fw) throws IOException {
		fw.write("\n");
	}

	public static void saveAccounts() {
		try {
			FileWriter fw = new FileWriter(accountFile);

			for (int i = 0; i < count; i++) {
				Account a = acc[i];

				fw.write("Account Type: " + a.getAccountType());

				newline(fw);

				fw.write("Account Number: " + a.accNo);

				newline(fw);

				fw.write("Name: " + a.name);

				newline(fw);

				fw.write("PIN: " + a.pin);

				newline(fw);

				fw.write("Phone Number: " + a.phone);

				newline(fw);

				fw.write("Card Number: " + a.cardNo);

				newline(fw);

				fw.write("Balance: Rs." + a.balance);

				newline(fw);

				newline(fw);
			}

			fw.close();
		} catch (Exception e) {
			System.out.println("❌ Exception: " + e.getMessage());
		}
	}

	public static void loadAccounts() {
		try {
			File file = new File(accountFile);

			if (!file.exists()) {
				return;
			}

			BufferedReader br = new BufferedReader(new FileReader(file));

			String line;

			String type = "";
			String accNo = "";
			String name = "";
			String pin = "";
			String phone = "";
			String cardNo = "";
			double balance = 0;

			while ((line = br.readLine()) != null) {
				if (line.startsWith("Account Type: ")) {
					type = line.substring(14);
				} else if (line.startsWith("Account Number: ")) {
					accNo = line.substring(16);
				} else if (line.startsWith("Name: ")) {
					name = line.substring(6);
				} else if (line.startsWith("PIN: ")) {
					pin = line.substring(5);
				} else if (line.startsWith("Phone Number: ")) {
					phone = line.substring(14);
				} else if (line.startsWith("Card Number: ")) {
					cardNo = line.substring(13);
				} else if (line.startsWith("Balance: Rs.")) {
					balance = Double.parseDouble(line.substring(12));

					if (type.equals("Savings")) {
						acc[count] = new SavingsAccount(accNo, name, pin, phone, cardNo, balance);
					} else {
						acc[count] = new CurrentAccount(accNo, name, pin, phone, cardNo, balance);
					}

					count++;
				}
			}

			br.close();
		} catch (Exception e) {
			System.out.println("❌ Exception: " + e.getMessage());
		}
	}

	public static void saveTransaction(String type, String accountNo, double amount) {
		try {
			FileWriter fw = new FileWriter(transactionFile, true);

			LocalDateTime now = LocalDateTime.now();

			String date = now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

			String time = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

			fw.write("Transaction Type: " + type);

			newline(fw);

			fw.write("Amount: Rs." + amount);

			newline(fw);

			fw.write("Account Number: " + accountNo);

			newline(fw);

			fw.write("Date: " + date);

			newline(fw);

			fw.write("Time: " + time);

			newline(fw);

			newline(fw);

			fw.close();
		} catch (Exception e) {
			System.out.println("❌ Exception: " + e.getMessage());
		}
	}

	public static void saveTransfer(String sender, String receiver, double amount) {
		try {
			FileWriter fw = new FileWriter(transactionFile, true);

			LocalDateTime now = LocalDateTime.now();

			String date = now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

			String time = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

			fw.write("Transaction Type: Transfer");

			newline(fw);

			fw.write("Amount: Rs." + amount);

			newline(fw);

			fw.write("Sender Account Number: " + sender);

			newline(fw);

			fw.write("Receiver Account Number: " + receiver);

			newline(fw);

			fw.write("Date and Time: " + date  + time);

			newline(fw);

			newline(fw);

			newline(fw);

			fw.close();
		} catch (Exception e) {
			System.out.println("❌ Exception: " + e.getMessage());
		}
	}

	public static int getTodayWithdrawals(String accountNo) {
		int countToday = 0;

		try {
			File file = new File(transactionFile);

			if (!file.exists()) {
				return 0;
			}

			BufferedReader br = new BufferedReader(new FileReader(file));

			String line;

			String transaction = "";

			String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

			while ((line = br.readLine()) != null) {
				if (line.trim().equals("")) {
					if (transaction.contains("Transaction Type: Withdraw")
							&& transaction.contains("Account Number: " + accountNo)
							&& transaction.contains("Date: " + today)) {
						countToday++;
					}

					if (transaction.contains("Transaction Type: Transfer")
							&& transaction.contains("Sender Account Number: " + accountNo)
							&& transaction.contains("Date: " + today)) {
						countToday++;
					}

					transaction = "";
				} else {
					if (transaction.equals("")) {
						transaction = line;
					} else {
						transaction = transaction + "\n" + line;
					}
				}
			}

			br.close();
		} catch (Exception e) {
			System.out.println("❌ Exception: " + e.getMessage());
		}

		return countToday;
	}

	public static void viewTransactions(String accountNo) {
		try {
			File file = new File(transactionFile);

			if (!file.exists()) {
				System.out.println("📭 No transactions found.");

				return;
			}

			BufferedReader br = new BufferedReader(new FileReader(file));

			String line;

			String transaction = "";

			boolean found = false;

			System.out.println("\n📜 ===== TRANSACTION HISTORY =====");

			while ((line = br.readLine()) != null) {
				if (line.trim().equals("")) {
					if (transaction.contains("Account Number: " + accountNo)
							|| transaction.contains("Sender Account Number: " + accountNo)
							|| transaction.contains("Receiver Account Number: " + accountNo)) {
						System.out.println(transaction);

						System.out.println();

						found = true;
					}

					transaction = "";
				} else {
					if (transaction.equals("")) {
						transaction = line;
					} else {
						transaction = transaction + "\n" + line;
					}
				}
			}

			br.close();

			if (!found) {
				System.out.println("📭 No transactions found for this account.");
			}
		} catch (Exception e) {
			System.out.println("❌ Exception: " + e.getMessage());
		}
	}

	static void enterCardNumber() {
		while (true) {
			try {
				System.out.print("💳 Enter Card Number: ");

				String cardNo = sc.next();

				if (!cardNo.matches("\\d{16}")) {
					throw new Exception("Card Number must contain exactly 16 digits.");
				}

				Account account = findCard(cardNo);

				if (account == null) {
					throw new Exception("Wrong Card Number.");
				}

				currentAccount = account;

				System.out.println("✅ Card Number accepted.");

				System.out.println("------------------------------");

				System.out.println("👋 Welcome back " + account.name);

				User.user(sc, account);

				return;
			} catch (Exception e) {
				System.out.println("❌ Exception: " + e.getMessage());

				System.out.println("🔁 Please enter Card Number again.");
			}
		}
	}

	static void removeCard() {
		if (currentAccount == null) {
			System.out.println("⚠️ Please enter Card Number first.");

			return;
		}

		for (int i = 0; i < count; i++) {
			if (acc[i] == currentAccount) {
				for (int j = i; j < count - 1; j++) {
					acc[j] = acc[j + 1];
				}

				acc[count - 1] = null;

				count--;

				saveAccounts();

				currentAccount = null;

				System.out.println("✅ Card removed successfully.");

				System.out.println("👋 Thank you for using the HDFC-ATM.");

				sc.close();

				System.exit(0);
			}
		}
	}

	public static void main(String[] args) {
		loadAccounts();

		while (true) {
			try {
				System.out.println("\n🏦 ===== WELCOME TO HDFC-ATM =====");

				System.out.println("💳 1. Enter Card Number");

				System.out.println("❌ 2. Remove Card");

				System.out.print("👉 Enter: ");

				int choice = sc.nextInt();

				switch (choice) {
				case 1:

					enterCardNumber();

					break;

				case 2:

					removeCard();

					break;

				default:

					System.out.println("⚠️ Invalid choice.");
				}
			} catch (Exception e) {
				System.out.println("❌ Exception: Please enter a valid input.");

				sc.nextLine();
			}
		}
	}
}