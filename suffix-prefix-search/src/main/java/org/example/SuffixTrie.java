package org.example;

import java.util.ArrayList;
import java.util.Set;
import java.util.List;

public class SuffixTrie extends Trie {
    SuffixTrie(List<String> words) {
        List<String> reversedWords = new ArrayList<>();
        for (String word : words) {
            StringBuilder reversedWord = new StringBuilder(word).reverse();
            reversedWords.add(reversedWord.toString());
        }
        // Flexible constructor bodies
        super(reversedWords); // Initialize with reverse order for suffixes
    }

    @Override
    public Set<Integer> search(String query) {
        return super.search(new StringBuilder(query).reverse().toString());
    }
}
