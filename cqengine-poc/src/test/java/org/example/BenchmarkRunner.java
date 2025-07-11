package org.example;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * Runner class for CQEngine vs For Loop benchmarks.
 *
 * To run all benchmarks:
 * mvn test-compile exec:java -Dexec.mainClass="org.example.BenchmarkRunner" -Dexec.classpathScope=test
 *
 * To run specific benchmarks (e.g., only simple queries):
 * mvn test-compile exec:java -Dexec.mainClass="org.example.BenchmarkRunner" -Dexec.classpathScope=test -Dexec.args=".*SimpleQuery.*"
 */
public class BenchmarkRunner {
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(CQEngineVsForLoopBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
