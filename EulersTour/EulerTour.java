
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class EulerTour {

    /*
     findSmallTour:
     i/p: 
     Graph g
     source: vertex from which the subtour needs to start
     incompletely processed vertices: Stores the vertices that has remaining edges that are still needs to be processed
     indexOfEdgesToBeMerged: stores the index of edges after which the subtour needs to be meged to the main  tour
     o/p:
     list of edges in the sub tour
     */
    static DoublyLinkedList<Edge> findSmallTour(Graph g, Vertex source, List<Vertex> incompletelyProcessedVertices, List<DoublyLinkedList.Entry<Edge>> indexOfEdgesToBeMerged) {
        if (source.edgesProcessed != source.Adj.size()) { // edgesProcesed: counter that stores the next edge to be processed in the vertex's adjacency list
            DoublyLinkedList<Edge> smallTourEdges = new DoublyLinkedList<>();
            Edge e;
            Vertex current = source;      //current: iterates through the list of vertex starting from the source
            /*
             Loop executes till a complete sub tour is found. 
             The sub tour may find euler's circuit or euler's path
             */
            do {
                e = current.Adj.get(current.edgesProcessed);
                current.edgesProcessed++;
                /*
                 Check the retrieved edge is not already processed. Loop continues till an edge that is not processsed is found
                 */
                while (e != null && e.visited) {
                    if (current.edgesProcessed != current.Adj.size()) {
                        e = current.Adj.get(current.edgesProcessed);
                        current.edgesProcessed++;
                    } else {
                        e = null;
                    }
                }
                if (e != null) {
                    e.visited = true;
                    /*
                    Add the vertex only if has addtional edges to be processed and the vertex is not in the incompletelyProcessedVertices list  
                    */
                    if ((current.Adj.size() - current.edgesProcessed) > 1 && !current.seen) {
                        incompletelyProcessedVertices.add(current);
                        indexOfEdgesToBeMerged.add(smallTourEdges.tail);
                        current.seen=true; // If the vertex is added to the incompletelyProcessedVertices list. Set true for seen flag
                    }
                    smallTourEdges.add(e);
                    current = e.otherEnd(current);
                } else {
                    current = null;
                }
            } while (current != null && current != source && current.edgesProcessed != current.Adj.size());
            return smallTourEdges;
        } else {
            return null;
        }
    }

    /*
     Merges the subtour with the main tour.
     i/p:
     mainTour: list of main tour edges
     subTour: list of sub tour edges
     edgeIndex: index of edge in the main tour after which the subtour should be inserted
     */
    static void merge(DoublyLinkedList<Edge> mainTour, DoublyLinkedList<Edge> subTour, DoublyLinkedList.Entry<Edge> edgeIndex) {
        /*
         if edgeIndex is null(i.e if the subtour needs to merged at the beggining of the main tour)
         'else' part gets executed.This manipulates the head of the main tour. 
         Otherwise subtour is inserted inside the main tour list
         */
        if (edgeIndex != null) {
            subTour.tail.next = edgeIndex.next;
            edgeIndex.next.previous = subTour.tail;
            edgeIndex.next = subTour.header.next;
            subTour.header.next.previous = edgeIndex;
        } else {
            subTour.tail.next = mainTour.header.next;
            mainTour.tail.next = subTour.header.next;
            mainTour.header.next = subTour.header.next;
            mainTour.header.next.previous = mainTour.tail;
            subTour.tail.next.previous = subTour.tail;
        }
        mainTour.size += subTour.size; //Increase the main tour's size.
    }

    /*
     Finds the euler tour.
     i/p:
     Graph g
     source: vertex from which euler tour starts
     */
    static DoublyLinkedList<Edge> findEulerTour(Graph g, Vertex source) {
        g.resetVertices(); // resets the vertex's attributes value to fault value.
        LinkedList<Vertex> incompletelyProcessedVertices = new LinkedList<>();  //keeps track of the list of vertices that has remaining edges  
        LinkedList<DoublyLinkedList.Entry<Edge>> indexOfEdgesToBeMerged = new LinkedList<>();    //keeps track of the list of positions in the list to which the sub tours needs to be merged
        DoublyLinkedList<Edge> eulerTourEdges = findSmallTour(g, source, incompletelyProcessedVertices, indexOfEdgesToBeMerged);
        /*
         Loop continues till  the incompletely processed vertices list becomes empty.
         */
        while (!incompletelyProcessedVertices.isEmpty()) {
            /*
            Peek the incompletely processed vertices list. 
            The vertex remains in the list as long as the particular vertex has remaining edges to be processed. 
            */
            
            Vertex u = incompletelyProcessedVertices.peekFirst(); 
            DoublyLinkedList.Entry<Edge> index = indexOfEdgesToBeMerged.peekFirst();
            /*
            Loop continues till it chooses the vertex that has teh remaining edges to be processed
            */
            while (u.edgesProcessed==u.Adj.size())
            {
                incompletelyProcessedVertices.removeFirst(); //Remove the vertex if no edges in that vertex needs to be processed
                indexOfEdgesToBeMerged.removeFirst(); //Remove the corresponding index in the tourlist where the  new subtour needs to be merged.
                u.seen=false; //Set seen to false meaning that, vertex has been removed from the incompletelyProcessedVertices list
                u=incompletelyProcessedVertices.peekFirst();
                index=indexOfEdgesToBeMerged.peekFirst();
            }
            DoublyLinkedList<Edge> t_eulerTourEdges = findSmallTour(g, u, incompletelyProcessedVertices, indexOfEdgesToBeMerged);

            if (t_eulerTourEdges != null && t_eulerTourEdges.size > 0) {
                Edge firstEdgeOfSubTour = t_eulerTourEdges.header.next.element;
                Edge lastEdgeOfSubTour = t_eulerTourEdges.tail.element;
                /*
                 'if' condition checks whether the sub tour is euler tour
                 'else' part executes whether a subtour is euler path
                
                 Merge is done based on whether tour formed is euler tour or path.
                
                 Euler tour is possible only when there are more than two edges
                 */
                if (t_eulerTourEdges.size > 2 && (firstEdgeOfSubTour.From == lastEdgeOfSubTour.From || firstEdgeOfSubTour.From == lastEdgeOfSubTour.To || firstEdgeOfSubTour.To == lastEdgeOfSubTour.From || firstEdgeOfSubTour.To == lastEdgeOfSubTour.To)) { //In case of euler tour
                    merge(eulerTourEdges, t_eulerTourEdges, index);

                } else { //In case of Euler path
                    /*
                     If there is an euler path, then tour with euler path is the 
                     main tour and the other one is a sub tour an this why the merge=ing is doe by passing the tours in reverse order 
                     */
                    merge(t_eulerTourEdges, eulerTourEdges, index);
                    eulerTourEdges = t_eulerTourEdges;
                }
            }

            //Once the euler tours sixe is equal to that of the number of edges in the graph, the method can be terminated
            if (eulerTourEdges.size == g.numEdges) {
                break;
            }
        }
        return eulerTourEdges;
    }

    /*
     Verifies the tour
     i/p: 
     Graph
     eulertour: tour form by euler tour algorithm
     source: source vertex to start the euler tour
     */
    static boolean verifyTour(Graph g, DoublyLinkedList<Edge> eulerTour, Vertex source) {
        DoublyLinkedList.Entry<Edge> x = eulerTour.header.next;
        int numberOfEdgesInTour = 0;
        if (x.element.From == source || x.element.To == source) {
            numberOfEdgesInTour++;
            x.element.visited = false;
            DoublyLinkedList.Entry<Edge> old = x;
            x = x.next;
            /*
             Loop checks all the edges  of the tour, whether the all the edges are connected
             */
            while (x != eulerTour.header.next) {
                if (x.element.visited && (x.element.From == old.element.From || x.element.From == old.element.To || x.element.To == old.element.From || x.element.To == old.element.To)) {
                    numberOfEdgesInTour++;
                    x.element.visited = false;
                    old = x;
                    x = x.next;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
        return numberOfEdgesInTour == g.numEdges; //Check if the edges in tour is equal to the number of edges in the graph.
    }

    public static void main(String[] args) throws FileNotFoundException {

        Scanner s = new Scanner(new File(args[0]));
        Graph g = Graph.readGraph(s, false);
        System.out.println("Input complete");
        Vertex source = new EulerianGraph().findEulerianSource(g);
        DoublyLinkedList<Edge> eulerTourEdges = null;
        if (source != null) {
            long start = System.currentTimeMillis();
            eulerTourEdges = findEulerTour(g, source);
            long end = System.currentTimeMillis();
            System.out.println("Time take for the algorithm is: "+((end-start)/1000)+"seconds");
        }
        if (source != null && verifyTour(g, eulerTourEdges, source)) {
            System.out.println("Graph has an Euler tour");
            
            //eulerTourEdges.printList();
        } else {
            eulerTourEdges.printList();
            System.out.println("Graph is not Eulerian");
        }

    }
}
