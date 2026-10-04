package exercises06;

import java.util.concurrent.atomic.AtomicInteger;

// import java.util.concurrent.atomic.AtomicInteger;

public class CasHistogram implements Histogram {
    private final AtomicInteger[] bins;
    private final int span;

    public CasHistogram(int span) {
        this.span = span;
        this.bins = new AtomicInteger[span]; // hehe
        for (int i = 0; i < span; i++) {
            bins[i] = new AtomicInteger(0);
        }
    }

    public void increment(int bin) {
        int oldValue;
        int newValue;
        do {
            oldValue = bins[bin].get();
            newValue = oldValue + 1;
        } while (!bins[bin].compareAndSet(oldValue, newValue));
    }

    public int getCount(int bin) {
        return bins[bin].get();
    }

    public int getSpan() {
        return span;
    }

    public int getAndClear(int bin) {
        int oldValue;
        do {
            oldValue = bins[bin].get();
        } while (!bins[bin].compareAndSet(oldValue, 0));
        return oldValue;
    }
}
