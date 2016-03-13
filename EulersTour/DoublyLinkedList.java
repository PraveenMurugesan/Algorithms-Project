

/**
 * Generic Implementation of a Doubly Circular Linked List
 */
public class DoublyLinkedList<T> {
	public static class Entry<T> {
		T element;
		Entry<T> next;
                Entry<T> previous;

		Entry() {
		}

		Entry(T x, Entry<T> nxt,Entry<T> prev) {
			element = x;
			next = nxt;
                        previous=prev;
		}
	}

	Entry<T> header, tail;
	int size;

	DoublyLinkedList() {
		header = new Entry<>(null, null,null);
		tail = null;
		size = 0;
	}

	void add(T x) {
		if (tail == null) {
                        Entry<T> newNode= new Entry<>(x, null,null);
			header.next = newNode;
                        newNode.next=newNode;
                        newNode.previous=newNode;
			tail = header.next;
		} else {
                        Entry<T> newNode= new Entry<>(x, tail.next,tail);
			tail.next = newNode;
                        tail=newNode;
                        header.next.previous=newNode;
		}
		size++;
	}

	void printList() {
		Entry<T> x = header.next;
		 do{
			System.out.println(x.element + " ");
			x = x.next;
		}while (x != header.next);
		System.out.println();
	}
}
