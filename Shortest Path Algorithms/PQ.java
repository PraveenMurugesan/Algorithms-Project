/**
 * @author G94 
 * This interface demands the class to implement functionalities of priority queue
 * @param <T>
 *            - T refers to any object
 * 
 */
public interface PQ<T> {
	public void insert(T x);

	public T deleteMin();

	public T min();

	public void add(T x);

	public T remove();

	public T peek();
}