package org.roaringbitmap.buffer.aggregation.andnot.bestcase;

import org.openjdk.jmh.annotations.*;
import org.roaringbitmap.RoaringBitmap;
import org.roaringbitmap.buffer.MutableRoaringBitmap;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class MutableRoaringBitmapBenchmark {

    private MutableRoaringBitmap bitmap1;
    private MutableRoaringBitmap bitmap2;

    @Setup
    public void setup() {
        bitmap1 = new MutableRoaringBitmap();
        bitmap2 = new MutableRoaringBitmap();
        int k = 1 << 16;
        int i = 0;
        for(; i < 10000; ++i) {
            bitmap1.add(i * k);
        }
        for(; i < 10050; ++i) {
            bitmap2.add(i * k);
            bitmap1.add(i * k + 13);
        }
        for(; i < 20000; ++i) {
            bitmap2.add(i * k);
        }
        bitmap1.add(i * k);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public MutableRoaringBitmap andNot() {
        return MutableRoaringBitmap.andNot(bitmap1, bitmap2);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public MutableRoaringBitmap inplace_andNot() {
        MutableRoaringBitmap b1 = bitmap1.clone();
        b1.andNot(bitmap2);
        return b1;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public MutableRoaringBitmap justclone() {
      return bitmap1.clone();
    }

}
