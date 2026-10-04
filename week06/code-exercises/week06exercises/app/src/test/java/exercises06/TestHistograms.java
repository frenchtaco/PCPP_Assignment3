// For week 6
// raup@itu.dk * 2026-09-23

package exercises06;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import java.util.concurrent.CyclicBarrier;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestHistograms {
    CasHistogram casHist;
    Histogram hist2;
    CyclicBarrier barrier;
    int noThreads;
    Thread[] threads;

    @BeforeEach
    public void initialize() {
        noThreads = 5000; 
        int c = 5000;
        barrier = new CyclicBarrier(noThreads);
        casHist = new CasHistogram(c);
        hist2 = new Histogram1(c);
    }


    @RepeatedTest(100)
    @DisplayName("Bullshit testphase")
    public void testHisto() throws Exception {

        threads = new Thread[noThreads];
        for(int i = 0; i < noThreads; i++){
            final int value = i;
            threads[i] = new Thread(() -> {
                try {
                    barrier.await();
                    casHist.increment(countFactors(value));
                    //hist2.increment(countFactors(value));




                } catch (Exception e) { System.out.println(e); }
                
            });
            threads[i].start();
        }

        for(Thread t: threads){
            t.join();
        }

        Histogram1 hist = new Histogram1(noThreads);
        for (int i = 0; i < noThreads; i++) {
            hist.increment(countFactors(i));
        }

        for (int i = 0; i < noThreads; i++) {
            assertEquals(casHist.getCount(i), hist.getCount(i));
        }
        
    }


    // Function to count the number of prime factors of a number `p`
    private static int countFactors(int p) {
        if (p < 2) return 0;
        int factorCount = 1, k = 2;
        while (p >= k * k) {
            if (p % k == 0) {
                factorCount++;
                p= p/k;
            } else
                k= k+1;
        }
        return factorCount;
    }

}
