// For week 6
// raup@itu.dk * 2026-09-23

package exercises06;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CyclicBarrier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.DisplayName;

// Very likely you will need some imports here

public class TestLocks {
    int nrThreads;
    CyclicBarrier barrier;
    Thread[] threads;
    SimpleRWTryLock monitor;
    // The imports above are just for convenience, feel free add or remove imports

    @BeforeEach
    public void initialize() {
        nrThreads = 1000; 
        barrier = new CyclicBarrier(nrThreads);
        monitor = new SimpleRWTryLock();

    }

    @RepeatedTest(1)
    @DisplayName("It is not possible to take a read lock while holding a write lock.")
    public void testReadLockWhileWriting(){
        monitor.writerTryLock();
        monitor.readerTryLock();

        int value = monitor.readCount();
        
        assertEquals(value, 0);
    }

    @RepeatedTest(1)
    @DisplayName("It is not possible to take a write lock while holding a read loc")
    public void testWriteLockWhileReading(){
        monitor.readerTryLock();
        monitor.writerTryLock();

        int value = monitor.writeCount();
        
        assertEquals(value, 0);
    }

    @RepeatedTest(1)
    @DisplayName("It is not possible to unlock a write lock that you do not hold")
    public void testUnableToUnlockAnotherWriteLock() throws Exception {
        threads = new Thread[2];
        for (int i = 0; i < 2; i++) {
            final int value = i; 
            
            threads[i] = new Thread(() -> {
                try {
                    if(value % 2 == 0) {
                        monitor.writerTryLock();
                    } else {
                        monitor.writerUnlock();
                    }


                } catch (Exception e) { /*System.out.println(e);*/ }
                
            });
            threads[i].start();
        }

        for (Thread t: threads) {
            t.join();
        }

        assertEquals(monitor.writeCount(), 1);   
    }

    @RepeatedTest(1)
    @DisplayName("It is not possible to unlock a read lock that you do not hold")
    public void testUnableToUnlockAnotherReadLock() throws Exception {
        threads = new Thread[nrThreads];

        for (int i = 0; i < nrThreads; i++) {
            final int value = i; 
            
            threads[i] = new Thread(() -> {
                try {
                    barrier.await();
                    if(value % 2 == 0) {
                        monitor.readerTryLock();
                    } else {
                        monitor.readerUnlock();
                    }


                } catch (Exception e) { /*System.out.println(e);*/ }
                
            });
            threads[i].start();
        }

        for (Thread t: threads) {
            t.join();
        }

        assertEquals(monitor.readCount(), nrThreads / 2);   
    }

    @RepeatedTest(1)
    @DisplayName("Testing that one thread can't lock readers multiple times")
    public void testMultipleReaders(){
        try {
            monitor.readerTryLock();
            monitor.readerTryLock();
            monitor.readerTryLock();
        } catch (Exception e) {
            // TODO: handle exception (NO)
        }

        int value = monitor.readCount();
        
        assertEquals(value, 1);
    }

    @RepeatedTest(1)
    @DisplayName("Test that multiple writers can't acquire the lock at the same time")
    public void testIfMultipleWritersCanLockAtTheSameTime() throws Exception {
        threads = new Thread[nrThreads];
        int[] results = new int[nrThreads];

        for (int i = 0; i < nrThreads; i++) {
            final int value = i; 
            
            threads[i] = new Thread(() -> {
                try {
                    barrier.await();
                    monitor.writerTryLock();
                    results[value] = monitor.writeCount();
                    monitor.writerUnlock();
                } catch (Exception e) { /*System.out.println(e);*/ }
            });
            threads[i].start();
        }

        for (Thread t: threads) {
            t.join();
        }

        int highest = 0;
        for (int result: results) {
            highest = Math.max(result, highest);
        }

        assertEquals(highest, 1);   
    }

    /* 
    @RepeatedTest(10)
    @DisplayName("It is not possible to take a read lock while holding a write lock.")
    public void thisblows(){
        for (int i = 0; i < nrThreads; i++) {
            final int value = i; 
            
            threads[i] = new Thread(() -> {
                try {
                    barrier.await();
                    if(value % 5 == 0) {
                        monitor.writerTryLock();
                        monitor.readerTryLock();
                        monitor.readerUnlock();
                        monitor.writerUnlock();
                    } else {
                        monitor.readerTryLock();
                        monitor.readerUnlock();
                    }

                } catch (Exception e) { System.out.println(e); }
                
            });
            threads[i].start();
        }
    }
    */
    // TODO: 6.2.5

    // TODO: 6.2.6

}
