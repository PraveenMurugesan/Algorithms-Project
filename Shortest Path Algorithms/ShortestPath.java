import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class ShortestPath {

	public static void shortestPathBFS(Graph g) {
		Queue<Vertex> queue = new LinkedList<>();
		g.initializeVertices();
		Vertex source = g.verts.get(1);
		source.distance = 0;
		source.seen = true;
		queue.add(source);

		while (!queue.isEmpty()) {
			Vertex u = queue.remove();
			for (Edge e : u.Adj) {
				Vertex v = e.otherEnd(u);
				if (!v.seen) {
					v.distance = u.distance + 1;
					v.parent = u;
					v.seen = true;
					queue.add(v);
				}
			}
		}
	}

	public static Vertex[] shortestPathDijkstra(Graph g, Vertex s) {
		Vertex[] verts = new Vertex[g.numNodes];
		int i = 0;
		for (Vertex v : g.verts) {
			if (v != null) {
				v.distance = v.name == s.name ? 0 : Integer.MAX_VALUE;
				v.seen = false;
				v.parent = null;
				verts[i++] = v;
			}
		}

		// set the default index value of each vertices as array index
		for (int j = 0; j < verts.length; j++)
			verts[j].index = j + 1;

		IndexedHeap<Vertex> vertexQueue = new IndexedHeap<Vertex>(verts, new Vertex(0));

		while (!vertexQueue.isEmpty()) {
			Vertex u = vertexQueue.remove();
			u.seen = true;
			for (Edge e : u.Adj) {
				Vertex v = e.otherEnd(u);
				if (!v.seen && (v.distance > u.distance + e.Weight)) {
					v.distance = u.distance + e.Weight;
					v.parent = u;
					vertexQueue.decreaseKey(v);
				}
			}
		}

		return verts;
	}

	public static void shortestPathDAG(Graph g) {
		Stack<Vertex> topologicalStack = topologicalSort(g);
		g.initializeVertices();
		Vertex source = g.verts.get(1);
		source.distance = 0;
		source.seen = true;
		while (!topologicalStack.isEmpty()) {
			Vertex u = topologicalStack.pop();
			for (Edge e : u.Adj) {
				Vertex v = e.otherEnd(u);
				if (!v.seen && (v.distance > u.distance + e.Weight)) {
					v.distance = u.distance + e.Weight;
					v.parent = u;
				}
			}
		}
	}

	public static boolean shortestPathBellmanFord(Graph g) {
		Queue<Vertex> queue = new LinkedList<>();
		g.initializeVertices();
		Vertex source = g.verts.get(1);
		source.distance = 0;
		source.seen = true;
		queue.add(source);
		while (!queue.isEmpty()) {
			Vertex u = queue.remove();
			u.seen = false;
			u.count++;
			if (u.count >= g.numNodes)
				return false;

			for (Edge e : u.Adj) {
				Vertex v = e.otherEnd(u);
				if (v.distance > u.distance + e.Weight) {
					v.distance = u.distance + e.Weight;
					v.parent = u;
					if (!v.seen) {
						v.seen = true;
						queue.add(v);
					}
				}
			}
		}
		
		return true;
	}

	/**
	 * @param g
	 *            - Input graph for which topological sort needs to be
	 *            determined
	 * @return - Returns a stack object which is in topological sort when popped
	 *         out. This method uses Depth First Search traversal to determine
	 *         topological sort
	 */
	private static Stack<Vertex> topologicalSort(Graph g) {

		Stack<Vertex> stack = new Stack<>();

		if (g != null) {
			// Pass the stack to DFS method to add the vertices in stack
			for (Vertex u : g) {
				if (u != null && !u.seen)
					DFSVisit(u, stack);
			}
		}

		// If there is a cycle in graph, the processed vertex will not match
		// with total vertex count.
		if (stack.size() != g.numNodes) {
			System.out.println("Given directed graph has cycle. Topological sort works only in DAG.");
			return new Stack<Vertex>();
		}

		return stack;
	}

	/**
	 * @param u
	 *            - Input vertex which is traversed in depth
	 * @param stack
	 *            - Stack which will contain vertices in topological order
	 * 
	 */
	private static void DFSVisit(Vertex u, Stack<Vertex> stack) {
		u.seen = true;
		// Invariant: for all the edges associated to u, the other vertex is
		// examined and DFS for it is called recursively
		for (Edge e : u.Adj) {
			Vertex v = e.otherEnd(u);
			if (!v.seen) {
				v.parent = u;
				DFSVisit(v, stack);
			}
		}
		// Finally the vertex that has all edge vertex seen is added to stack
		stack.push(u);
	}

	public static void main(String[] args) {

	}

}
