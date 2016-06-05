
/**
 * Class to represent a vertex of a graph
 * 
 *
 */

import java.util.*;

public class Vertex implements Index, Comparator<Vertex> {

	public int name; // name of the vertex
	public boolean seen; // flag to check if the vertex has already been visited
	public Vertex parent; // parent of the vertex
	public int distance; // distance to the vertex from the source vertex
	public List<Edge> Adj, revAdj; // adjacency list; use LinkedList or
									// ArrayList
	public int inDegree; // number of incoming edges in a directed graph
	public int cno; // the component number of the vertex in the graph
	public int index; // the index field used for indexedHeap
	public int count;

	/**
	 * Constructor for the vertex
	 * 
	 * @param n
	 *            : int - name of the vertex
	 */
	Vertex(int n) {
		name = n;
		seen = false;
		parent = null;
		Adj = new ArrayList<Edge>();
		revAdj = new ArrayList<Edge>(); /* only for directed graphs */
		count = 0;
	}

	/**
	 * Method to represent a vertex by its name
	 */
	public String toString() {
		return Integer.toString(name);
	}

	/*
	 * sets the index value of the vertex
	 */
	public void putIndex(int i) {
		index = i;
	}

	/*
	 * returns the index value of the vertex
	 */
	public int getIndex() {
		return index;
	}

	/*
	 * compares the distance of two vertices and returns the difference.
	 */
	public int compare(Vertex v1, Vertex v2) {
		return v1.distance - v2.distance;
	}
}