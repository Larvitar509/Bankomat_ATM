import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ATM {
    public ArrayList<User> listOfUsers = new ArrayList<>();
    public int pointer = -1;
    public int action = -1;
    public int undo = -1;
    public int counter = 0;
    public int transaction = 0;
    public int sign = 0;
    public String number = "";
    public JLabel label = new JLabel("", SwingConstants.CENTER);
    public JPanel mainPanel = new JPanel();

    public static void main(String[] args) {
        new ATM();
    }

    public ATM() {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                         UnsupportedLookAndFeelException ex) {
                }

                UI ui = new UI();
                ui.setLocationRelativeTo(null);

            }
        });
    }

    public class UI extends JFrame implements ActionListener {
        ArrayList<Button> buttonList = new ArrayList();
        JPanel buttonPanel;

        public UI() {
            super("ATM");
            setSize(400, 500);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
            for (int i = 0; i < 10; i++) {
                buttonList.add(new Button("" + i));
            }
            buttonList.add(new Button("Delete"));
            buttonList.add(new Button("Utwórz nową kartę"));
            buttonList.add(new Button("Użyj posiadanej karty"));
            buttonList.add(new Button("Zatwierdź"));
            buttonList.add(new Button("Cofnij"));

            buttonList.add(new Button("Sprawdź numer karty"));
            buttonList.add(new Button("Stan konta"));
            buttonList.add(new Button("Zmień PIN"));
            buttonList.add(new Button("Wypłać gotówkę"));
            buttonList.add(new Button("Wpłać gotówkę"));
            buttonList.add(new Button("Wyjmij kartę"));
            buttonList.add(new Button("50"));
            buttonList.add(new Button("100"));
            buttonList.add(new Button("150"));
            buttonList.add(new Button("200"));
            buttonList.add(new Button("300"));
            buttonList.add(new Button("400"));
            buttonList.add(new Button("500"));
            buttonList.add(new Button("Inna kwota"));
            for (Button button : buttonList) {
                button.addActionListener(this);
            }
            firstPage();
        }

        public void firstPage() {
            buttonPanel = new JPanel();
            mainPanel.add(label);
            label.setText("Witaj w Banku Rzeszowskim!");
            undo = 0;
           mainPanel.setLayout(new GridLayout(2, 1));
            buttonPanel.setLayout(new GridLayout(1, 2));
            add(mainPanel);
            mainPanel.revalidate();
            for (int i = 11; i < 13; i++) {
                buttonPanel.add(buttonList.get(i));
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainPanel.add(buttonPanel);
            }
        }

        public void mainPage() {
            mainPanel.removeAll();
            buttonPanel = new JPanel();
            mainPanel.add(label);
            undo = 1;
            mainPanel.setLayout(new GridLayout(2, 1));
            buttonPanel.setLayout(new GridLayout(2, 3));
            add(mainPanel);
            mainPanel.revalidate();
            for (int i = 15; i <= 20; i++) {
                buttonPanel.add(buttonList.get(i));
            }
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainPanel.add(buttonPanel);
        }

        public void enteringNumber() {
            mainPanel.removeAll();
            mainPanel.add(label);
            mainPanel.setLayout(new GridLayout(2, 1));
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(5, 3));
            add(mainPanel);
            mainPanel.revalidate();
            for (int i = 1; i < 10; i++) {
                buttonPanel.add(buttonList.get(i));
            }
            buttonPanel.add(buttonList.get(0));
            buttonPanel.add(buttonList.get(10));
            buttonPanel.add(buttonList.get(13));
            buttonPanel.add(buttonList.get(14));

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainPanel.add(buttonPanel);
        }

        public void changingBalance() {
            mainPanel.removeAll();
            buttonPanel = new JPanel();
            mainPanel.add(label);
            undo = 1;
            mainPanel.setLayout(new GridLayout(2, 1));
            buttonPanel.setLayout(new GridLayout(5, 2));
            add(mainPanel);
            mainPanel.revalidate();
            for (int i = 21; i <= 28; i++) {
                buttonPanel.add(buttonList.get(i));
            }
            buttonPanel.add(buttonList.get(14));
            buttonPanel.add(buttonList.get(13));
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainPanel.add(buttonPanel);
        }

        public String generateCardNumber() {
            int leftLimit = 48; // numeral '0'
            int rightLimit = 57; // numeral '9'
            int targetStringLength = 6;
            Random random = new Random();
            String generatedString = random.ints(leftLimit, rightLimit + 1)
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            while (!checkCardNumber(generatedString)) {
                generatedString = random.ints(leftLimit, rightLimit + 1)
                        .limit(targetStringLength)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString();

            }
            return generatedString;
        }

        public boolean checkCardNumber(String number) {
            if (listOfUsers != null) {
                for (User listOfUser : listOfUsers) {
                    return !Objects.equals(number, listOfUser.getNumber());
                }
            }
            return true;
        }

        public boolean findCard(String number) {
            if (listOfUsers != null) {
                for (User user : listOfUsers) {
                    if (user.getNumber().equals(number)) {
                        pointer = listOfUsers.indexOf(user);
                        return true;
                    }
                }
            }
            return false;
        }

        public void enteringDigit(int i) {
            number = number + i;
            label.setText(number);
        }

        public boolean checkPinDigits(String pin) {
            return pin.length() == 4;
        }

        public boolean checkPin(String pin) {
            return Objects.equals(pin, listOfUsers.get(pointer).getPin());
        }

        public void newBalance() {
            if (sign == 0) {
                listOfUsers.get(pointer).addBalance(transaction);
                label.setText("Transakcja przebiegła pomyślnie");
                mainPage();
            } else if (sign == 1) {
                if (transaction > listOfUsers.get(pointer).getBalance()) {
                    label.setText("Nie masz wystarczającej ilości środków na koncie");
                    number = "";
                } else {
                    listOfUsers.get(pointer).subtractBalance(transaction);
                    label.setText("Transakcja przebiegła pomyślnie");
                    mainPage();
                }
            }
        }

        public void counterEx() {
            counter++;
            if (counter == 5) {
                System.exit(404);
            }
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == buttonList.get(0)) {
                enteringDigit(0);
            } else if (e.getSource() == buttonList.get(1)) {
                enteringDigit(1);
            } else if (e.getSource() == buttonList.get(2)) {
                enteringDigit(2);
            } else if (e.getSource() == buttonList.get(3)) {
                enteringDigit(3);
            } else if (e.getSource() == buttonList.get(4)) {
                enteringDigit(4);
            } else if (e.getSource() == buttonList.get(5)) {
                enteringDigit(5);
            } else if (e.getSource() == buttonList.get(6)) {
                enteringDigit(6);
            } else if (e.getSource() == buttonList.get(7)) {
                enteringDigit(7);
            } else if (e.getSource() == buttonList.get(8)) {
                enteringDigit(8);
            } else if (e.getSource() == buttonList.get(9)) {
                enteringDigit(9);
            } else if (e.getSource() == buttonList.get(10)) {
                if (!Objects.equals(number, "")) {
                    number = number.substring(0, number.length() - 1);
                }
                label.setText(number);
            } else if (e.getSource() == buttonList.get(11)) {
                label.setText("Podaj kod PIN do nowej karty");
                action = 0;
                mainPanel.removeAll();
                enteringNumber();
            } else if (e.getSource() == buttonList.get(12)) {
                label.setText("Podaj numer posiadanej karty");
                action = 1;
                mainPanel.removeAll();
                enteringNumber();
            } else if (e.getSource() == buttonList.get(13)) {
                if (action == 0) {
                    //utworzenie konta
                    if (checkPinDigits(number)) {
                        listOfUsers.add(new User(number, generateCardNumber()));
                        pointer = listOfUsers.size() - 1;
                        number = "";
                        label.setText("Pomyślnie utworzono nową kartę o numerze:" + listOfUsers.get(pointer).getNumber());
                        action = -1;
                        mainPage();
                    } else if (!checkPinDigits(number)) {
                        label.setText("Kod PIN musi zawierać cztery cyfry");
                        number = "";
                    }
                } else if (action == 1) {                      // sprawdzenie numeru konta
                    if (findCard(number)) {
                        label.setText("Podaj kod PIN");
                        action = 2;
                        undo = 0;
                        number = "";
                        enteringNumber();

                    } else if (!findCard(number)) {
                        label.setText("Podany numer karty nie jest poprawny");
                        number = "";
                    }
                } else if (action == 2) {
                    // sprawdzenie pinu istniejącego konta
                    if (checkPin(number)) {
                        label.setText("Pomyślnie uzyskano dostęp do karty");
                        number = "";
                        counter = 0;
                        action = -1;
                        mainPage();
                    } else if (!checkPin(number)) {
                        label.setText("Podany kod PIN nie jest poprawny, spróbuj ponownie");
                        number = "";
                        counterEx();
                    }

                } else if (action == 3) {
                    newBalance();
                } else if (action == 4) {
                    if (!Objects.equals(number, "")) {
                        transaction = Integer.parseInt(number);
                        if (transaction % 20 == 0) {
                            newBalance();
                            number = "";
                        } else {
                            number = "";
                            label.setText("Wybierz kwotę równą wielokrotności 20");
                        }
                    }
                } else if (action == 5) {
                    if (checkPinDigits(number)) {
                        listOfUsers.get(pointer).setPin(number);
                        number = "";
                        label.setText("Pomyślnie zmieniono kod PIN");
                        action = -1;
                        mainPage();
                    } else if (!checkPinDigits(number)) {
                        label.setText("Kod PIN musi zawierać cztery cyfry");
                        number = "";
                    }
                }
            } else if (e.getSource() == buttonList.get(14)) {
                mainPanel.removeAll();
                if (undo == 0) {
                    // powrót do startu
                    number = "";
                    firstPage();
                } else if (undo == 1) {
                    number = "";
                    mainPage();
                } else if (undo == 2) {
                    number = "";
                    changingBalance();
                }
            } else if (e.getSource() == buttonList.get(15)) {
                label.setText("Numer tej karty to: " + listOfUsers.get(pointer).getNumber());
            } else if (e.getSource() == buttonList.get(16)) {
                label.setText("Stan konta jest równy: " + listOfUsers.get(pointer).getBalance() + "PLN");
            } else if (e.getSource() == buttonList.get(17)) {
                label.setText("Utwórz nowy kod PIN");
                undo = 1;
                action = 5;
                enteringNumber();
            } else if (e.getSource() == buttonList.get(18)) {
                label.setText("Wybierz wypłacaną kwotę");
                sign = 1;
                action = 3;
                undo = 1;
                changingBalance();
            } else if (e.getSource() == buttonList.get(19)) {
                label.setText("Wybierz wpłacaną kwotę");
                sign = 0;
                action = 3;
                undo = 1;
                changingBalance();
            } else if (e.getSource() == buttonList.get(20)) {
                mainPanel.removeAll();
                pointer = -1;
                number = "";
                firstPage();
            } else if (e.getSource() == buttonList.get(21)) {
                label.setText("Wybrana kwota to: 50 PLN");
                transaction = 50;
            } else if (e.getSource() == buttonList.get(22)) {
                label.setText("Wybrana kwota to: 100 PLN");
                transaction = 100;
            } else if (e.getSource() == buttonList.get(23)) {
                label.setText("Wybrana kwota to: 150 PLN");
                transaction = 150;
            } else if (e.getSource() == buttonList.get(24)) {
                label.setText("Wybrana kwota to: 200 PLN");
                transaction = 200;
            } else if (e.getSource() == buttonList.get(25)) {
                label.setText("Wybrana kwota to: 300 PLN");
                transaction = 300;
            } else if (e.getSource() == buttonList.get(26)) {
                label.setText("Wybrana kwota to: 400 PLN");
                transaction = 400;
            } else if (e.getSource() == buttonList.get(27)) {
                label.setText("Wybrana kwota to: 500 PLN");
                transaction = 500;
            } else if (e.getSource() == buttonList.get(28)) {
                label.setText("Wprowadź inną kwotę");
                action = 4;
                undo = 2;
                enteringNumber();
            }
        }
    }
}