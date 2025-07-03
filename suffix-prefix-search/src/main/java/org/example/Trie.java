package org.example;

import java.util.ArrayList;
import java.util.List;

public class Trie {
    private Trie[] children;
    private List<Integer> indexes = new ArrayList<>();

    private Trie(){
        this.children = new Trie[26]; // Assuming only lowercase letters a-z
    }

    public Trie(List<String> words){
        this(words, false); // Default to normal order
    }

    protected Trie(List<String> words, boolean suffix) {
        this(); // Ensure children is initialized
        for(int index = 0; index < words.size(); index++) {

            this.indexes.add(index);
            String word = words.get(index);

            if (suffix) {
                word = new StringBuilder(word).reverse().toString();
            }
            Trie node = this;
            for (char c : word.toCharArray()) {
                int idx = c - 'a';
                if (node.children[idx] == null) {
                    node.children[idx] = new Trie();
                }
                node = node.children[idx];
                node.indexes.add(index);
            }
        }
    }

    List<Integer> search(String query) {
        return search(query, false); // Default to normal prefix search
    }

    List<Integer> search(String query, boolean suffix) {
        Trie node = this;
        if(suffix) {
            query = new StringBuilder(query).reverse().toString(); // Reverse for suffix search
        }
        for (char ch : query.toCharArray()){
            int idx = ch - 'a';

            if (node.children[idx] == null) {
                return new ArrayList<>(); // No matches found
            }

            node = node.children[idx];
        }
        return node.indexes;
    }
}

