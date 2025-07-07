package org.example;

import java.util.*;

public class Trie {
    private final HashMap<Character, Trie> children;
    private final Set<Integer> indexes;

    private Trie(){
        this.children = new HashMap<>();
        this.indexes = new HashSet<>();
    }

    protected Trie(List<String> words) {
        this(); // Ensure children is initialized
        for(int index = 0; index < words.size(); index++) {

            this.indexes.add(index);
            String word = words.get(index);

            Trie node = this;
            for (char c : word.toCharArray()) {
                if (node.children.get(c) == null) {
                    node.children.put(c, new Trie());
                }
                node = node.children.get(c);
                node.indexes.add(index);
            }
        }
    }

    Set<Integer> search(String query) {
        Trie node = this;

        for (char ch : query.toCharArray()){

            if (node.children.get(ch) == null) {
                return new HashSet<>(); // No matches found
            }

            node = node.children.get(ch);
        }
        return new HashSet<>(node.indexes); // to avoid returning the same reference
    }
}

