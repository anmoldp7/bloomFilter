import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

/*
 * Driver program.
 */
public class Main {
    private static final int FILTER_SIZE = 999983;
    private static final String DICTIONARY_CSV = "res/dictionary_words.csv";
    private static boolean[] BLOOM_FILTER;
    private static Set<String> DICTIONARY_WORDS;
    private static final String REPL_END = "-1";

    private static final void prepareDictionary() {
        String commaSeparatedWords = "";

        try {
            commaSeparatedWords = Files.readString(Paths.get(DICTIONARY_CSV));
        } catch (IOException e) {
            System.out.println("IOException while reading dictionary words CSV " + e);
        }
        DICTIONARY_WORDS =
                Arrays.stream(
                        commaSeparatedWords
                                .split(","))
                        .collect(Collectors.toSet());
    }

    private static final int modex(long a, long b) {
        long c = 1L;
        while (b > 0) {
            if (b % 2 == 1) {
                c *= a;
                c %= FILTER_SIZE;
            }
            a *= a;
            a %= FILTER_SIZE;
            b /= 2;
        }
        return (int) c;
    }

    private static final int getSumHashFunc(String word) {
        int hash = 0;
        for (int i = 0; i < word.length(); i++) {
            hash += word.charAt(i);
            hash %= FILTER_SIZE;
        }
        return hash;
    }

    private static final int getProdHashFunc(String word) {
        long prod = 1L;
        for (int i = 0; i < word.length(); i++) {
            prod *= word.charAt(i);
            prod %= FILTER_SIZE;
        }
        return (int) prod;
    }

    private static final int getExpHashFunc(String word) {
        if (word.isEmpty()) {
            throw new RuntimeException("Empty word!");
        }
        int a = word.charAt(0);
        for (int i = 1; i < word.length(); i++) {
            a = modex(a, word.charAt(i));
        }
        return a;
    }

    private static final void insertWordIntoBloomFilter(String word) {
        BLOOM_FILTER[getSumHashFunc(word)] = true;
        BLOOM_FILTER[getProdHashFunc(word)] = true;
        BLOOM_FILTER[getExpHashFunc(word)] = true;
    }

    private static final boolean bloomFilterGet(String word) {
        return BLOOM_FILTER[getSumHashFunc(word)]
                && BLOOM_FILTER[getProdHashFunc(word)]
                && BLOOM_FILTER[getExpHashFunc(word)];
    }

    private static final void initializeBloomFilter() {
        BLOOM_FILTER = new boolean[FILTER_SIZE + 1];
        prepareDictionary();
        for (String word: DICTIONARY_WORDS) {
            insertWordIntoBloomFilter(word);
        }
    }

    public static void main(String[] args) {
        initializeBloomFilter();
        String userInputString;
        Scanner sc = new Scanner(System.in);
        userInputString = sc.next();
        while (!REPL_END.equals(userInputString)) {
            System.out.println("BLOOM FILTER GET: " + bloomFilterGet(userInputString));
            System.out.println("IS ACTUAL WORD: " + DICTIONARY_WORDS.contains(userInputString));
            userInputString = sc.next();
        }
    }
}