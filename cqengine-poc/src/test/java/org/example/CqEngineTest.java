package org.example;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.MultiValueAttribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.radix.RadixTreeIndex;
import com.googlecode.cqengine.index.suffix.SuffixTreeIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.googlecode.cqengine.query.QueryFactory.*;

public class CqEngineTest {

    private IndexedCollection<Car> cars;

    // Define attributes for querying
    public static final Attribute<Car, Integer> CAR_ID = new SimpleAttribute<Car, Integer>("id") {
        public Integer getValue(Car car, QueryOptions queryOptions) {
            return car.getId();
        }
    };

    public static final Attribute<Car, String> CAR_NAME = new SimpleAttribute<Car, String>("name") {
        public String getValue(Car car, QueryOptions queryOptions) {
            return car.getName();
        }
    };

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

    public static final Attribute<Car, String> CAR_DESCRIPTION = new SimpleAttribute<Car, String>("description") {
        public String getValue(Car car, QueryOptions queryOptions) {
            return car.getDescription();
        }
    };

    // Multi-value attribute for optionals list
    public static final MultiValueAttribute<Car, String> CAR_OPTIONALS = new MultiValueAttribute<Car, String>("optionals") {
        public Iterable<String> getValues(Car car, QueryOptions queryOptions) {
            return car.getOptionals() != null ? car.getOptionals() : Arrays.asList();
        }
    };

    @BeforeEach
    void setUp() {
        cars = new ConcurrentIndexedCollection<>();

        // Add sample data
        Car car1 = new Car();
        car1.setId(1);
        car1.setName("Mustang GT");
        car1.setMake("Ford");
        car1.setYear(2023);
        car1.setHp(460);
        car1.setDescription("Powerful American muscle car");
        car1.setOptionals(Arrays.asList("Leather Seats", "Navigation", "Sunroof"));

        Car car2 = new Car();
        car2.setId(2);
        car2.setName("Civic Type R");
        car2.setMake("Honda");
        car2.setYear(2022);
        car2.setHp(315);
        car2.setDescription("High-performance compact car");
        car2.setOptionals(Arrays.asList("Sport Package", "Navigation"));

        Car car3 = new Car();
        car3.setId(3);
        car3.setName("Model S");
        car3.setMake("Tesla");
        car3.setYear(2024);
        car3.setHp(670);
        car3.setDescription("Electric luxury sedan");
        car3.setOptionals(Arrays.asList("Autopilot", "Premium Interior", "Enhanced Audio"));

        Car car4 = new Car();
        car4.setId(4);
        car4.setName("911 Turbo");
        car4.setMake("Porsche");
        car4.setYear(2023);
        car4.setHp(640);
        car4.setDescription("German sports car excellence");
        car4.setOptionals(Arrays.asList("Sport Chrono", "Carbon Fiber", "Ceramic Brakes"));

        Car car5 = new Car();
        car5.setId(5);
        car5.setName("Camry");
        car5.setMake("Toyota");
        car5.setYear(2022);
        car5.setHp(203);
        car5.setDescription("Reliable family sedan");
        car5.setOptionals(Arrays.asList("Safety Package", "Navigation"));

        cars.addAll(Arrays.asList(car1, car2, car3, car4, car5));
    }

    @Test
    void testSimpleEqualQuery() {
        ResultSet<Car> results = cars.retrieve(equal(CAR_MAKE, "Tesla"));

        Assertions.assertEquals(1, results.size());
        Car tesla = results.iterator().next();
        Assertions.assertEquals("Model S", tesla.getName());
        Assertions.assertEquals("Tesla", tesla.getMake());
    }

    @Test
    void testRangeQuery() {
        // Find cars with horsepower between 300 and 500
        ResultSet<Car> results = cars.retrieve(
            and(
                greaterThanOrEqualTo(CAR_HP, 300),
                lessThanOrEqualTo(CAR_HP, 500)
            )
        );

        Assertions.assertEquals(2, results.size());
        for (Car car : results) {
            Assertions.assertTrue(car.getHp() >= 300 && car.getHp() <= 500);
        }
    }

    @Test
    void testMultipleConditionsQuery() {
        // Find Ford cars from 2023 with more than 400 HP
        ResultSet<Car> results = cars.retrieve(
            and(
                equal(CAR_MAKE, "Ford"),
                equal(CAR_YEAR, 2023),
                greaterThan(CAR_HP, 400)
            )
        );

        Assertions.assertEquals(1, results.size());
        Car ford = results.iterator().next();
        Assertions.assertEquals("Mustang GT", ford.getName());
    }

    @Test
    void testOrQuery() {
        // Find cars that are either Tesla or Porsche
        ResultSet<Car> results = cars.retrieve(
            or(
                equal(CAR_MAKE, "Tesla"),
                equal(CAR_MAKE, "Porsche")
            )
        );

        Assertions.assertEquals(2, results.size());
        for (Car car : results) {
            Assertions.assertTrue(car.getMake().equals("Tesla") || car.getMake().equals("Porsche"));
        }
    }

    @Test
    void testStringContainsQuery() {
        // Find cars with "sedan" in description
        ResultSet<Car> results = cars.retrieve(contains(CAR_DESCRIPTION, "sedan"));

        Assertions.assertEquals(2, results.size());
        for (Car car : results) {
            Assertions.assertTrue(car.getDescription().toLowerCase().contains("sedan"));
        }
    }

    @Test
    void testStringStartsWithQuery() {
        // Find cars with names starting with "M"
        ResultSet<Car> results = cars.retrieve(startsWith(CAR_NAME, "M"));

        Assertions.assertEquals(2, results.size());
        for (Car car : results) {
            Assertions.assertTrue(car.getName().startsWith("M"));
        }
    }

    @Test
    void testMultiValueAttributeQuery() {
        // Find cars that have "Navigation" as an optional
        ResultSet<Car> results = cars.retrieve(equal(CAR_OPTIONALS, "Navigation"));

        Assertions.assertEquals(3, results.size());
        for (Car car : results) {
            Assertions.assertTrue(car.getOptionals().contains("Navigation"));
        }
    }

    @Test
    void testNotQuery() {
        // Find cars that are NOT from Honda
        ResultSet<Car> results = cars.retrieve(not(equal(CAR_MAKE, "Honda")));

        Assertions.assertEquals(4, results.size());
        for (Car car : results) {
            Assertions.assertNotEquals("Honda", car.getMake());
        }
    }

    @Test
    void testInQuery() {
        // Find cars from specific makes
        ResultSet<Car> results = cars.retrieve(
            in(CAR_MAKE, "Ford", "Tesla", "Porsche")
        );

        Assertions.assertEquals(3, results.size());
        for (Car car : results) {
            Assertions.assertTrue(Arrays.asList("Ford", "Tesla", "Porsche").contains(car.getMake()));
        }
    }

    @Test
    void testComplexNestedQuery() {
        // Find high-performance cars (HP > 300) that are either from 2023 or have sport features
        Query<Car> complexQuery = and(
            greaterThan(CAR_HP, 300),
            or(
                equal(CAR_YEAR, 2023),
                or(
                    equal(CAR_OPTIONALS, "Sport Package"),
                    equal(CAR_OPTIONALS, "Sport Chrono")
                )
            )
        );

        ResultSet<Car> results = cars.retrieve(complexQuery);

        Assertions.assertTrue(results.size() > 0);
        for (Car car : results) {
            Assertions.assertTrue(car.getHp() > 300);
            Assertions.assertTrue(car.getYear() == 2023 ||
                      car.getOptionals().contains("Sport Package") ||
                      car.getOptionals().contains("Sport Chrono"));
        }
    }

    @Test
    void testIndexedPerformance() {
        // Add indexes for better performance
        cars.addIndex(HashIndex.onAttribute(CAR_MAKE));
        cars.addIndex(SuffixTreeIndex.onAttribute(CAR_NAME));

        // Test query performance with indexes
        long startTime = System.nanoTime();
        ResultSet<Car> results = cars.retrieve(equal(CAR_MAKE, "Tesla"));
        long endTime = System.nanoTime();

        Assertions.assertEquals(1, results.size());
        // With small dataset, performance difference won't be noticeable,
        // but this demonstrates how to add indexes
        Assertions.assertTrue(endTime - startTime >= 0);
    }

    @Test
    void testCountResults() {
        // Count cars by make
        int fordCount = cars.retrieve(equal(CAR_MAKE, "Ford")).size();
        int teslaCount = cars.retrieve(equal(CAR_MAKE, "Tesla")).size();
        int totalCars = cars.size();

        Assertions.assertEquals(1, fordCount);
        Assertions.assertEquals(1, teslaCount);
        Assertions.assertEquals(5, totalCars);
    }

    @Test
    void testQueryWithMultipleOptionals() {
        // Find cars that have both "Navigation" AND another specific optional
        ResultSet<Car> results = cars.retrieve(
            and(
                equal(CAR_OPTIONALS, "Navigation"),
                equal(CAR_OPTIONALS, "Sunroof")
            )
        );

        Assertions.assertEquals(1, results.size());
        Car car = results.iterator().next();
        Assertions.assertTrue(car.getOptionals().contains("Navigation"));
        Assertions.assertTrue(car.getOptionals().contains("Sunroof"));
    }

    @Test
    void testEmptyResults() {
        // Query that should return no results
        ResultSet<Car> results = cars.retrieve(
            and(
                equal(CAR_MAKE, "Lamborghini"),
                equal(CAR_YEAR, 2023)
            )
        );

        Assertions.assertEquals(0, results.size());
        Assertions.assertTrue(results.isEmpty());
    }

    @Test
    void testYearRangeQuery() {
        // Find cars from 2022-2023
        ResultSet<Car> results = cars.retrieve(
            and(
                greaterThanOrEqualTo(CAR_YEAR, 2022),
                lessThanOrEqualTo(CAR_YEAR, 2023)
            )
        );

        Assertions.assertEquals(4, results.size());
        for (Car car : results) {
            Assertions.assertTrue(car.getYear() >= 2022 && car.getYear() <= 2023);
        }
    }
}
