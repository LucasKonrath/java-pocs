package org.example.pitpoc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Calculator Tests for Mutation Testing POC")
class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    @DisplayName("Addition should return correct sum")
    void testAdd() {
        assertEquals(5, calculator.add(2, 3));
        assertEquals(0, calculator.add(-1, 1));
        assertEquals(-5, calculator.add(-2, -3));
    }

    @Test
    @DisplayName("Subtraction should return correct difference")
    void testSubtract() {
        assertEquals(1, calculator.subtract(3, 2));
        assertEquals(-2, calculator.subtract(-1, 1));
        assertEquals(1, calculator.subtract(-2, -3));
    }

    @Test
    @DisplayName("Multiplication should return correct product")
    void testMultiply() {
        assertEquals(6, calculator.multiply(2, 3));
        assertEquals(0, calculator.multiply(0, 5));
        assertEquals(-6, calculator.multiply(-2, 3));
    }

    @Test
    @DisplayName("Division should return correct quotient")
    void testDivide() {
        assertEquals(2, calculator.divide(6, 3));
        assertEquals(0, calculator.divide(0, 5));
        assertEquals(-2, calculator.divide(-6, 3));
    }

    @Test
    @DisplayName("Division by zero should throw exception")
    void testDivideByZero() {
        assertThrows(IllegalArgumentException.class, () -> calculator.divide(5, 0));
    }

    @Test
    @DisplayName("isEven should correctly identify even numbers")
    void testIsEven() {
        assertTrue(calculator.isEven(2));
        assertTrue(calculator.isEven(0));
        assertTrue(calculator.isEven(-4));
        assertFalse(calculator.isEven(3));
        assertFalse(calculator.isEven(-1));
    }

    @Test
    @DisplayName("Factorial should calculate correctly")
    void testFactorial() {
        assertEquals(1, calculator.factorial(0));
        assertEquals(1, calculator.factorial(1));
        assertEquals(2, calculator.factorial(2));
        assertEquals(6, calculator.factorial(3));
        assertEquals(24, calculator.factorial(4));
        assertEquals(120, calculator.factorial(5));
    }

    @Test
    @DisplayName("Factorial with negative number should throw exception")
    void testFactorialNegative() {
        assertThrows(IllegalArgumentException.class, () -> calculator.factorial(-1));
    }

    @Test
    @DisplayName("Max should return the larger number")
    void testMax() {
        assertEquals(5, calculator.max(3, 5));
        assertEquals(5, calculator.max(5, 3));
        assertEquals(0, calculator.max(-1, 0));
        assertEquals(-1, calculator.max(-5, -1));
    }

    @Test
    @DisplayName("Grade calculation should return correct letter grades")
    void testGetGrade() {
        assertEquals("A", calculator.getGrade(95));
        assertEquals("A", calculator.getGrade(90));
        assertEquals("B", calculator.getGrade(85));
        assertEquals("B", calculator.getGrade(80));
        assertEquals("C", calculator.getGrade(75));
        assertEquals("C", calculator.getGrade(70));
        assertEquals("D", calculator.getGrade(65));
        assertEquals("D", calculator.getGrade(60));
        assertEquals("F", calculator.getGrade(55));
        assertEquals("F", calculator.getGrade(0));
    }
}
