package org.example.junit6poc.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@Tag("fast")
class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    void add_basic() {
        assertEquals(5, calculator.add(2,3));
    }



    @Test
    void divide_by_zero() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> calculator.divide(1,0));
        assertEquals("Division by zero", ex.getMessage());
    }

    @Test
    void divide_timeout() {
        assertTimeout(Duration.ofMillis(50), () -> calculator.divide(10,2));
    }

    @Test
    @EnabledOnOs(OS.MAC)
    void onlyOnMac() {
        assertEquals(4, calculator.multiply(2,2));
    }

    @Test
    @Disabled("Example of a disabled test")
    void disabledExample() {
        fail("Should be disabled");
    }
}

