package org.example.guavapoc.runner;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.RangeSet;
import com.google.common.collect.Table;
import com.google.common.collect.TreeRangeMap;
import com.google.common.collect.TreeRangeSet;
import com.google.common.eventbus.EventBus;
import org.example.guavapoc.event.OrderEvent;
import org.example.guavapoc.event.OrderEventListener;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class GuavaDemoRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        immutableCollections();
        multimapDemo();
        biMapDemo();
        tableDemo();
        stringsDemo();
        splitterJoinerDemo();
        preconditionsDemo();
        cacheDemo();
        rangeSetDemo();
        rangeMapDemo();
        eventBusDemo();
    }

    private void immutableCollections() {
        System.out.println("\n=== Immutable Collections ===");

        ImmutableList<String> list = ImmutableList.of("alpha", "beta", "gamma");
        System.out.println("ImmutableList: " + list);

        ImmutableSet<Integer> set = ImmutableSet.of(1, 2, 3, 4, 5);
        System.out.println("ImmutableSet: " + set);

        ImmutableMap<String, Integer> map = ImmutableMap.of("one", 1, "two", 2, "three", 3);
        System.out.println("ImmutableMap: " + map);

        try {
            list.add("delta");
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify ImmutableList: " + e.getClass().getSimpleName());
        }
    }

    private void multimapDemo() {
        System.out.println("\n=== Multimap ===");

        Multimap<String, String> courseStudents = ArrayListMultimap.create();
        courseStudents.put("Math", "Alice");
        courseStudents.put("Math", "Bob");
        courseStudents.put("Math", "Carol");
        courseStudents.put("Science", "Alice");
        courseStudents.put("Science", "Dave");

        System.out.println("Math students: " + courseStudents.get("Math"));
        System.out.println("Science students: " + courseStudents.get("Science"));
        System.out.println("Total entries: " + courseStudents.size());
    }

    private void biMapDemo() {
        System.out.println("\n=== BiMap ===");

        var countryCode = HashBiMap.<String, String>create();
        countryCode.put("BR", "Brazil");
        countryCode.put("US", "United States");
        countryCode.put("JP", "Japan");

        System.out.println("BR -> " + countryCode.get("BR"));
        System.out.println("Japan -> " + countryCode.inverse().get("Japan"));
    }

    private void tableDemo() {
        System.out.println("\n=== Table ===");

        Table<String, String, Double> grades = HashBasedTable.create();
        grades.put("Alice", "Math", 9.5);
        grades.put("Alice", "Science", 8.7);
        grades.put("Bob", "Math", 7.2);
        grades.put("Bob", "Science", 9.1);

        System.out.println("Alice's Math grade: " + grades.get("Alice", "Math"));
        System.out.println("All Math grades: " + grades.column("Math"));
        System.out.println("Alice's grades: " + grades.row("Alice"));
    }

    private void stringsDemo() {
        System.out.println("\n=== Strings Utility ===");

        System.out.println("isNullOrEmpty(null): " + Strings.isNullOrEmpty(null));
        System.out.println("isNullOrEmpty(\"\"): " + Strings.isNullOrEmpty(""));
        System.out.println("isNullOrEmpty(\"hello\"): " + Strings.isNullOrEmpty("hello"));
        System.out.println("padStart(\"42\", 5, '0'): " + Strings.padStart("42", 5, '0'));
        System.out.println("padEnd(\"hi\", 6, '!'): " + Strings.padEnd("hi", 6, '!'));
        System.out.println("commonPrefix(\"hello\", \"help\"): " + Strings.commonPrefix("hello", "help"));
        System.out.println("repeat(\"ab\", 3): " + Strings.repeat("ab", 3));
    }

    private void splitterJoinerDemo() {
        System.out.println("\n=== Splitter & Joiner ===");

        List<String> parts = Splitter.on(',')
                .trimResults()
                .omitEmptyStrings()
                .splitToList("foo, bar,,  baz , qux");
        System.out.println("Split: " + parts);

        String joined = Joiner.on(" | ").skipNulls().join("alpha", null, "beta", "gamma", null);
        System.out.println("Joined: " + joined);

        String mapJoined = Joiner.on(", ").withKeyValueSeparator("=")
                .join(Map.of("host", "localhost", "port", "8080"));
        System.out.println("Map joined: " + mapJoined);
    }

    private void preconditionsDemo() {
        System.out.println("\n=== Preconditions ===");

        try {
            validateAge(-1);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        try {
            validateName(null);
        } catch (NullPointerException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }

    private void validateAge(int age) {
        com.google.common.base.Preconditions.checkArgument(age >= 0, "Age must be non-negative, got: %s", age);
    }

    private void validateName(String name) {
        com.google.common.base.Preconditions.checkNotNull(name, "Name must not be null");
    }

    private void cacheDemo() throws Exception {
        System.out.println("\n=== Cache ===");

        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                .maximumSize(3)
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .recordStats()
                .build(new CacheLoader<>() {
                    @Override
                    public String load(String key) {
                        System.out.println("  Loading value for key: " + key);
                        return "value-for-" + key;
                    }
                });

        System.out.println("Get 'a': " + cache.get("a"));
        System.out.println("Get 'a' (cached): " + cache.get("a"));
        System.out.println("Get 'b': " + cache.get("b"));
        System.out.println("Get 'c': " + cache.get("c"));
        System.out.println("Get 'd' (evicts oldest): " + cache.get("d"));

        CacheStats stats = cache.stats();
        System.out.println("Hit count: " + stats.hitCount());
        System.out.println("Miss count: " + stats.missCount());
        System.out.println("Hit rate: " + stats.hitRate());
    }

    private void rangeSetDemo() {
        System.out.println("\n=== RangeSet ===");

        RangeSet<Integer> rangeSet = TreeRangeSet.create();
        rangeSet.add(Range.closed(1, 10));
        rangeSet.add(Range.closed(15, 20));
        rangeSet.add(Range.closed(5, 17));

        System.out.println("RangeSet (merged): " + rangeSet);
        System.out.println("Contains 12: " + rangeSet.contains(12));
        System.out.println("Contains 22: " + rangeSet.contains(22));
        System.out.println("Complement: " + rangeSet.complement());
    }

    private void rangeMapDemo() {
        System.out.println("\n=== RangeMap ===");

        RangeMap<Integer, String> gradeMap = TreeRangeMap.create();
        gradeMap.put(Range.closed(90, 100), "A");
        gradeMap.put(Range.closed(80, 89), "B");
        gradeMap.put(Range.closed(70, 79), "C");
        gradeMap.put(Range.closed(60, 69), "D");
        gradeMap.put(Range.closed(0, 59), "F");

        System.out.println("Score 95 -> " + gradeMap.get(95));
        System.out.println("Score 82 -> " + gradeMap.get(82));
        System.out.println("Score 55 -> " + gradeMap.get(55));
    }

    private void eventBusDemo() {
        System.out.println("\n=== EventBus ===");

        EventBus eventBus = new EventBus("orders");
        eventBus.register(new OrderEventListener());

        eventBus.post(new OrderEvent("ORD-001", 150.00));
        eventBus.post(new OrderEvent("ORD-002", 299.99));
    }
}
