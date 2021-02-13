import javax.swing.*;
import java.awt.*;

public class UI {
    private JFrame window;
    private JLabel congrats;
    private JTextField worldField;
    private JTextField alreadyGuessedField;
    private JTextField errorField;
    private JTextField input;
    private JButton newGame;


    private LogicForHumanGuesses logic;
    Container<Object> details;
    private String currentState;
    private GameState state;
    private int remaining;
    private String alreadyGuessed;
    private int correctGuesses = 0;
    private int falseGuesses = 0;
    private DictionaryManager dictionaryManager;

    public UI() {
        window = new JFrame("Galgenmännchen");
        window.setSize(600, 400);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        logic = new LogicForHumanGuesses((dictionaryManager = new DictionaryManager()));
        addItems();
        initLogic();
        window.setVisible(true);
    }

    private void addItems() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.lightGray);
        panel.setLayout(new GridLayout(6, 2, 5, 5));

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Spieltyp");
        JMenuItem human = new JCheckBoxMenuItem("Du rätst");
        JMenuItem ai = new JCheckBoxMenuItem("AI rät");
        menu.add(human);
        menu.add(ai);
        human.addActionListener(e -> {
            if (human.isSelected()) {
                ai.setSelected(false);
            }
        });
        ai.addActionListener(e -> {
            if (ai.isSelected()) {
                human.setSelected(false);
            }
        });
        menuBar.add(menu);
        menuBar.setVisible(true);
        window.setJMenuBar(menuBar);

        JLabel wordFieldLabel = new JLabel("Zu erraten:");
        JLabel inputLabel = new JLabel("Eingabe:");
        JLabel alreadyGuessedLabel = new JLabel("Bereits versucht:");
        JLabel remainingTries = new JLabel("Verbleibende Versuche:");
        congrats = new JLabel("");

        worldField = new JTextField();
        worldField.setEditable(false);
        alreadyGuessedField = new JTextField();
        alreadyGuessedField.setEditable(false);
        errorField = new JTextField();
        errorField.setEditable(false);
        input = new JTextField();
        input.addActionListener(e -> {
            details = logic.handleUserInput(input.getText());
            updateFromContainer(details);
            updateUI();
        });

        newGame = new JButton("Neues Spiel");
        newGame.setEnabled(false);
        newGame.setVisible(false);
        newGame.addActionListener(e -> {
            initLogic();
            newGame.setEnabled(false);
            newGame.setVisible(false);
            input.setEnabled(true);
        });

        panel.add(wordFieldLabel);
        panel.add(alreadyGuessedLabel);
        panel.add(worldField);
        panel.add(alreadyGuessedField);
        panel.add(inputLabel);
        panel.add(remainingTries);
        panel.add(input);
        panel.add(errorField);
        panel.add(congrats);
        panel.add(newGame);

        window.add(panel);
    }

    private void updateFromContainer(Container<Object> container) {
        currentState = (String) container.getByName("currentState");
        state = (GameState) container.getByName("state");
        remaining = (int) container.getByName("remaining");
        alreadyGuessed = (String) container.getByName("alreadyGuessed");
    }

    private void updateUI() {
        congrats.setText("");
        worldField.setText(currentState);
        input.setText("");
        alreadyGuessedField.setText(alreadyGuessed);
        errorField.setText(String.valueOf(remaining));
        if (state == GameState.Lost) {
            congrats.setText("You lost");
            worldField.setText(logic.getActiveWord());
            newGame.setVisible(true);
            newGame.setEnabled(true);
            input.setEnabled(false);
            falseGuesses++;
        } else if (state == GameState.Won) {
            congrats.setText("You won");
            newGame.setVisible(true);
            newGame.setEnabled(true);
            input.setEnabled(false);
            correctGuesses++;
        }
    }

    private void initLogic() {
        details = logic.initialize();
        updateFromContainer(details);
        updateUI();
    }
}
