package exercises05;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CyclicBarrier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
// TODO: Very likely you need to expand the list of imports
import org.junit.jupiter.api.RepeatedTest;

public class ConcurrentSetTest {

    // Variable with set under test
    private ConcurrentIntegerSet set;
    CyclicBarrier barrier; 
    int nrThreads; 
    Thread[] threads;

    // TODO: Very likely you should add more variables here


    // Uncomment the appropriate line below to choose the class to
    // test
    // Remember that @BeforeEach is executed before each test
    @BeforeEach
    public void initialize() {
        nrThreads = 1000; 
        barrier = new CyclicBarrier(nrThreads); //init the main thread first
        
        // init set
        //set = new ConcurrentIntegerSetBuggy();
        //set = new ConcurrentIntegerSetSync();
        set = new ConcurrentIntegerSetLibrary();
        
       
    }

    // TODO: Define your tests below
    @RepeatedTest(100)
    @DisplayName("Test add function")
    public void testAdd() throws Exception {
        
        threads = new Thread[nrThreads];

        for(int i = 0; i < nrThreads; i++){
            final int value = i; 
            threads[i] = new Thread(() -> {
                try {
                    barrier.await();
                    set.add(value);
                } catch (Exception e) { e.printStackTrace(); }
            });
            threads[i].start();

        }
        // barrier.await();

        for(Thread t: threads){
            t.join();
        }

        assertEquals(nrThreads, set.size());
    }

    @RepeatedTest(100)
    @DisplayName("Test remove function")
    public void testRemove() throws Exception {

        threads = new Thread[nrThreads];
        for(int i = 0; i < nrThreads; i++){
            final int value = i;
            threads[i] = new Thread(() -> {

                try{
                    barrier.await();
                    set.add(value);
                    set.remove(value);

                } catch (Exception e) { System.out.println(e); }
            
            });
            threads[i].start();
        }
        // barrier.await();
        for(Thread t: threads){
            t.join();
        }

        assertEquals(0, set.size());
    }
}