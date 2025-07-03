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

    protected Trie(List<String> words, boolean reverse) {
        this(); // Ensure children is initialized
        for(int index = 0; index < words.size(); index++) {

            this.indexes.add(index);
            String word = words.get(index);

            if (reverse) {
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

    List<Integer> search(String prefix) {
        Trie node = this;
        for (char ch : prefix.toCharArray()){
            int idx = ch - 'a';

            if (node.children[idx] == null) {
                return new ArrayList<>(); // No matches found
            }

            node = node.children[idx];
        }
        return node.indexes;
    }
}

