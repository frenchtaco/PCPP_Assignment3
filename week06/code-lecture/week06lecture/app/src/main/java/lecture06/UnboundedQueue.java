// For week 6
// sestoft@itu.dk * 2014-11-16
package lecture06;

interface UnboundedQueue<T> {
    void enqueue(T item);
    T dequeue();
}
