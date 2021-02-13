import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LogicForAIGuesses {
    private DictionaryManager manager;
    private int pos;
    private String uiStateBefore;

    public LogicForAIGuesses(DictionaryManager dictionaryManager) {
        manager = dictionaryManager;
    }

    public String acceptGame(String currentState, boolean init) {
        if (init) {
            uiStateBefore = currentState;
        } else {
            if (uiStateBefore.equalsIgnoreCase(currentState)) {
                pos++;
            } else {
                pos = 0;
            }
        }
        return aiGuess(currentState, pos);

    }

    private String aiGuess(String currentState, int pos) {
        currentState = currentState.replaceAll(" ", "");
        int length = currentState.length();
        List<String> possibleGuesses = manager.getNomen().stream().filter(word -> word.length() == length).collect(Collectors.toList());
        if (possibleGuesses.size() == 1) {
            return possibleGuesses.get(0);
        } else {
            return getXCommonestChar(possibleGuesses, pos);
        }
    }

    private String getXCommonestChar(List<String> words, int pos) {
        Map<Character, Integer> charOccurrences = new HashMap<>();
        putChars(charOccurrences);
        words.forEach(word -> {
            for (char x : word.toLowerCase().toCharArray()) {
                charOccurrences.put(x, charOccurrences.get(x) + 1);
            }
        });
        ArrayList<String> list = charOccurrences.keySet().stream().map(String::valueOf).sorted((o1, o2) -> {
            int count1 = charOccurrences.get(o1.charAt(0));
            int count2 = charOccurrences.get(o2.charAt(0));
            return Integer.compare(count1, count2);
        }).collect(Collectors.toCollection(ArrayList::new));
        return list.get(pos);
    }

    private void putChars(Map<Character, Integer> map) {
        for (int i = 61; i < 123; i++) {
            map.put((char) i, 0);
        }
        map.put('ö', 0);
        map.put('ä', 0);
        map.put('ü', 0);
        map.put('ß', 0);
    }
}
