import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ProfessionalCalculator extends JFrame implements ActionListener {
    private JTextField display;
    private String operator;
    private double firstNumber;
    private boolean startNewNumber = true;

    public ProfessionalCalculator() {
        setTitle("Calculator");
        setSize(400, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.DARK_GRAY);

        // Display
        display = new JTextField("0");
        display.setFont(new Font("Arial", Font.BOLD, 36));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBackground(Color.BLACK);
        display.setForeground(Color.GREEN);
        display.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(display, BorderLayout.NORTH);

        // Buttons panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.DARK_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1;
        gbc.weighty = 1;

        String[][] buttons = {
            {"C", "←", "±", "%"},
            {"7", "8", "9", "/"},
            {"4", "5", "6", "*"},
            {"1", "2", "3", "-"},
            {"0", "0", ".", "+"}, // "0" spans 2 columns
            {"√", "=", "", ""}     // Optional row for sqrt and equal
        };

        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < 4; col++) {
                String text = buttons[row][col];
                if (text.isEmpty()) continue;
                JButton button = new JButton(text);
                button.setFont(new Font("Arial", Font.BOLD, 24));
                button.setFocusPainted(false);

                if ("+-*/=".contains(text)) {
                    button.setBackground(Color.ORANGE);
                    button.setForeground(Color.WHITE);
                } else if ("C←±%√".contains(text)) {
                    button.setBackground(Color.GRAY);
                    button.setForeground(Color.WHITE);
                } else {
                    button.setBackground(Color.LIGHT_GRAY);
                    button.setForeground(Color.BLACK);
                }

                button.addActionListener(this);

                gbc.gridx = col;
                gbc.gridy = row;
                if (text.equals("0") && row == 4 && col == 0) {
                    gbc.gridwidth = 2; // "0" spans 2 columns
                    panel.add(button, gbc);
                    col++; // skip next column
                } else {
                    gbc.gridwidth = 1;
                    panel.add(button, gbc);
                }
            }
        }

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if ("0123456789.".contains(command)) {
            if (startNewNumber) {
                display.setText(command.equals(".") ? "0." : command);
                startNewNumber = false;
            } else {
                if (command.equals(".") && display.getText().contains(".")) return;
                display.setText(display.getText() + command);
            }
        } else if ("+-*/".contains(command)) {
            firstNumber = Double.parseDouble(display.getText());
            operator = command;
            startNewNumber = true;
        } else if ("=".equals(command)) {
            calculateResult();
        } else if ("C".equals(command)) {
            display.setText("0");
            operator = null;
            firstNumber = 0;
            startNewNumber = true;
        } else if ("←".equals(command)) {
            String text = display.getText();
            if (text.length() > 1) {
                display.setText(text.substring(0, text.length() - 1));
            } else {
                display.setText("0");
                startNewNumber = true;
            }
        } else if ("±".equals(command)) {
            double value = Double.parseDouble(display.getText());
            display.setText(String.valueOf(-value));
        } else if ("%".equals(command)) {
            double value = Double.parseDouble(display.getText()) / 100;
            display.setText(String.valueOf(value));
        } else if ("√".equals(command)) {
            double value = Double.parseDouble(display.getText());
            if (value >= 0) {
                display.setText(String.valueOf(Math.sqrt(value)));
            } else {
                display.setText("Error");
                startNewNumber = true;
            }
        }
    }

    private void calculateResult() {
        if (operator == null) return;
        double secondNumber = Double.parseDouble(display.getText());
        double result = 0;

        switch (operator) {
            case "+": result = firstNumber + secondNumber; break;
            case "-": result = firstNumber - secondNumber; break;
            case "*": result = firstNumber * secondNumber; break;
            case "/": 
                if (secondNumber != 0) result = firstNumber / secondNumber;
                else {
                    display.setText("Error");
                    startNewNumber = true;
                    return;
                }
                break;
        }

        display.setText(result % 1 == 0 ? String.valueOf((int) result) : String.valueOf(result));
        startNewNumber = true;
        operator = null;
    }

    public static void main(String[] args) {
        new ProfessionalCalculator();
    }
}


