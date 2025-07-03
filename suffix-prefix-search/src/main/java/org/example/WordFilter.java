package org.example;

import java.util.List;

public class WordFilter {

    private Trie prefixTrie;
    private SuffixTrie suffixTrie;

    public WordFilter(List<String> words) {
        this.prefixTrie = new Trie(words);
        this.suffixTrie = new SuffixTrie(words);
    }

    public int f(String prefix, String suffix) {
        List<Integer> prefixMatches = prefixTrie.search(prefix);
        List<Integer> suffixMatches = suffixTrie.search(suffix);

        if(prefixMatches.isEmpty() || suffixMatches.isEmpty()) {
            return -1; // No matches found
        }

        int i = prefixMatches.size() - 1;
        int j = suffixMatches.size() - 1;
        while (i >= 0 && j >= 0) {
            int prefixIndex = prefixMatches.get(i);
            int suffixIndex = suffixMatches.get(j);

            if (prefixIndex == suffixIndex) {
                return prefixIndex; // Found a match
            } else if (prefixIndex > suffixIndex) {
                i--; // Move to the next smaller prefix index
            } else {
                j--; // Move to the next smaller suffix index
            }
        }
        return -1; // No common index found
    }
}
