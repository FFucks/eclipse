package com.ffucks.jmh;

import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;
import org.openjdk.jmh.annotations.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
@State(Scope.Thread)
public class PrimitivesJmh {

    @Param({"1000000"})
    int size;

    int[] data;
    java.util.List<Integer> boxed;
    MutableIntList eCollection;

    @Setup(Level.Trial)
    public void setup() {
        Random r = new Random(42);
        data = new int[size];
        for (int i = 0; i < size; i++) {
            data[i] = r.nextInt(1_000_000);
        }

        boxed = new ArrayList<>(size);
        for (int v : data) {
            boxed.add(v);
        }

        eCollection = new IntArrayList(size);
        for (int v : data) {
            eCollection.add(v);
        }
    }

    @Benchmark
    public long baselineIntArraySum() {
        long s = 0;
        for (int v : data) s += v;
        return s;
    }

    @Benchmark
    public int jdkBoxedStreamSum() {
        return boxed.stream().mapToInt(Integer::intValue).sum();
    }

    @Benchmark
    public long ecIntlistSum() {
        return eCollection.sum();
    }
}