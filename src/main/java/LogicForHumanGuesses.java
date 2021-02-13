import java.util.StringJoiner;

public class LogicForHumanGuesses {
    private String activeWord;
    private String currentState;
    private DictionaryManager manager;
    private int errors;
    private GameState state;
    private StringJoiner alreadyGuessed;
    private String earlierAIGuess;

    public LogicForHumanGuesses(DictionaryManager dictionaryManager) {
        manager = dictionaryManager;
    }

    public Container<Object> initialize() {
        activeWord = manager.getRandomWord();
        currentState = activeWord.replaceAll("\\w", "_").toLowerCase().replaceAll("ä|ö|ü", "_");
        errors = 0;
        state = GameState.Running;
        alreadyGuessed = new StringJoiner(",");
        return pack();
    }

    private String doGuess(String guess) {
        if (!activeWord.toLowerCase().contains(guess.toLowerCase())) {
            errors++;
            alreadyGuessed.add(guess);
            return currentState;
        } else if (!currentState.contains(guess)) {
            char[] curr = currentState.toCharArray();
            char[] active = activeWord.toCharArray();
            for (int i = 0; i < activeWord.length(); i++) {
                if (String.valueOf(active[i]).equalsIgnoreCase(guess)) {
                    curr[i] = guess.charAt(0);
                }
            }
            alreadyGuessed.add(guess);
            currentState = String.valueOf(curr);
            return currentState;
        } else {
            alreadyGuessed.add(guess);
            return currentState;
        }
    }

    private void updateGameState() {
        if (errors == 15) {
            state = GameState.Lost;
        } else if (currentState.contains("_")) {
            state = GameState.Running;
        } else {
            state = GameState.Won;
        }
    }

    public Container<Object> handleUserInput(String s) {
        if (s.length() > 1) {
            for (String x : s.split("")) {
                doGuess(x);
            }
        } else doGuess(s);
        updateGameState();
        return pack();
    }

    private Container<Object> pack() {
        Container<Object> current = new Container<>();
        return current.addValue("currentState", convertCurrentState())
                .addValue("state", state)
                .addValue("remaining", 15 - errors)
                .addValue("alreadyGuessed", alreadyGuessed.toString());
    }

    private String convertCurrentState() {
        StringJoiner joiner = new StringJoiner(" ");
        for (char c : currentState.toCharArray()) {
            joiner.add(String.valueOf(c));
        }
        return joiner.toString();
    }

    public String getActiveWord() {
        return activeWord;
    }
}
