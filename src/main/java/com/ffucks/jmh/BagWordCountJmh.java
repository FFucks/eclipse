package com.ffucks.jmh;

import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.impl.factory.Lists;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(2)
@State(Scope.Benchmark)
public class BagWordCountJmh {

    @Param({"10000", "1000000"})
    int size;

    List<String> tokens; // JDK list

    @Setup(Level.Trial)
    public void setup() {
        String[] fruits = {"apple","orange","grape","watermelon","strawberry","banana","mango","pineapple","lemon","tangerine"};
        Random rand = new Random(42);
        tokens = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            tokens.add(fruits[rand.nextInt(fruits.length)]);
        }
    }

    @Benchmark
    public void jdkGroupingByCount(Blackhole bh) {
        Map<String, Long> counts = tokens.stream()
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
        bh.consume(counts);
    }

    @Benchmark
    public void ecToBag(Blackhole bh) {
        MutableBag<String> bag = Lists.mutable.withAll(tokens).toBag();
        bh.consume(bag);
    }
}
