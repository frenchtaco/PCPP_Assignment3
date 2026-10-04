// For week 6
// raup@itu.dk * 2024-09-22

package exercises06;
import java.util.concurrent.atomic.AtomicReference;
// Very likely you will need some imports here

class SimpleRWTryLock implements SimpleRWTryLockInterface {

    private final  AtomicReference<Holders> holders = new AtomicReference<Holders>();
    int maxReaders;
    boolean writer;
    

    // TODO: Add necessary field(s) for the class

    public boolean readerTryLock() {
        // TODO 6.2.3
        final Thread current = Thread.currentThread();
        Holders currentHolder;
        Holders newHolder; 
         
        do {
            currentHolder = holders.get();
            if (currentHolder != null && currentHolder instanceof Writer) {
                return false; 
            }

            ReaderList currentReaderList = (ReaderList) currentHolder;
            if (currentReaderList == null)
                newHolder = new ReaderList(current, null);
            else if (!currentReaderList.contains(current))
                newHolder = currentReaderList.add(current);
            else
                throw new RuntimeException("Lock already held");
            
        } while (!holders.compareAndSet(currentHolder, (Holders) newHolder));
        return true;
    }

    public void readerUnlock() {
        final Thread current = Thread.currentThread();
        Holders currentHolder;
        Holders newHolder; 
        
        
            do {
                currentHolder = holders.get();

                if (currentHolder == null || !(currentHolder instanceof ReaderList)){
                    throw new RuntimeException("Currently no readers to read, dawg.");
                }
                
                ReaderList currentReaderList = (ReaderList) currentHolder;
                
                if (!currentReaderList.contains(current))
                    throw new RuntimeException("Not a lock holder");
                    
                newHolder = currentReaderList.remove(current);
                
            } while (!holders.compareAndSet(currentHolder, (Holders) newHolder));
    }

    public boolean writerTryLock() {
        // TODO 6.2.1
        final Thread current = Thread.currentThread();
        final Holders holder = new Writer(current);
        return holders.compareAndSet(null, holder);
    }

    public void writerUnlock() {
        // TODO 6.2.2
        final Thread current = Thread.currentThread();
        final Holders currentHolder = holders.get();
        if (currentHolder == null)
            throw new RuntimeException("No lock holders"); 
        if (currentHolder.getThread() == current) {
            holders.compareAndSet(currentHolder, null);
        } else {
            throw new RuntimeException("Not lock holder");
        }
    }

    public int readCount() {
        Holders holder = holders.get();

        if (holder == null || !(holder instanceof ReaderList)){
            return 0;
        } else {
            ReaderList holder_rl = (ReaderList) holder;
            return holder_rl.getCount();
        }
    }

    public int writeCount(){
        Holders holder = holders.get();

        if (holder == null || !(holder instanceof Writer)){
            return 0;
        } else {
            return 1; 
        }

    }



    private static abstract class Holders {
        public abstract Thread getThread();
    }

    private static class ReaderList extends Holders {
        private final Thread thread;
        private final ReaderList next;


        // TODO: Constructor
        public ReaderList(Thread t, ReaderList next){
            this.thread = t;
            this.next = next; 
        }

        // TODO: contains
        public boolean contains(Thread t){
            if (this.thread == t){
                return true;
            }
            if(this.next != null){
                return this.next.contains(t);
            } 
            return false;
        }

        public Thread getThread(){
            return this.thread;
        }

        // TODO: remove
        public ReaderList remove(Thread t){
            if (this.thread == t) { // Handles remove head case
                if (this.next == null) { // If head is only element
                    return null; 
                } else { // If head has tail
                    return this.next.remove(t);
                }
            } else if (this.next == null) { // Handle last element
                return new ReaderList(this.thread, null);
            } else if (this.next.thread == t) { // Handle when next is t
                return new ReaderList(this.thread, next.next.remove(t));
            } else { // Continue through list
                return new ReaderList(this.thread, next.remove(t));
            }
        }

        public ReaderList add(Thread t){
            if (this.next == null){
                return new ReaderList(this.thread, new ReaderList(t, null));
            } else {
                return new ReaderList(this.thread, this.next.add(t));
            }
        }

        public int getCount(){
            if(this.next == null){
                return 1; 
            } else {
                return 1 + this.next.getCount();
            }
        }
    }

    private static class Writer extends Holders {
        public final Thread thread;

        // TODO: Constructor
        public Writer(Thread t){
            this.thread = t; 
        }

        public Thread getThread(){
            return this.thread;
        }
    }
}
