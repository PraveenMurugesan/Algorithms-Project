

/**
 * Class to check if a graph is Eulerian
 * 
 * @author G94
 */
public class EulerianGraph {
    /**
     * Method to test if a graph is Eulerian. A connected graph G is called
     * Eulerian if the degree of every vertex is an even number. A connected
     * graph that has exactly 2 vertices of odd degree has an Eulerian path.
     * 
     * @param g
     *            : The graph to be tested
     */
    public Vertex  findEulerianSource(Graph g) {
	
	int oddVerts = 0;
	Vertex[] v = new Vertex[2];
	for (Vertex u : g) {
	    if (u.Adj.size() % 2 != 0) {
		/*
		 * Using mod of odd vertices count to store any 2 vertices of
		 * odd degree instead of an if-else block
		 */
		v[oddVerts % 2] = u;
		oddVerts++;
	    }
	}
	switch (oddVerts) {
	case 0:
	    // All vertices are of even degree
	    return g.verts.get(1);
	    

	case 2:
	    // Exactly 2 vertices are of odd degree
	    return v[0];

	default:
	    // More than 2 vertices are of odd degree
	    return null;
	}
    }
}
