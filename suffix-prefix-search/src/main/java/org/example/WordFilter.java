package org.example;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class WordFilter {

    private final Trie prefixTrie;
    private final SuffixTrie suffixTrie;

    public WordFilter(List<String> words) {
        this.prefixTrie = new Trie(words);
        this.suffixTrie = new SuffixTrie(words);
    }

    public int f(String prefix, String suffix) {
        Set<Integer> prefixMatches = prefixTrie.search(prefix);
        Set<Integer> suffixMatches = suffixTrie.search(suffix);

        prefixMatches.retainAll(suffixMatches); // Find common indices

        return !prefixMatches.isEmpty() ? Collections.max(prefixMatches) : -1;
    }
}
