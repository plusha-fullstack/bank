import java.time.LocalDateTime;

class Card {

    private String cardNumber;
    private int cvv;
    private double balance;
    private int pin;
    private int pinAttempts = 0;
    private boolean isBlocked = false;
    private LocalDateTime lastFailedAttempt;

    public Card(String cardNumber, int cvv, double balance, int pin) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.balance = balance;
        this.pin = pin;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public int getCvv() {
        return cvv;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        this.balance = balance;
    }

    public String toString() {
        return "Card number: " + cardNumber + "\nCVV: " + cvv + "\nBalance: " + balance;
    }

    public int getPin() {
        return pin;
    }

    public void incrementPinAttempts() {
        pinAttempts++;
    }

    public int getPinAttempts() {
        return pinAttempts;
    }

    public void resetPinAttempts() {
        pinAttempts = 0;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void blockCard() {
        isBlocked = true;
    }

    public void unblockCard() {
        isBlocked = false;
    }

    public void setLastFailedAttempt(LocalDateTime lastFailedAttempt) {
        this.lastFailedAttempt = lastFailedAttempt;
    }

    public void checkAndRemoveBlock() {
        if (isBlocked) {
            LocalDateTime currentTime = LocalDateTime.now();
            if (lastFailedAttempt != null && lastFailedAttempt.plusDays(1).isBefore(currentTime)) {
                unblockCard();
                resetPinAttempts();
            }
        }
    }
}
