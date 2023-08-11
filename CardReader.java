import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CardReader {

    private List<Card> cards;
    private String cardsFilename;

    public CardReader(String cardsFilename) {
        this.cardsFilename = cardsFilename;
        this.cards = loadCards();
    }

    public Card authorizeCard(String cardNumber, int pin) {
        for (Card card : cards) {
            card.checkAndRemoveBlock();
            if (card.getCardNumber().equals(cardNumber)) {
                if (!card.isBlocked()) {
                    if (card.getCardNumber().equals(cardNumber) && isValidPin(card, pin)) {
                        card.resetPinAttempts();
                        return card;
                    } else {
                        card.incrementPinAttempts();
                        card.setLastFailedAttempt(LocalDateTime.now());
                        if (card.getPinAttempts() >= 3) {
                            card.blockCard();
                        }
                        return null;
                    }
                } else {
                    System.out.println("Карта заблокирована. Пожалуйста, подождите.");
                    return null;
                }
            }
        }
        return null;
    }

    private boolean isValidPin(Card card, int pin) {
        int cardPin = card.getPin();
        return cardPin == pin;
    }

    public void updateCardData() {
        try {
            FileWriter writer = new FileWriter(cardsFilename);
            for (Card card : cards) {
                writer.write(card.getCardNumber() + " " + card.getCvv() + " " + card.getBalance() + " " + card.getPin() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Card> loadCards() {
        List<Card> loadedCards = new ArrayList<>();
        try {
            File file = new File(cardsFilename);
            if (!file.exists()) {
                return loadedCards;
            }
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");
                if (parts.length == 4) {
                    String cardNumber = parts[0];
                    int cvv = Integer.parseInt(parts[1]);
                    double balance = Double.parseDouble(parts[2]);
                    int pin = Integer.parseInt(parts[3]);
                    loadedCards.add(new Card(cardNumber, cvv, balance, pin));
                }
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadedCards;
    }
}
