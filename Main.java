import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ATM atm = new ATM(1000000); // Лимит банкомата - 1 000 000
        CardReader cardReader = new CardReader("cards_data.txt");

        Scanner scanner = new Scanner(System.in);

        boolean exit = false;

        while (!exit) {
            System.out.println("1. Ввести номер карты и ПИН");
            System.out.println("2. Выйти");
            System.out.print("Выберите действие: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    System.out.print("Введите номер карты: ");
                    String cardNumber = scanner.next();
                    System.out.print("Введите ПИН-код: ");
                    int pin = scanner.nextInt();
                    Card card = cardReader.authorizeCard(cardNumber, pin);
                    if (card != null) {
                        System.out.println("Доступ разрешен.");
                        processCardOperations(card, atm, scanner);
                        cardReader.updateCardData();
                    } else {
                        System.out.println("Доступ запрещен. Пожалуйста, проверьте номер карты и ПИН-код.");
                    }
                }
                case 2 -> exit = true;
                default -> System.out.println("Некорректный выбор.");
            }
        }

        scanner.close();
    }

    private static void processCardOperations(Card card, ATM atm, Scanner scanner) {
        boolean logout = false;

        while (!logout) {
            System.out.println("1. Проверить баланс карты");
            System.out.println("2. Снять средства");
            System.out.println("3. Пополнить баланс");
            System.out.println("4. Выйти");
            System.out.print("Выберите действие: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> System.out.println("Баланс карты: " + card.getBalance());
                case 2 -> {
                    System.out.print("Введите сумму для снятия: ");
                    int amountToWithdraw = scanner.nextInt();
                    if (atm.canWithdraw(amountToWithdraw)) {
                        atm.withdraw(amountToWithdraw, card);
                        System.out.println("Снято " + amountToWithdraw + ". Новый баланс карты: " + card.getBalance());
                    } else {
                        System.out.println("Недостаточно средств на счете или превышен лимит средств в банкомате.");
                    }
                }
                case 3 -> {
                    System.out.print("Введите сумму для пополнения: ");
                    int amountToDeposit = scanner.nextInt();
                    if (amountToDeposit > 0) {
                        atm.deposit(amountToDeposit, card);
                        System.out.println("Пополнено " + amountToDeposit + ". Новый баланс карты: " + card.getBalance());
                    } else {
                        System.out.println("Неверная сумма для пополнения.");
                    }
                }
                case 4 -> logout = true;
                default -> System.out.println("Некорректный выбор.");
            }
        }
    }
}
