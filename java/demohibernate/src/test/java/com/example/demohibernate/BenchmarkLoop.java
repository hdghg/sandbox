package com.example.demohibernate;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
//@Warmup(iterations = 3)
//@Measurement(iterations = 8)
public class BenchmarkLoop {

    @Param({"10000000"})
    private int N;

    private List<Long> DATA_FOR_TESTING;

    @Test
    void main() throws RunnerException {

        Options opt = new OptionsBuilder()
                .include(BenchmarkLoop.class.getSimpleName())
                .measurementTime(TimeValue.milliseconds(500))
                .warmupTime(TimeValue.milliseconds(500))
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Setup
    public void setup() {
        DATA_FOR_TESTING = createData();
    }

    @Benchmark
    public void take10CollectionsShuffle(Blackhole bh) {
        Collections.shuffle(DATA_FOR_TESTING);
        bh.consume(DATA_FOR_TESTING.subList(0, 10));
    }

    @Benchmark
    public void take10SelectiveShuffle(Blackhole bh) {
        Random r = new Random();
        for (int i = N - 1; i >= N - 10; --i) {
            Collections.swap(DATA_FOR_TESTING, i, r.nextInt(i + 1));
        }
        bh.consume(DATA_FOR_TESTING.subList(N - 10, N));
    }

    private List<Long> createData() {
        List<Long> data = new ArrayList<>();
        for (long i = 0; i < N; i++) {
            data.add(i);
        }
        return data;
    }

}
