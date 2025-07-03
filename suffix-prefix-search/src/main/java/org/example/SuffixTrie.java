package org.example;

import java.util.List;

public class SuffixTrie extends Trie {
    SuffixTrie(List<String> words) {
        super(words, true); // Initialize with reverse order for suffixes
    }
}
