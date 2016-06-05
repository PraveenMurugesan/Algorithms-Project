import java.util.*;

/**
 * Class to represent a graph
 */
class Graph implements Iterable<Vertex> {
	public List<Vertex> verts; // array of vertices
	public int numNodes; // number of verices in the graph

	/**
	 * Constructor for Graph
	 * 
	 * @param size
	 *            : int - number of vertices
	 */
	Graph(int size) {
		numNodes = size;
		verts = new ArrayList<>(size + 1);
		verts.add(0, null);
		// create an array of Vertex objects
		for (int i = 1; i <= size; i++)
			verts.add(i, new Vertex(i));
	}

	/**
	 * Method to add an edge to the graph
	 * 
	 * @param a
	 *            : int - one end of edge
	 * @param b
	 *            : int - other end of edge
	 * @param weight
	 *            : int - the weight of the edge
	 */
	void addEdge(int a, int b, int weight) {
		Vertex u = verts.get(a);
		Vertex v = verts.get(b);
		Edge e = new Edge(u, v, weight);
		u.Adj.add(e);
		v.Adj.add(e);
	}

	/**
	 * Method to add an arc (directed edge) to the graph
	 * 
	 * @param a
	 *            : int - the head of the arc
	 * @param b
	 *            : int - the tail of the arc
	 * @param weight
	 *            : int - the weight of the arc
	 */
	void addDirectedEdge(int a, int b, int weight) {
		Vertex head = verts.get(a);
		Vertex tail = verts.get(b);
		Edge e = new Edge(head, tail, weight);
		head.Adj.add(e);
		tail.inDegree++; // in-degree is added to the vertex to which the
		// directed edge is pointing
		tail.revAdj.add(e);
	}

	/**
	 * @return - the diameter of the graph. if the graph has cycle or if it is
	 *         not connected, then it returns -1
	 */
	public int getDiameter() {
		int diameter = 0;
		// if there is only one node, then diameter is zero
		if (this.numNodes > 1) {
			// finding the longest vertex from vertex 1
			Vertex longestVertex = BFSTraversal(this.verts.get(1));

			if (longestVertex == null)
				return -1;
			// reset values
			resetVertices();
			// sets the diameter as the maximum distance from longest vertex
			diameter = BFSTraversal(longestVertex).distance;
		}

		// reset values
		resetVertices();

		return diameter;
	}

	/**
	 * This method resets seen flag, parent and distance of each vertices in
	 * graph
	 */
	public void resetVertices() {
		for (Vertex v : verts) {
			if (v != null) {
				v.seen = false;
				v.parent = null;
				v.distance = 0;
			}
		}
	}

	public void initializeVertices() {
		for (Vertex v : verts) {
			if (v != null) {
				v.seen = false;
				v.parent = null;
				v.distance = Integer.MAX_VALUE;
				v.count = 0;
			}
		}
	}

	/**
	 * @param source
	 *            - Vertex from which BFS is performed
	 * @return - Returns the vertex which is maximum distance from source
	 */
	private Vertex BFSTraversal(Vertex source) {
		Queue<Vertex> queue = new LinkedList<>();
		source.seen = true;
		source.distance = 0;
		queue.add(source);
		Vertex longestVertex = source;
		int numberOfVertex = 1;
		// queue is polled until it is empty. BFS traversal is done. Distance is
		// updated as Parent's vertex distance plus one
		// longestVertex keeps track as vertex with maximum distance from source
		while (!queue.isEmpty()) {
			Vertex u = queue.poll();
			for (Edge e : u.Adj) {
				Vertex v = e.otherEnd(u);
				// if the edge is not seen already but the vertex is seen, then
				// there is a cycle, return null
				if (!e.seen && v.seen)
					return null;
				e.seen = true;
				if (!v.seen) {
					v.parent = u;
					v.distance = u.distance + 1;
					if (longestVertex.distance < v.distance)
						longestVertex = v;
					v.seen = true;
					queue.add(v);
					numberOfVertex++;
				}
			}
		}

		// Check if graph is disconnected
		if (numberOfVertex != numNodes)
			return null;

		return longestVertex;
	}

	/**
	 * Method to create an instance of VertexIterator
	 */
	public Iterator<Vertex> iterator() {
		return new VertexIterator();
	}

	/**
	 * A Custom Iterator Class for iterating through the vertices in a graph
	 * 
	 *
	 * @param <Vertex>
	 */
	private class VertexIterator implements Iterator<Vertex> {
		private Iterator<Vertex> it;

		/**
		 * Constructor for VertexIterator
		 * 
		 * @param v
		 *            : Array of vertices
		 * @param n
		 *            : int - Size of the graph
		 */
		private VertexIterator() {
			it = verts.iterator();
			it.next(); // Index 0 is not used. Skip it.
		}

		/**
		 * Method to check if there is any vertex left in the iteration
		 * Overrides the default hasNext() method of Iterator Class
		 */
		public boolean hasNext() {
			return it.hasNext();
		}

		/**
		 * Method to return the next Vertex object in the iteration Overrides
		 * the default next() method of Iterator Class
		 */
		public Vertex next() {
			return it.next();
		}

		/**
		 * Throws an error if a vertex is attempted to be removed
		 */
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	public static Graph readGraph(Scanner in, boolean directed) {
		// read the graph related parameters
		int n = in.nextInt(); // number of vertices in the graph
		int m = in.nextInt(); // number of edges in the graph

		// create a graph instance
		Graph g = new Graph(n);
		for (int i = 0; i < m; i++) {
			int u = in.nextInt();
			int v = in.nextInt();
			int w = in.nextInt();
			if (directed) {
				g.addDirectedEdge(u, v, w);
			} else {
				g.addEdge(u, v, w);
			}
		}
		in.close();
		return g;
	}
}