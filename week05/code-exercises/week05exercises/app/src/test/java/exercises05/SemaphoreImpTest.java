package exercises05;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CyclicBarrier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
// TODO: Very likely you need to expand the list of imports
import org.junit.jupiter.api.RepeatedTest;

public class SemaphoreImpTest {

    // Variable with set under test
    private SemaphoreImp semp;
    CyclicBarrier barrier; 
    int noThreads; 
    Thread[] threads;
    int c;

    // TODO: Very likely you should add more variables here


    // Uncomment the appropriate line below to choose the class to
    // test
    // Remember that @BeforeEach is executed before each test
    @BeforeEach
    public void initialize() {
        noThreads = 1000; 
        barrier = new CyclicBarrier(noThreads); //init the main thread first
        
        c = 5;

        // init set
        //set = new ConcurrentIntegerSetBuggy();
        //set = new ConcurrentIntegerSetSync();
        semp = new SemaphoreImp(c);
        
       
    }


    @RepeatedTest(100)
    @DisplayName("Test capacity above 0")
    public void testSemp() throws Exception {

        threads = new Thread[noThreads];
        for(int i = 0; i < noThreads; i++){
            threads[i] = new Thread(() -> {

                try{
                    barrier.await();
                    semp.acquire();
                    semp.release(); 
                    semp.release();

                } catch (Exception e) { System.out.println(e); }
            
            });
            threads[i].start();
        }
        // barrier.await();
        for(Thread t: threads){
            t.join();
        }
        int state = semp.getState();

        assertTrue(state >= 0, "we wanted >= 0, but got: " + state);
    }

    @RepeatedTest(100)
    @DisplayName("Test capacity below c")
    public void testSemp2() throws Exception {

        threads = new Thread[noThreads];
        for(int i = 0; i < noThreads; i++){
            threads[i] = new Thread(() -> {

                try{
                    barrier.await();
                    // semp.acquire(); results in deadlock as expected :(
                    semp.acquire();
                    semp.release();
         

                } catch (Exception e) { System.out.println(e); }
            
            });
            threads[i].start();
        }
        barrier.await();
        for(Thread t: threads){
            t.join();
        }
        int state = semp.getState();

        assertTrue(state <= c, "we wanted state <= " + c + ", but got: " + state);
    }
}