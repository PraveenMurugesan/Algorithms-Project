import java.util.Comparator;

/**
 * Class that represents an arc in a Graph
 *
 */
public class Edge implements Comparator<Edge> {
	public Vertex From; // head vertex
	public Vertex To; // tail vertex
	public int Weight;// weight of the arc
	public boolean seen;// flag to check if the edge has already been visited

	/**
	 * Constructor for Edge
	 * 
	 * @param u
	 *            : Vertex - The head of the arc
	 * @param v
	 *            : Vertex - The tail of the arc
	 * @param w
	 *            : int - The weight associated with the arc
	 */
	Edge(Vertex u, Vertex v, int w) {
		From = u;
		To = v;
		Weight = w;
		seen = false;
	}

	Edge() {
	}

	/**
	 * Method to find the other end end of the arc given a vertex reference
	 * 
	 * @param u
	 *            : Vertex
	 * @return
	 */
	public Vertex otherEnd(Vertex u) {
		// if the vertex u is the head of the arc, then return the tail else
		// return the head
		if (From == u) {
			return To;
		} else {
			return From;
		}
	}

	/**
	 * Method to represent the edge in the form (x,y) where x is the head of the
	 * arc and y is the tail of the arc
	 */
	public String toString() {
		return "(" + From + "," + To + ")";
	}

	public int compare(Edge e1, Edge e2) {
		return ((e1 != null ? e1.Weight : Integer.MIN_VALUE)) - ((e2 != null) ? e2.Weight : Integer.MIN_VALUE);
	}
}