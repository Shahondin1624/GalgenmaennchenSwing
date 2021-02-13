import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DictionaryManager {
    private Collection<String> nomen = new TreeSet<>();
    private ArrayList<String> randomAccess = new ArrayList<>();
    private String[] recently = new String[20];
    private int position = 0;

    public DictionaryManager() {
        InputStream is = Main.class.getResourceAsStream("/german_words.txt");
        readFile(is, nomen, randomAccess);
    }

    private void readFile(InputStream is, Collection<String> set, Collection<String> randomAccess) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(is))) {
            List<String> strings = new ArrayList<>();
            String line;
            while ((line = in.readLine()) != null) {
                strings.add(line);
            }
            is.close();
            strings.stream().map(string -> string.split(" ")).flatMap(Arrays::stream).forEach(set::add);
            randomAccess.addAll(nomen);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Collection<String> getNomen() {
        return nomen;
    }

    public String getRandomWord() {
        Random random = new Random();
        String word;
        while (!recently(word = randomAccess.get(random.nextInt(randomAccess.size() - 1)))) {

        }
        System.out.printf("%d: %s\n", position, word);
        System.out.println(Arrays.toString(recently));
        return word;
    }

    private boolean recently(String word) {
        for (int i = 0; i < 20; i++) {
            if (word.equalsIgnoreCase(recently[i])) {
                return false;
            }
        }
        recently[position] = word;
        if (position == 19) {
            position = 0;
        } else {
            position++;
        }
        return true;
    }


}
