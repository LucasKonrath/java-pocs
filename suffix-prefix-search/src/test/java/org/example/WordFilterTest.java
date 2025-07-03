package org.example;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class WordFilterTest {
    @Test
    void testBasicFunctionality() {
        WordFilter wf = new WordFilter(Arrays.asList("apple", "apply", "ape", "banana"));
        assertEquals(0, wf.f("app", "le")); // "apple"
        assertEquals(1, wf.f("app", "ly")); // "apply"
        assertEquals(2, wf.f("ap", "e"));   // "ape"
        assertEquals(3, wf.f("ban", "na")); // "banana"
    }

    @Test
    void testNoMatch() {
        WordFilter wf = new WordFilter(Arrays.asList("apple", "apply", "ape", "banana"));
        assertEquals(-1, wf.f("app", "na")); // No such word
        assertEquals(-1, wf.f("ban", "le")); // No such word
    }

    @Test
    void testMultipleMatchesReturnsLargestIndex() {
        WordFilter wf = new WordFilter(Arrays.asList("apple", "apple", "apple"));
        assertEquals(2, wf.f("app", "le")); // Should return the largest index
    }

    @Test
    void testEmptyPrefixOrSuffix() {
        WordFilter wf = new WordFilter(Arrays.asList("apple", "apply", "ape", "banana"));
        assertEquals(3, wf.f("", "")); // Should match the last word
        assertEquals(3, wf.f("", "na")); // Should match "banana"
        assertEquals(2, wf.f("ap", "")); // Should match "ape"
    }
}

