public class User {
    private String pin;
    private String number;
    private double balance;

    public User(String pin, String nr) {
        this.pin = pin;
        this.number = nr;
        this.balance = 0;
    }

    public String getNumber() {
        return this.number;
    }

    public String getPin() {
        return this.pin;
    }


    public double getBalance() {
        return balance;
    }

    public void setPin(String newPin) {
        pin = newPin;
    }

    public void addBalance(double money) {
        balance += money;
    }

    public void subtractBalance(double money) {
        balance -= money;
    }
}
