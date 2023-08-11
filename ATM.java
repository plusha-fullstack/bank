import java.io.*;
import java.util.Scanner;

public class ATM {

    private int limit;
    private String limitFilename = "atm_limit.txt";

    public ATM(int initialLimit) {
        this.limit = loadLimit();
        if (this.limit == -1) {
            this.limit = initialLimit;
        }
    }

    public boolean canWithdraw(int amount) {
        return amount > 0 && amount <= limit;
    }

    public void withdraw(int amount, Card card) {
        if (amount > 0 && amount <= limit) {
            card.setBalance(card.getBalance() - amount);
            this.limit -= amount;
            saveLimit();
        } else {
            System.out.println("Невозможно снять указанную сумму. Пожалуйста, проверьте лимит средств в банкомате.");
        }
    }

    public void deposit(int amount, Card card) {
        if (amount > 0) {
            card.setBalance(card.getBalance() + amount);
            this.limit += amount;
            saveLimit();
        } else {
            System.out.println("Невозможно пополнить баланс на отрицательную сумму.");
        }
    }

    private void saveLimit() {
        try (FileWriter writer = new FileWriter(limitFilename, false)) {
            writer.write(Integer.toString(limit));
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении лимита в файл: " + e.getMessage());
        }
    }

    private int loadLimit() {
        try {
            File file = new File(limitFilename);
            if (!file.exists()) {
                return -1;
            }
            try (Scanner scanner = new Scanner(file)) {
                return scanner.nextInt();
            }
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке лимита из файла: " + e.getMessage());
            return -1;
        }
    }
}
