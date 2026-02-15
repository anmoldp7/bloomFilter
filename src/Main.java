// Refactored Main.java

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        try {
            Set<String> bloomFilter = new HashSet<>();
            loadBloomFilter(bloomFilter, "data.txt");
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter a word to check:");
            String input = scanner.nextLine();

            if (isInputValid(input)) {
                checkWord(bloomFilter, input);
            } else {
                System.out.println("Invalid input.");
            }
            scanner.close();
        } catch (IOException e) {
            System.err.println("Error loading bloom filter: " + e.getMessage());
        }
    }

    private static void loadBloomFilter(Set<String> bloomFilter, String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                bloomFilter.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            throw new IOException("Failed to read file: " + filePath, e);
        }
    }

    private static boolean isInputValid(String input) {
        return input != null && !input.trim().isEmpty();
    }

    private static void checkWord(Set<String> bloomFilter, String input) {
        if (bloomFilter.contains(input.toLowerCase())) {
            System.out.println(input + " is possibly in the bloom filter.");
        } else {
            System.out.println(input + " is definitely not in the bloom filter.");
        }
    }
}