package org.example;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.MultiValueAttribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.index.suffix.SuffixTreeIndex;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.googlecode.cqengine.query.QueryFactory.*;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
public class CQEngineVsForLoopBenchmark {

    // CQEngine attributes
    public static final Attribute<Car, String> CAR_MAKE = new SimpleAttribute<Car, String>("make") {
        public String getValue(Car car, QueryOptions queryOptions) {
            return car.getMake();
        }
    };

    public static final Attribute<Car, Integer> CAR_YEAR = new SimpleAttribute<Car, Integer>("year") {
        public Integer getValue(Car car, QueryOptions queryOptions) {
            return car.getYear();
        }
    };

    public static final Attribute<Car, Integer> CAR_HP = new SimpleAttribute<Car, Integer>("hp") {
        public Integer getValue(Car car, QueryOptions queryOptions) {
            return car.getHp();
        }
    };

    public static final Attribute<Car, String> CAR_NAME = new SimpleAttribute<Car, String>("name") {
        public String getValue(Car car, QueryOptions queryOptions) {
            return car.getName();
        }
    };

    public static final MultiValueAttribute<Car, String> CAR_OPTIONALS = new MultiValueAttribute<Car, String>("optionals") {
        public Iterable<String> getValues(Car car, QueryOptions queryOptions) {
            return car.getOptionals() != null ? car.getOptionals() : Collections.emptyList();
        }
    };

    private IndexedCollection<Car> cqEngineCollection;
    private IndexedCollection<Car> cqEngineIndexedCollection;
    private List<Car> plainList;

    @Setup
    public void setup() {
        // Create test data
        List<Car> cars = generateTestData(10000);

        // Setup plain list
        plainList = new ArrayList<>(cars);

        // Setup CQEngine without indexes
        cqEngineCollection = new ConcurrentIndexedCollection<>();
        cqEngineCollection.addAll(cars);

        // Setup CQEngine with indexes
        cqEngineIndexedCollection = new ConcurrentIndexedCollection<>();
        cqEngineIndexedCollection.addIndex(HashIndex.onAttribute(CAR_MAKE));
        cqEngineIndexedCollection.addIndex(NavigableIndex.onAttribute(CAR_YEAR));
        cqEngineIndexedCollection.addIndex(NavigableIndex.onAttribute(CAR_HP));
        cqEngineIndexedCollection.addIndex(SuffixTreeIndex.onAttribute(CAR_NAME));
        cqEngineIndexedCollection.addIndex(HashIndex.onAttribute(CAR_OPTIONALS));
        cqEngineIndexedCollection.addAll(cars);
    }

    private List<Car> generateTestData(int size) {
        List<Car> cars = new ArrayList<>();
        String[] makes = {"Ford", "Honda", "Tesla", "Porsche", "Toyota", "BMW", "Mercedes", "Audi", "Chevrolet", "Nissan"};
        String[] models = {"Sedan", "Coupe", "SUV", "Hatchback", "Convertible", "Truck", "Wagon"};
        String[] optionals = {"Navigation", "Leather Seats", "Sunroof", "Sport Package", "Premium Audio", "Autopilot"};

        Random random = new Random(42); // Fixed seed for reproducible results

        for (int i = 0; i < size; i++) {
            Car car = new Car();
            car.setId(i);
            car.setMake(makes[random.nextInt(makes.length)]);
            car.setName(car.getMake() + " " + models[random.nextInt(models.length)] + " " + (i % 100));
            car.setYear(2015 + random.nextInt(10)); // Years 2015-2024
            car.setHp(150 + random.nextInt(500)); // HP 150-650
            car.setDescription("Description for car " + i);

            // Random optionals
            List<String> carOptionals = new ArrayList<>();
            int numOptionals = random.nextInt(4) + 1; // 1-4 optionals
            for (int j = 0; j < numOptionals; j++) {
                String optional = optionals[random.nextInt(optionals.length)];
                if (!carOptionals.contains(optional)) {
                    carOptionals.add(optional);
                }
            }
            car.setOptionals(carOptionals);

            cars.add(car);
        }

        return cars;
    }

    // Benchmark 1: Simple equality query by make
    @Benchmark
    public void cqEngineSimpleQuery(Blackhole bh) {
        try (ResultSet<Car> results = cqEngineCollection.retrieve(equal(CAR_MAKE, "Tesla"))) {
            for (Car car : results) {
                bh.consume(car);
            }
        }
    }

    @Benchmark
    public void cqEngineIndexedSimpleQuery(Blackhole bh) {
        try (ResultSet<Car> results = cqEngineIndexedCollection.retrieve(equal(CAR_MAKE, "Tesla"))) {
            for (Car car : results) {
                bh.consume(car);
            }
        }
    }

    @Benchmark
    public void forLoopSimpleQuery(Blackhole bh) {
        for (Car car : plainList) {
            if ("Tesla".equals(car.getMake())) {
                bh.consume(car);
            }
        }
    }

    @Benchmark
    public void streamSimpleQuery(Blackhole bh) {
        plainList.stream()
                .filter(car -> "Tesla".equals(car.getMake()))
                .forEach(bh::consume);
    }

    // Benchmark 2: Range query (HP between 300-500)
    @Benchmark
    public void cqEngineRangeQuery(Blackhole bh) {
        try (ResultSet<Car> results = cqEngineCollection.retrieve(
            and(
                greaterThanOrEqualTo(CAR_HP, 300),
                lessThanOrEqualTo(CAR_HP, 500)
            )
        )) {
            for (Car car : results) {
                bh.consume(car);
            }
        }
    }

    @Benchmark
    public void cqEngineIndexedRangeQuery(Blackhole bh) {
        try (ResultSet<Car> results = cqEngineIndexedCollection.retrieve(
            and(
                greaterThanOrEqualTo(CAR_HP, 300),
                lessThanOrEqualTo(CAR_HP, 500)
            )
        )) {
            for (Car car : results) {
                bh.consume(car);
            }
        }
    }

    @Benchmark
    public void forLoopRangeQuery(Blackhole bh) {
        for (Car car : plainList) {
            if (car.getHp() >= 300 && car.getHp() <= 500) {
                bh.consume(car);
            }
        }
    }

    @Benchmark
    public void streamRangeQuery(Blackhole bh) {
        plainList.stream()
                .filter(car -> car.getHp() >= 300 && car.getHp() <= 500)
                .forEach(bh::consume);
    }

    // Benchmark 3: Complex multi-condition query
    @Benchmark
    public void cqEngineComplexQuery(Blackhole bh) {
        try (ResultSet<Car> results = cqEngineCollection.retrieve(
            and(
                equal(CAR_MAKE, "Ford"),
                greaterThanOrEqualTo(CAR_YEAR, 2020),
                greaterThan(CAR_HP, 250)
            )
        )) {
            for (Car car : results) {
                bh.consume(car);
            }
        }
    }

    @Benchmark
    public void cqEngineIndexedComplexQuery(Blackhole bh) {
        try (ResultSet<Car> results = cqEngineIndexedCollection.retrieve(
            and(
                equal(CAR_MAKE, "Ford"),
                greaterThanOrEqualTo(CAR_YEAR, 2020),
                greaterThan(CAR_HP, 250)
            )
        )) {
            for (Car car : results) {
                bh.consume(car);
            }
        }
    }

    @Benchmark
    public void forLoopComplexQuery(Blackhole bh) {
        for (Car car : plainList) {
            if ("Ford".equals(car.getMake()) &&
                car.getYear() >= 2020 &&
                car.getHp() > 250) {
                bh.consume(car);
            }
        }
    }

    @Benchmark
    public void streamComplexQuery(Blackhole bh) {
        plainList.stream()
                .filter(car -> "Ford".equals(car.getMake()) &&
                              car.getYear() >= 2020 &&
                              car.getHp() > 250)
                .forEach(bh::consume);
    }

    // Benchmark 4: String search query
    @Benchmark
    public void cqEngineStringQuery(Blackhole bh) {
        try (ResultSet<Car> results = cqEngineCollection.retrieve(contains(CAR_NAME, "Sedan"))) {
            for (Car car : results) {
                bh.consume(car);
            }
        }
    }

    @Benchmark
    public void cqEngineIndexedStringQuery(Blackhole bh) {
        try (ResultSet<Car> results = cqEngineIndexedCollection.retrieve(contains(CAR_NAME, "Sedan"))) {
            for (Car car : results) {
                bh.consume(car);
            }
        }
    }

    @Benchmark
    public void forLoopStringQuery(Blackhole bh) {
        for (Car car : plainList) {
            if (car.getName().contains("Sedan")) {
                bh.consume(car);
            }
        }
    }

    @Benchmark
    public void streamStringQuery(Blackhole bh) {
        plainList.stream()
                .filter(car -> car.getName().contains("Sedan"))
                .forEach(bh::consume);
    }

    // Benchmark 5: Multi-value attribute query
    @Benchmark
    public void cqEngineMultiValueQuery(Blackhole bh) {
        try (ResultSet<Car> results = cqEngineCollection.retrieve(equal(CAR_OPTIONALS, "Navigation"))) {
            for (Car car : results) {
                bh.consume(car);
            }
        }
    }

    @Benchmark
    public void cqEngineIndexedMultiValueQuery(Blackhole bh) {
        try (ResultSet<Car> results = cqEngineIndexedCollection.retrieve(equal(CAR_OPTIONALS, "Navigation"))) {
            for (Car car : results) {
                bh.consume(car);
            }
        }
    }

    @Benchmark
    public void forLoopMultiValueQuery(Blackhole bh) {
        for (Car car : plainList) {
            if (car.getOptionals() != null && car.getOptionals().contains("Navigation")) {
                bh.consume(car);
            }
        }
    }

    @Benchmark
    public void streamMultiValueQuery(Blackhole bh) {
        plainList.stream()
                .filter(car -> car.getOptionals() != null && car.getOptionals().contains("Navigation"))
                .forEach(bh::consume);
    }

    // Benchmark 6: Count operations
    @Benchmark
    public int cqEngineCount() {
        try (ResultSet<Car> results = cqEngineCollection.retrieve(equal(CAR_MAKE, "Honda"))) {
            return results.size();
        }
    }

    @Benchmark
    public int cqEngineIndexedCount() {
        try (ResultSet<Car> results = cqEngineIndexedCollection.retrieve(equal(CAR_MAKE, "Honda"))) {
            return results.size();
        }
    }

    @Benchmark
    public int forLoopCount() {
        int count = 0;
        for (Car car : plainList) {
            if ("Honda".equals(car.getMake())) {
                count++;
            }
        }
        return count;
    }

    @Benchmark
    public long streamCount() {
        return plainList.stream()
                .filter(car -> "Honda".equals(car.getMake()))
                .count();
    }

    // Benchmark 7: OR query
    @Benchmark
    public void cqEngineOrQuery(Blackhole bh) {
        try (ResultSet<Car> results = cqEngineCollection.retrieve(
            or(
                equal(CAR_MAKE, "Tesla"),
                equal(CAR_MAKE, "Porsche"),
                equal(CAR_MAKE, "BMW")
            )
        )) {
            for (Car car : results) {
                bh.consume(car);
            }
        }
    }

    @Benchmark
    public void cqEngineIndexedOrQuery(Blackhole bh) {
        try (ResultSet<Car> results = cqEngineIndexedCollection.retrieve(
            or(
                equal(CAR_MAKE, "Tesla"),
                equal(CAR_MAKE, "Porsche"),
                equal(CAR_MAKE, "BMW")
            )
        )) {
            for (Car car : results) {
                bh.consume(car);
            }
        }
    }

    @Benchmark
    public void forLoopOrQuery(Blackhole bh) {
        for (Car car : plainList) {
            String make = car.getMake();
            if ("Tesla".equals(make) || "Porsche".equals(make) || "BMW".equals(make)) {
                bh.consume(car);
            }
        }
    }

    @Benchmark
    public void streamOrQuery(Blackhole bh) {
        Set<String> targetMakes = Set.of("Tesla", "Porsche", "BMW");
        plainList.stream()
                .filter(car -> targetMakes.contains(car.getMake()))
                .forEach(bh::consume);
    }
}
