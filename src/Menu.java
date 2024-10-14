import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.time.*;

import static java.lang.Math.round;

public class Menu {

    public String writingPolice = "Times New Roman";
    int windowX = 700;
    int windowY = 350;

    private double userMoneyDouble;
    private String userMoney;

    //window
    JFrame frame;
    CardLayout cardLayout;
    JPanel panel;

    // db
    DB db = new DB();

    double actualMoney = db.getMoney();

    JButton moneyButton; // button for user money amount

    public Menu() throws SQLException {
        //set up frame
        frame = new JFrame("BrokeNoMore Manager");
        frame.setSize(this.windowX, this.windowY);
        frame.setLocationRelativeTo(null); // put the frame in the middle of the frame
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        panel = new JPanel(cardLayout);

        JPanel menuLauncher = menuLauncher();
        JPanel toolWindow = toolWindow();
        JPanel moneyWindow = moneyWindow();
        JPanel converterWindow = converterWindow();
        JPanel manageMoneyWindow = manageMoneyWindow();
        //JPanel monthlyReportWindow = monthlyReportWindow();
        JPanel actionsWindow = actionsWindow();
        JPanel historyWindow = historyWindow();

        panel.add(menuLauncher, "MenuLauncher");
        panel.add(toolWindow, "ToolWindow");
        panel.add(moneyWindow, "MoneyWindow");
        panel.add(converterWindow, "ConverterWindow");
        panel.add(manageMoneyWindow, "manageMoneyWindow");
        panel.add(actionsWindow, "actionsWindow");
        panel.add(historyWindow, "historyWindow");
        frame.add(panel);

        frame.setVisible(true);
    }

    public JPanel menuLauncher() throws SQLException {

        userMoneyDouble = db.getMoney();
        userMoney = String.format("%.2f", userMoneyDouble);
        // set menu panel
        JPanel menuLauncher = new JPanel(new BorderLayout());
        //frame.setTitle("BrokeNoMore Manager");

        JLabel titleLabel = new JLabel("Balance");
        titleLabel.setFont(new Font(writingPolice, Font.BOLD, 30));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER); // center the title

        moneyButton = new JButton(this.userMoney + "$");
        moneyButton.setFont(new Font(writingPolice, Font.PLAIN, 50));
        moneyButton.setBackground(Color.GREEN); // set color

        JPanel panelBalance = new JPanel();
        panelBalance.setLayout(new BoxLayout(panelBalance, BoxLayout.Y_AXIS));
        panelBalance.add(titleLabel);

        // add some spacing between the label and button
        panelBalance.add(Box.createVerticalStrut(10));
        panelBalance.add(moneyButton);

        // align components to the center
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        moneyButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton buttonTool = new JButton("Tools");
        buttonTool.setFont(new Font(writingPolice, Font.BOLD, 30));
        buttonTool.setBackground(Color.ORANGE);

        JButton buttonClose = new JButton("Close");
        buttonClose.setFont(new Font(writingPolice, Font.BOLD, 30));
        buttonClose.setBackground(Color.RED);

        JPanel panelButtons = new JPanel(new GridLayout(1, 2, 10, 0));
        panelButtons.add(buttonTool);
        panelButtons.add(buttonClose);

        buttonClose.setSize(250, 100);
        buttonTool.setSize(250, 100);

        panelButtons.setBorder(new EmptyBorder(20, 30, 20, 30));
        menuLauncher.add(panelBalance, BorderLayout.CENTER);
        menuLauncher.add(panelButtons, BorderLayout.SOUTH);


        buttonTool.addActionListener(e -> {
            cardLayout.show(panel, "ToolWindow");
        });

        moneyButton.addActionListener(e -> {
            cardLayout.show(panel, "MoneyWindow");
        });

        buttonClose.addActionListener(e -> {
            frame.dispose();
            System.exit(0); // stop the Java program from running
        });

        return menuLauncher;
    }


    public JPanel toolWindow() {
        JPanel toolWindow = new JPanel();
        //("BrokeNoMore Tools");

        GridLayout grid = new GridLayout(3, 3, 10, 10);
        toolWindow.setLayout(grid);

        String[] buttonNames = {"Converter", "Manage Money", "Actions", "History", "e", "d", "d", "r", "Return to menu"};
        Color[] colors = {Color.GREEN, Color.YELLOW, Color.GRAY, Color.ORANGE, Color.PINK, Color.CYAN, Color.GRAY, Color.ORANGE, Color.RED};
        ActionListener[] eventListeners = {
                e -> {
                    cardLayout.show(panel, "ConverterWindow");
                },

                e -> {
                    cardLayout.show(panel, "manageMoneyWindow");
                },

                e -> {
                    cardLayout.show(panel, "actionsWindow");
                },

                e -> {
                    cardLayout.show(panel, "historyWindow");
                },

                e -> System.out.println("Button e clicked"),
                e -> System.out.println("Button d clicked"),
                e -> System.out.println("Button d clicked"),
                e -> System.out.println("Button r clicked"),

                e -> {cardLayout.show(panel, "MenuLauncher");}
        };

        for (int i = 1; i <= 9; i++) {
            JButton button = new JButton(buttonNames[i-1]);
            button.setFont(new Font(writingPolice, Font.BOLD, 20));
            button.setBackground(colors[i-1]);
            toolWindow.add(button);

            button.addActionListener(eventListeners[i-1]);
        }
        //frame.setVisible(true);
        return toolWindow;
    }

    public JPanel moneyWindow(){
        JPanel moneyWindow = new JPanel(new BorderLayout());
        //frame.setTitle("Asset Manager");


        JLabel titleLabel = new JLabel("Asset Viewer");
        titleLabel.setFont(new Font(writingPolice, Font.BOLD, 30));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.add(titleLabel);

        String[][] moneyRepartition = {{"1", "In-Bank", "1500$", "1500$"},
                {"2", "Cash", "51.18$", "51.18$"},
                {"3", "Euro", "20€", "22.09$"},
                {"4", "Gold", "0.035 oz", "12.97$"},
                {"5", "Bitcoin", "0.00002 ₿", "11.45$"}
        };

        String[] column = {"Rank", "Asset", "Quantity", "Dollar Value"};

        JTable tableMoney = new JTable(moneyRepartition, column);
        tableMoney.setSize(500,250);
        tableMoney.setPreferredSize(new Dimension(500,500));
        JScrollPane scrollPane = new JScrollPane(tableMoney);

        tableMoney.getColumnModel().getColumn(0).setPreferredWidth(20);

        tableMoney.setRowHeight(40);
        tableMoney.setFont(new Font(writingPolice, Font.BOLD, 30));

        JButton buttonReturn = new JButton("Return to Menu");
        buttonReturn.setFont(new Font(writingPolice, Font.BOLD, 30));
        buttonReturn.setBackground(Color.RED);

        titlePanel.setVisible(true);
        moneyWindow.add(titlePanel, BorderLayout.NORTH);
        moneyWindow.add(buttonReturn, BorderLayout.SOUTH);
        moneyWindow.add(scrollPane, BorderLayout.CENTER);

        buttonReturn.addActionListener(e -> {
            cardLayout.show(panel, "MenuLauncher");
        });
        return moneyWindow;
    }

    public JPanel converterWindow(){
        JPanel converterWindow = new JPanel(null);
        //frame.setTitle("Converter");

        JTextArea textArea = new JTextArea();
        JTextArea textArea2 = new JTextArea();

        textArea.setBounds(100, 110, 200, 50);
        textArea.setFont(new Font(writingPolice, Font.BOLD, 30));
        textArea2.setBounds(400, 110, 200, 50);
        textArea2.setFont(new Font(writingPolice, Font.BOLD, 30));
        textArea.setBorder(BorderFactory.createLineBorder(Color.black, 5, true));
        textArea2.setBorder(BorderFactory.createLineBorder(Color.black, 5, true));

        textArea2.setEditable(false);

        String[] listCurrencies = {"EUR", "USD", "GBP", "WON", "CA"};
        JComboBox<String> currenciesBox = new JComboBox<>(listCurrencies);
        JComboBox<String> currenciesBox2 = new JComboBox<>(listCurrencies);
        currenciesBox2.setSelectedIndex(1);

        currenciesBox.setFont(new Font(writingPolice, Font.BOLD, 30));
        currenciesBox.setBounds(100, 40, 200, 50);

        currenciesBox2.setFont(new Font(writingPolice, Font.BOLD, 30));
        currenciesBox2.setBounds(400, 40, 200, 50);

        converterWindow.add(currenciesBox);
        converterWindow.add(currenciesBox2);

        String userInput = textArea.getText();

        converterWindow.add(textArea);
        converterWindow.add(textArea2);

        JButton buttonConvert = new JButton("Convert");
        buttonConvert.setFont(new Font(writingPolice, Font.BOLD, 30));
        buttonConvert.setBackground(Color.GREEN);

        JButton buttonReturn = new JButton("Return to tools");
        buttonReturn.setFont(new Font(writingPolice, Font.BOLD, 30));
        buttonReturn.setBackground(Color.RED);

        buttonReturn.setBounds(400, 250, 250, 50);
        buttonConvert.setBounds(50, 250, 250, 50);

        converterWindow.add(buttonConvert);
        converterWindow.add(buttonReturn);



        buttonConvert.addActionListener(e -> {
            if (textArea.getText().isEmpty()){
                return;
            }
            else if (textArea2.getText().matches("[a-zA-Z]+")){ // this doesnt' work
                textArea2.setText("Put only numbers !");
            }

            Map<String, Double> conversionRates = new HashMap<>();

            conversionRates.put("EUR", 1.0); // 1 eur = 1 eur
            conversionRates.put("USD", 1.09); // 1 eur = 1.09 usd
            conversionRates.put("GBP", 0.84);
            conversionRates.put("WON", 1474.5);
            conversionRates.put("CA", 1.50);

            String startCurr = (String) currenciesBox.getSelectedItem();
            String targetCurr = (String) currenciesBox2.getSelectedItem();
            double amount = Double.parseDouble(textArea.getText());

            double amountEur = amount / conversionRates.get(startCurr); // convert in EUR

            double amountTarget = amountEur * conversionRates.get(targetCurr); // convert in the target currency

            textArea2.setText(String.valueOf(amountTarget)); // convert double to string

        });

        buttonReturn.addActionListener(e -> {
            cardLayout.show(panel, "ToolWindow");
        });
        return converterWindow;
    }

    public JPanel manageMoneyWindow() throws SQLException{
        JPanel moneyWindow = new JPanel(new BorderLayout());

        JButton buttonAddMoney = new JButton("Add Money");
        buttonAddMoney.setFont(new Font(writingPolice, Font.BOLD, 30));
        buttonAddMoney.setBackground(new Color(0, 255, 0)); // color green

        JButton buttonRemoveMoney = new JButton("Remove Money");
        buttonRemoveMoney.setFont(new Font(writingPolice, Font.BOLD, 30));
        buttonRemoveMoney.setBackground(Color.ORANGE);

        JButton buttonReturn = new JButton("Return to Tools");
        buttonReturn.setFont(new Font(writingPolice, Font.BOLD, 30));
        buttonReturn.setBackground(Color.RED);

        JPanel panelButtons = new JPanel(new GridLayout(1, 2, 10, 0));
        panelButtons.add(buttonAddMoney);
        panelButtons.add(buttonRemoveMoney);
        panelButtons.add(buttonReturn);

        //buttonReturn.setSize(250, 100);
        //buttonAddMoney.setSize(250, 100);

        panelButtons.setBorder(new EmptyBorder(20, 30, 20, 30));
        moneyWindow.add(panelButtons, BorderLayout.SOUTH);

        JTextArea amountMoneyArea = new JTextArea();
        amountMoneyArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5, true));
        amountMoneyArea.setFont(new Font(writingPolice, Font.BOLD, 30));
        moneyWindow.add(amountMoneyArea, BorderLayout.CENTER);

        buttonReturn.addActionListener(e -> {
            cardLayout.show(panel, "ToolWindow");
        });

        buttonRemoveMoney.addActionListener(e -> {
            if (amountMoneyArea.getText().isEmpty()){
                JOptionPane.showMessageDialog(panel, "Please specify the amount you want to remove !");
                return;
            }
            else if (Double.parseDouble(amountMoneyArea.getText()) < 0){
                JOptionPane.showMessageDialog(panel, "Please enter a positive number !");
                return;
            }
            else if (actualMoney < Double.parseDouble(amountMoneyArea.getText())){
                JOptionPane.showMessageDialog(panel, "You don't have enough money !");
                return;
            }

            DB db = new DB();
            try {
                db.addMoney(-Double.parseDouble(amountMoneyArea.getText()));
                reload();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        buttonAddMoney.addActionListener(e -> {
            if (amountMoneyArea.getText().isEmpty()){
                JOptionPane.showMessageDialog(panel, "Please specify the amount you want to add !");
                return;
            }
            else if (Double.parseDouble(amountMoneyArea.getText()) < 0){
                JOptionPane.showMessageDialog(panel, "Please enter a positive number !");
                return;
            }

            DB db = new DB();
            try {
                db.addMoney(Double.parseDouble(amountMoneyArea.getText()));
                reload();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        return moneyWindow;
    }

    public JPanel actionsWindow() throws SQLException {
        JPanel actionsWindow = new JPanel(new BorderLayout(20,20));

        JButton buttonLog = new JButton("Log Action");
        buttonLog.setFont(new Font(writingPolice, Font.BOLD, 30));
        buttonLog.setBackground(new Color(0, 255, 0)); // green

        JButton buttonReturn = new JButton("Return to Tools");
        buttonReturn.setFont(new Font(writingPolice, Font.BOLD, 30));
        buttonReturn.setBackground(Color.RED);

        JPanel panelButtons = new JPanel(new GridLayout(1, 2, 10, 0));
        panelButtons.add(buttonLog);
        panelButtons.add(buttonReturn);
        panelButtons.setBorder(new EmptyBorder(20, 30, 20, 30));

        actionsWindow.add(panelButtons, BorderLayout.SOUTH);

        JTextArea amountMoneyArea = new JTextArea();
        amountMoneyArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5, true));
        amountMoneyArea.setFont(new Font(writingPolice, Font.BOLD, 30));

        String[] typeTransaction = {"Deposit", "Withdraw"};
        JComboBox<String> typeBox = new JComboBox<>(typeTransaction);
        typeBox.setFont(new Font(writingPolice, Font.BOLD, 30));

        JTextArea transactionNotesArea = new JTextArea();
        transactionNotesArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5, true));
        transactionNotesArea.setFont(new Font(writingPolice, Font.BOLD, 30));

        JPanel panelActions = new JPanel(new GridLayout(3, 2, 2, 20));
        panelActions.add(new JLabel("Amount:"));
        panelActions.add(amountMoneyArea);
        panelActions.add(new JLabel("Notes:"));
        panelActions.add(transactionNotesArea);
        panelActions.add(new JLabel("Transaction Type:"));
        panelActions.add(typeBox);

        actionsWindow.add(panelActions, BorderLayout.CENTER);


        buttonReturn.addActionListener(e -> {
            cardLayout.show(panel, "ToolWindow");
        });

        buttonLog.addActionListener(e -> {
            double moneyBefore = 0;
            double transactionAmount = Double.parseDouble(amountMoneyArea.getText());
            double moneyAfter = 0;
            String notes = transactionNotesArea.getText();
            String type = typeBox.getSelectedItem().toString();

            try {
                reload();
                moneyBefore = db.getMoney();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            if (amountMoneyArea.getText().isEmpty() || transactionNotesArea.getText().isEmpty()){
                JOptionPane.showMessageDialog(panel, "Please fill all the fields !");
                return;
            }
            if (transactionAmount < 0){
                JOptionPane.showMessageDialog(panel, "Please enter a positive amount of money !");
                return;
            }
            try {
                if (Objects.equals(type, "Withdraw") && transactionAmount > db.getMoney()){
                    JOptionPane.showMessageDialog(panel, "You can't deposit this amount as you don't have enough money !");
                    return;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            if (type == "Deposit"){
                moneyAfter = moneyBefore + transactionAmount;
                try {
                    db.addMoney(transactionAmount);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            else if (type == "Withdraw"){
                moneyAfter = moneyBefore - transactionAmount;
                try {
                    db.addMoney(-transactionAmount);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            try {
                db.addLogs(moneyBefore, transactionAmount, type, moneyAfter, notes);
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }

            try {
                reload();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            if (type == "Deposit"){
                JOptionPane.showMessageDialog(panel, "Successfully deposit " + transactionAmount + "$ ! You have now " + String.format("%.2f", moneyAfter) + "$ !");
            }

            if (type == "Withdraw"){
                JOptionPane.showMessageDialog(panel, "Successful withdraw " + transactionAmount + "$ ! You have now " + String.format("%.2f", moneyAfter) + "$ !");
            }

            amountMoneyArea.setText("");
            transactionNotesArea.setText("");

        });

        return actionsWindow;
    }

    public JPanel historyWindow() throws SQLException {
        JPanel historyWindow = new JPanel(new BorderLayout(20, 20));

        JButton buttonReturn = new JButton("Return to Tools");
        buttonReturn.setFont(new Font(writingPolice, Font.BOLD, 30));
        buttonReturn.setBackground(Color.RED);

        JPanel panelButtons = new JPanel(new GridLayout(1, 2, 10, 0));
        panelButtons.add(buttonReturn);
        panelButtons.setBorder(new EmptyBorder(20, 30, 20, 30));

        historyWindow.add(panelButtons, BorderLayout.SOUTH);

        JPanel historyPanel = new JPanel();
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(historyPanel);
        historyWindow.add(scrollPane, BorderLayout.CENTER);


        try {
            List<List<String>> history = db.getHistory();

            for (List<String> row : history) {
                JPanel rowPanel = new JPanel(new GridLayout(1, 7, 5, 0));

                for (String data : row) {
                    JLabel label = new JLabel(data);
                    rowPanel.add(label);
                }

                historyPanel.add(rowPanel);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        buttonReturn.addActionListener(e -> {
            cardLayout.show(panel, "ToolWindow");
        });

        return historyWindow;
    }

    /*public JPanel monthlyReportWindow() throws SQLException {

    }*/


    public void reload() throws SQLException {
        userMoneyDouble = db.getMoney();
        userMoney = String.format("%.2f", userMoneyDouble);
        moneyButton.setText(userMoney + "$");
        panel.revalidate();
        panel.repaint();
        this.actualMoney = db.getMoney(); // update the money
    }



    /*public void monthlyReportCheck() {
        LocalDate date = LocalDate.now();
        int dayOfMonth = date.getDayOfMonth();
        if (dayOfMonth == 01){

        }
    }*/

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Menu();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}


/*
            String moneyString = moneyButton.getText();
            int dollarSignIndex = moneyString.length() - 1; // get index of "$" character
            moneyString = moneyString.substring(0, dollarSignIndex);
            moneyString = moneyString.replace(",", "."); // replace , by .
 */