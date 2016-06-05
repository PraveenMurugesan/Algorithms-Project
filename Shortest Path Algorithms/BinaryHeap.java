
// Ver 1.0:  Wec, Feb 3.  Initial description.

import java.util.Comparator;

/**
 * @author Srikanth This class implements Binary Heap functionality
 * @param <T>
 *            Any object
 */
public class BinaryHeap<T> implements PQ<T> {
	Object[] pq;
	Comparator<T> c;
	private int size;

	/** Build a priority queue with a given array q */
	BinaryHeap(T[] q, Comparator<T> comp) {
		pq = new Object[q.length + 1];
		int i = 1;
		// set PQ from index 1
		for (T item : q)
			pq[i++] = item;

		size = q.length;
		c = comp;
		buildHeap();
	}

	/** Create an empty priority queue of given maximum size */
	public BinaryHeap(int n, Comparator<T> comp) {
		c = comp;
		pq = new Object[n + 1];
		size = 0;
	}

	/*
	 * Inserts new element to the heap
	 */
	public void insert(T x) {
		add(x);
	}

	/*
	 * delete the element in the root of the heap tree. It either deletes the
	 * minimum value of maximum value which depends on Comparator implementation
	 */
	public T deleteMin() {

		return remove();
	}

	/*
	 * Returns the element in the root of the heap tree. Only difference between
	 * deleteMin and min is deleteMin removes the element and returns it, and
	 * min only returns the element.
	 */
	public T min() {
		return peek();
	}

	/*
	 * Adds new element to the heap. If the heap has reached maximum size, the
	 * heap size is increased.
	 */
	public void add(T x) {
		// if heap has reached maximum size, increase heap size.
		if (size == pq.length - 1)
			increaseHeapSize(pq.length * 2);
		/*
		 * New element is added at last in the heap and percolate up action is
		 * performed over the newly added element.
		 **/
		pq[++size] = x;
		percolateUp(size);
	}

	/**
	 * @param newSize
	 *            - new size to which the heap has to be increased
	 */
	@SuppressWarnings("unchecked")
	private void increaseHeapSize(int newSize) {
		Object[] old = pq;
		pq = new Object[newSize];
		for (int i = 1; i < old.length; i++)
			pq[i] = old[i];
	}

	/*
	 * Removes the minimum or maximum element from the heap based on comparator
	 * operation
	 */
	public T remove() {
		T returnValue = peek();

		if (returnValue != null) {
			pq[1] = pq[size--];
			percolateDown(1);
		}

		return returnValue;
	}

	/*
	 * Returns the minimum or maximum element from the heap based on comparator
	 * operation
	 */
	@SuppressWarnings("unchecked")
	public T peek() {
		if (size == 0)
			return null;

		return (T) pq[1];
	}

	/**
	 * @param toIndex
	 *            Sets the value from destination index to this index
	 * @param fromIndex
	 *            Sets the value to source index from this index
	 */
	public void setValueIndex(int toIndex, int fromIndex) {
		pq[toIndex] = pq[fromIndex];
	}

	/** pq[i] may violate heap order with parent */
	@SuppressWarnings("unchecked")
	void percolateUp(int i) {

		int hole = i;
		// moves those element down until the element at i gets the correct
		// position. This operation depends on comparator operation as whether
		// max heap or min heap is performed.
		for (pq[0] = pq[i]; c.compare((T) pq[0], (T) pq[hole / 2]) < 0; hole /= 2)
			setValueIndex(hole, hole / 2);

		setValueIndex(hole, 0);
	}

	/** pq[i] may violate heap order with children */
	@SuppressWarnings("unchecked")
	void percolateDown(int i) {
		int child = 0;
		pq[0] = pq[i];
		// Move all child elements up based on Max or Min heap decided by
		// comparator operation and finally set the element which was in 'i'th
		// position in heap
		while (i * 2 <= size) {
			child = i * 2;
			// if right child element is less the left child element in min heap
			if (child != size && c.compare((T) pq[child], (T) pq[child + 1]) > 0)
				child++;
			if (c.compare((T) pq[child], (T) pq[0]) < 0)
				setValueIndex(i, child);
			else
				break;

			i = child;
		}

		setValueIndex(i, 0);

	}

	/** Create a heap. Precondition: none. */
	void buildHeap() {
		for (int i = size / 2; i > 0; i--)
			percolateDown(i);
	}

	/**
	 * @return checks whether the heap is empty or not
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/*
	 * sort array A[1..n]. A[0] is not used. Sorted order depends on comparator
	 * used to build heap. min heap ==> descending order max heap ==> ascending
	 * order
	 */
	@SuppressWarnings("unchecked")
	public static <T> void heapSort(T[] A, Comparator<T> comp) {
		// Binary heap will be maxheap or minHeap based on comp
		// parameter;heapify is done as part of the constructor
		BinaryHeap<T> inputHeap = new BinaryHeap<>(A, comp);

		for (int i = inputHeap.size; i > 0; i--) {
			T temp = (T) inputHeap.pq[1];
			inputHeap.pq[1] = inputHeap.pq[i];
			inputHeap.pq[i] = temp;
			inputHeap.size--;
			// percolate down re-arranges as max on top or min on top based on
			// the comp assigned to inputHeap object
			inputHeap.percolateDown(1);
		}
		// Set the ordered PQ to input array A
		for (int i = 0; i < A.length; i++)
			A[i] = (T) inputHeap.pq[i + 1];
	}
}
