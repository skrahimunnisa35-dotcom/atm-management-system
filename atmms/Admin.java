package atmms;

import java.io.*;
import java.util.Scanner;

public class Admin {
	static String adminFile = "Admin1.txt";

	public static void admin(Scanner sc) {
		createAdminFile();

		while (true) {
			try {
				System.out.print("🔐 Enter Admin Password: ");

				String password = sc.next();

				String savedPassword = readPassword();

				if (!password.equals(savedPassword)) {
					throw new Exception("Wrong Admin Password.");
				}

				System.out.println("✅ Admin Login Successful.");

				break;
			} catch (Exception e) {
				System.out.println("❌ Exception: " + e.getMessage());
			}
		}

		while (true) {
			try {
				System.out.println("\n👨‍💼 ===== ADMIN SYSTEM =====");

				System.out.println("➕ 1. Add Account");

				System.out.println("🔍 2. View Account");

				System.out.println("📋 3. View All Accounts");

				System.out.println("✏️ 4. Modify Account");
				

				System.out.println("🔐 5. Change Admin Password");

				System.out.println("⬅️ 6. Back");

				System.out.print("👉 Enter: ");

				int choice = sc.nextInt();

				switch (choice) {
				case 1:
					addAccount(sc);
					break;

				case 2:
					viewAccount(sc);
					break;

				case 3:
					viewAllAccounts();
					break;

				case 4:
					modifyAccount(sc);
					break;

				case 5:
					changePassword(sc);
					break;

				case 6:
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

	static void createAdminFile() {
		try {
			File file = new File(adminFile);

			if (!file.exists()) {
				FileWriter fw = new FileWriter(adminFile);

				fw.write("admin123");

				fw.close();
			}
		} catch (Exception e) {
			System.out.println("❌ Exception: " + e.getMessage());
		}
	}

	static String readPassword() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(adminFile));

			String password = br.readLine();

			br.close();

			return password;
		} catch (Exception e) {
			return "admin123";
		}
	}

	static void changePassword(Scanner sc) {
		while (true) {
			try {
				System.out.print("🔐 Enter Old Password: ");

				String oldPassword = sc.next();

				if (!oldPassword.equals(readPassword())) {
					throw new Exception("Wrong Password.");
				}

				System.out.print("🔑 Enter New Password: ");

				String newPassword = sc.next();

				FileWriter fw = new FileWriter(adminFile);

				fw.write(newPassword);

				fw.close();

				System.out.println("✅ Password changed successfully.");

				break;
			} catch (Exception e) {
				System.out.println("❌ Exception: " + e.getMessage());
			}
		}
	}

	static void addAccount(Scanner sc) {
		int type;

		while (true) {
			try {
				System.out.println("🏦 1. Savings Account");

				System.out.println("🏦 2. Current Account");

				System.out.print("👉 Enter: ");

				type = sc.nextInt();

				if (type != 1 && type != 2) {
					throw new Exception("Invalid Account Type.");
				}

				break;
			} catch (Exception e) {
				System.out.println("❌ Exception: " + e.getMessage());

				sc.nextLine();
			}
		}

		String accNo;

		while (true) {
			try {
				System.out.print("🔢 Enter 12-digit Account Number: ");

				accNo = sc.next();

				if (!accNo.matches("\\d{12}")) {
					throw new Exception("Account Number must contain exactly 12 digits.");
				}

				if (ATMDemo.findAccount(accNo) != null) {
					throw new Exception("Account Number already exists.");
				}

				break;
			} catch (Exception e) {
				System.out.println("❌ Exception: " + e.getMessage());
			}
		}

		sc.nextLine();

		String name;

		while (true) {
			try {
				System.out.print("👤 Enter Name: ");

				name = sc.nextLine();

				if (!name.matches("[a-zA-Z ]+")) {
					throw new Exception("Invalid Name.");
				}

				break;
			} catch (Exception e) {
				System.out.println("❌ Exception: " + e.getMessage());
			}
		}

		String pin;

		while (true) {
			try {
				System.out.print("🔐 Enter 6-digit PIN: ");

				pin = sc.next();

				if (!pin.matches("\\d{6}")) {
					throw new Exception("PIN must contain exactly 6 digits.");
				}

				break;
			} catch (Exception e) {
				System.out.println("❌ Exception: " + e.getMessage());
			}
		}

		String phone;

		while (true) {
			try {
				System.out.print("📱 Enter Phone Number: ");

				phone = sc.next();

				if (!phone.matches("[6-9]\\d{9}")) {
					throw new Exception("Invalid Phone Number.");
				}

				break;
			} catch (Exception e) {
				System.out.println("❌ Exception: " + e.getMessage());
			}
		}

		String cardNo;

		while (true) {
			try {
				System.out.print("💳 Enter 16-digit Card Number: ");

				cardNo = sc.next();

				if (!cardNo.matches("\\d{16}")) {
					throw new Exception("Card Number must contain exactly 16 digits.");
				}

				if (ATMDemo.findCard(cardNo) != null) {
					throw new Exception("Card Number already exists.");
				}

				break;
			} catch (Exception e) {
				System.out.println("❌ Exception: " + e.getMessage());
			}
		}

		double balance;

		while (true) {
			try {
				System.out.print("💰 Enter Initial Balance: Rs.");

				balance = sc.nextDouble();

				if (balance < 0) {
					throw new Exception("Balance cannot be negative.");
				}

				if (type == 1 && balance < 1000) {
					throw new Exception("Savings Account requires minimum balance of Rs.1000.");
				}

				if (type == 2 && balance < 5000) {
					throw new Exception("Current Account requires minimum balance of Rs.5000.");
				}

				break;
			} catch (Exception e) {
				System.out.println("❌ Exception: " + e.getMessage());

				sc.nextLine();
			}
		}

		if (ATMDemo.count >= ATMDemo.acc.length) {
			System.out.println("❌ Account limit reached.");

			return;
		}

		if (type == 1) {
			ATMDemo.acc[ATMDemo.count] = new SavingsAccount(accNo, name, pin, phone, cardNo, balance);
		} else {
			ATMDemo.acc[ATMDemo.count] = new CurrentAccount(accNo, name, pin, phone, cardNo, balance);
		}

		ATMDemo.count++;

		ATMDemo.saveAccounts();

		System.out.println("✅ Account added successfully.");
	}

	static void viewAccount(Scanner sc) {
		while (true) {
			try {
				System.out.print("🔍 Enter Account Number: ");

				String accNo = sc.next();

				Account account = ATMDemo.findAccount(accNo);

				if (account == null) {
					throw new Exception("Account Number not found.");
				}

				ATMDemo.displayAccount(account);

				break;
			} catch (Exception e) {
				System.out.println("❌ Exception: " + e.getMessage());
			}
		}
	}

	static void viewAllAccounts() {
		if (ATMDemo.count == 0) {
			System.out.println("📭 No accounts found.");

			return;
		}

		for (int i = 0; i < ATMDemo.count; i++) {
			ATMDemo.displayAccount(ATMDemo.acc[i]);
		}
	}

	static void modifyAccount(Scanner sc) {
		Account account;

		while (true) {
			try {
				System.out.print("🔍 Enter Account Number: ");

				String accNo = sc.next();

				account = ATMDemo.findAccount(accNo);

				if (account == null) {
					throw new Exception("Account Number not found.");
				}

				break;
			} catch (Exception e) {
				System.out.println("❌ Exception: " + e.getMessage());
			}
		}

		while (true) {
			try {
				System.out.println("\n✏️ ===== MODIFY ACCOUNT =====");

				System.out.println("👤 1. Change Name");

				System.out.println("🔐 2. Change PIN");

				System.out.println("📱 3. Change Phone Number");

				System.out.println("💳 4. Change Card Number");

				System.out.println("💰 5. Change Balance");

				System.out.println("⬅️ 6. Back");

				System.out.print("👉 Enter: ");

				int option = sc.nextInt();

				switch (option) {
				case 1:
					changeName(sc, account);
					break;

				case 2:
					changePin(sc, account);
					break;

				case 3:
					changePhone(sc, account);
					break;

				case 4:
					changeCard(sc, account);
					break;

				case 5:
					changeBalance(sc, account);
					break;

				case 6:
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

	static void changeName(Scanner sc, Account account) {
		sc.nextLine();

		while (true) {
			try {
				System.out.print("👤 Enter New Name: ");

				String name = sc.nextLine();

				if (!name.matches("[a-zA-Z ]+")) {
					throw new Exception("Invalid Name.");
				}

				account.name = name;

				ATMDemo.saveAccounts();

				System.out.println("✅ Name changed successfully.");

				break;
			} catch (Exception e) {
				System.out.println("❌ Exception: " + e.getMessage());
			}
		}
	}

	static void changePin(Scanner sc, Account account) {
		while (true) {
			try {
				System.out.print("🔐 Enter New 6-digit PIN: ");

				String pin = sc.next();

				if (!pin.matches("\\d{6}")) {
					throw new Exception("PIN must contain exactly 6 digits.");
				}

				account.pin = pin;

				ATMDemo.saveAccounts();

				System.out.println("✅ PIN changed successfully.");

				break;
			} catch (Exception e) {
				System.out.println("❌ Exception: " + e.getMessage());
			}
		}
	}

	static void changePhone(Scanner sc, Account account) {
		while (true) {
			try {
				System.out.print("📱 Enter New Phone Number: ");

				String phone = sc.next();

				if (!phone.matches("[6-9]\\d{9}")) {
					throw new Exception("Invalid Phone Number.");
				}

				account.phone = phone;

				ATMDemo.saveAccounts();

				System.out.println("✅ Phone Number changed successfully.");

				break;
			} catch (Exception e) {
				System.out.println("❌ Exception: " + e.getMessage());
			}
		}
	}

	static void changeCard(Scanner sc, Account account) {
		while (true) {
			try {
				System.out.print("💳 Enter New 16-digit Card Number: ");

				String card = sc.next();

				if (!card.matches("\\d{16}")) {
					throw new Exception("Card Number must contain exactly 16 digits.");
				}

				Account old = ATMDemo.findCard(card);

				if (old != null && old != account) {
					throw new Exception("Card Number already exists.");
				}

				account.cardNo = card;

				ATMDemo.saveAccounts();

				System.out.println("✅ Card Number changed successfully.");

				break;
			} catch (Exception e) {
				System.out.println("❌ Exception: " + e.getMessage());
			}
		}
	}

static void changeBalance(
        Scanner sc,
        Account account)
    {
        while(true)
        {
            try
            {
                System.out.print(
                    "💰 Enter New Balance: Rs."
                );

                double balance = sc.nextDouble();

                if(balance < 0)
                {

                                   throw new Exception(
                        "Balance cannot be negative."
                    );
                }

                if(account.getAccountType()
                    .equals("Savings") &&
                    balance < 1000)
                {
                    throw new Exception(
                        "Savings Account minimum balance is Rs.1000."
                    );
                }

                if(account.getAccountType()
                    .equals("Current") &&
                    balance < 5000)
                {
                    throw new Exception(
                        "Current Account minimum balance is Rs.5000."
                    );
                }

                account.balance = balance;

                ATMDemo.saveAccounts();

                System.out.println(
                    "✅ Balance changed successfully."
                );

                break;
            }
            catch(Exception e)
            {
                System.out.println(
                    "❌ Exception: " +
                    e.getMessage()
                );

                sc.nextLine();
            }
        }
    }

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		ATMDemo.loadAccounts();

		admin(sc);

		sc.close();
	}
}