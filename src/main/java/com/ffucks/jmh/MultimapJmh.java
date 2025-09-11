package com.ffucks.jmh;

import org.eclipse.collections.api.multimap.list.MutableListMultimap;
import org.eclipse.collections.impl.factory.Multimaps;
import org.eclipse.collections.impl.list.mutable.ListAdapter;
import org.openjdk.jmh.annotations.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(value = 1, jvmArgsAppend = {"-Xms2g","-Xmx2g"})
@State(Scope.Thread)
public class MultimapJmh {

    @Param({"100000", "1000000"})
    int size;

    List<Tst> listTsts;
    String[] fruits = { "apple","orange","grape","watermelon","strawberry","banana","mango","pineapple","lemon","tangerine" };

    record Tst(int id, String dept) {}

    @Setup(Level.Trial)
    public void setup() {
        Random rand = new Random(42);
        listTsts = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            listTsts.add(new Tst(i, fruits[rand.nextInt(fruits.length)]));
        }
    }

    @Benchmark
    public Map<String, List<Tst>> jdkGroupingByBuild() {
        return listTsts.stream().collect(Collectors.groupingBy(Tst::dept));
    }

    @Benchmark
    public MutableListMultimap<String, Tst> ecGroupByBuild() {
        return ListAdapter.adapt(listTsts).groupBy(Tst::dept);
    }

    @Benchmark
    public MutableListMultimap<String, Tst> ecPutBuild() {
        MutableListMultimap<String, Tst> multimap = Multimaps.mutable.list.empty();
        for (Tst t : listTsts) {
            multimap.put(t.dept(), t);
        }
        return multimap;
    }
}
