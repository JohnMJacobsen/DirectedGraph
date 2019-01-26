package edu.sdsu.cs.datastructures;

/**
 * Program 3
 * Daniel Valoria
 * cssc0811
 * John Jacobsen
 * cssc-none, Healy Approved
 */

import java.util.*;

public class DirectedGraph<V extends Comparable<V>> implements IGraph<V> {

    private class Vertex implements Comparable<V> {

        V vertexName;
        ArrayList<V> destinations = new ArrayList<>();
        int value = -1;
        V parent;

        Vertex(V vertexName) {
            this.vertexName = vertexName;
        }

        void addDestinations(V dest) {
            if(!destinations.contains(dest))
                destinations.add(dest);
        }

        void removeDestinations(V dest) {
            if(destinations.contains(dest))
                destinations.remove(dest);
        }

        @Override
        public int compareTo(V vertex) {
            return vertexName.compareTo(vertex);
        }
    }

    TreeMap<V, Vertex> map = new TreeMap<>();

    public DirectedGraph() {
    }

    /**
     * Inserts a vertex with the specified name into the Graph if it
     * is not already present.
     *
     * @param vertexName The label to associate with the vertex
     */
    @Override
    public void add(V vertexName) {
        if(!contains(vertexName))
            map.put(vertexName, new Vertex(vertexName));
    }

    /**
     * Adds a connection between the named vertices if one does not
     * yet exist.
     *
     * @param start       The first vertex for the edge
     * @param destination The second vertex
     * @throws NoSuchElementException if either vertex are
     *                                not present in the graph
     */
    @Override
    public void connect(V start, V destination) {
        try {
            Iterator<Vertex> vertexIterator = map.values().iterator();
            Vertex a = null, b = null;
            while(vertexIterator.hasNext()) {
                Vertex v = vertexIterator.next();
                if(start.compareTo(v.vertexName) == 0)
                    a = v;
                if(destination.compareTo(v.vertexName) == 0)
                    b = v;
            }
            if(a == null || b == null)
                vertexIterator.next();
            a.addDestinations(destination);
        } catch(NoSuchElementException e) {
            throw e;
        }
    }

    /**
     * Resets the graph to an empty state.
     */
    @Override
    public void clear() {
        map.clear();
    }

    /**
     * Reports if a vertex with the specified label is stored within
     * the graph.
     *
     * @param label The vertex name to find
     * @return true if within the graph, false if not.
     */
    @Override
    public boolean contains(V label) {
        return map.containsKey(label);
    }

    /**
     * Removes the specified edge, if it exists, from the Graph.
     *
     * @param start       The name of the origin vertex
     * @param destination The name of the terminal vertex
     * @throws NoSuchElementException if either vertex are
     *                                not present in the graph
     */
    @Override
    public void disconnect(V start, V destination) {
        try {
            Iterator<Vertex> vertexIterator = map.values().iterator();
            Vertex a = null, b = null;
            while(vertexIterator.hasNext()) {
                Vertex v = vertexIterator.next();
                if(start.compareTo(v.vertexName) == 0)
                    a = v;
                if(destination.compareTo(v.vertexName) == 0)
                    b = v;
            }
            if(a == null || b == null)
                vertexIterator.next();
            a.removeDestinations(destination);

        } catch(NoSuchElementException e) {
            throw e;
        }
    }

    /**
     * Identifies if a path exists between the two vertices.
     * <p>
     * When the start and destination node names are the same, this
     * method shall only return true if there exists a self-edge on
     * the specified vertex.
     *
     * @param start       The initial Vertex
     * @param destination The terminal vertex
     * @return True if any path exists between them
     * @throws NoSuchElementException if either vertex are
     *                                not present in the graph
     */
    @Override
    public boolean isConnected(V start, V destination) {
        return !shortestPath(start, destination).isEmpty();
    }

    /**
     * Provides a collection of vertex names directly connected
     * through a single outgoing edge to the target vertex.
     * <p>
     * Changes to the returned Iterable object (e.g., .remove())
     * shall NOT impact or change the graph.
     *
     * @param vertexName The target vertex
     * @return An iterable, possibly empty, containing all
     * neighboring vertices.
     * @throws NoSuchElementException if the vertex is not
     *                                present in the graph
     */
    @Override
    public Iterable<V> neighbors(V vertexName) {
        try {
            Iterator<Vertex> vertexIterator = map.values().iterator();
            while(vertexIterator.hasNext()) {
                Vertex v = vertexIterator.next();
                if(vertexName.compareTo(v.vertexName) == 0)
                    return v.destinations;
            }
            vertexIterator.next();
            return null;
        }
        catch(NoSuchElementException e) {
            throw e;
        }
    }

    /**
     * This method deletes the vertex from the graph as well as every
     * edge using the specified vertex as a start (out) or
     * destination (in) vertex.
     *
     * @param vertexName The vertex name to remove from the graph
     * @throws NoSuchElementException if the origin vertex
     *                                is not present in this graph
     */
    @Override
    public void remove(V vertexName) {
        try {
            Vertex a = null;
            Iterator<Vertex> vertexIterator = map.values().iterator();
            while(vertexIterator.hasNext()) {
                Vertex v = vertexIterator.next();
                if(vertexName.compareTo(v.vertexName) == 0)
                    a = v;
            }
            if(a == null)
                vertexIterator.next();
            for(Vertex v : map.values()) {
                if(v.destinations.contains(a.vertexName))
                    disconnect(v.vertexName, vertexName);
            }
            map.remove(a.vertexName, a);
        }
        catch(NoSuchElementException e) {
            throw e;
        }
    }

    /**
     * Returns one shortest path through the graph from the starting
     * vertex and ending in the destination vertex.
     *
     * @param start       The vertex from which to begin the search
     * @param destination The terminal vertex within the graph
     * @return A sequence of vertices to visit requiring the fewest
     * steps through the graph from its starting position
     * (at index 0 in the list) to its terminus at the list's end.
     * If no path exists between the nodes, this method
     * returns an empty list.
     * @throws NoSuchElementException if either vertex are
     *                                not present in the graph
     */
    @Override
    public List<V> shortestPath(V start, V destination) {
        Vertex a = null, b = null;
        try {
            Iterator<Vertex> vertexIterator = map.values().iterator();

            while (vertexIterator.hasNext()) {
                Vertex v = vertexIterator.next();
                if (start.compareTo(v.vertexName) == 0)
                    a = v;
                if (destination.compareTo(v.vertexName) == 0)
                    b = v;
            }
            if (a == null || b == null) {
                vertexIterator.next();
            }

        } catch (NoSuchElementException e) {
            throw e;
        }

        computePaths(map.get(start), destination);
        return getShortestPathTo(destination, start);
    }

    void computePaths(Vertex start, V destination){
        start.value = 0;
        PriorityQueue<V> queue = new PriorityQueue<>();
        queue.add(start.vertexName);

        while(!queue.isEmpty()) {
            V current = queue.poll();
            if(map.get(current).destinations.isEmpty()) {
                return;
            }
            for(V v : neighbors(current)) {
                int distance = map.get(current).value + 1;
                if(distance >= map.get(v).value) {
                    queue.remove(v);
                    map.get(v).value = distance;
                    map.get(v).parent = current;
                    if(destination.compareTo(map.get(v).vertexName) == 0)
                        return;
                    queue.add(v);
                }
            }
        }
    }

    ArrayList<V> getShortestPathTo(V target, V origin){

        ArrayList<V> path = new ArrayList<>();
        for(Vertex vertex = map.get(target); vertex!=null; vertex = map.get(vertex.parent)){
            path.add(vertex.vertexName);
            if(vertex.parent == null)
                break;
        }

        if(path.size() == 1 && !map.get(origin).destinations.contains(target))
            return new ArrayList<>();

        if(map.get(origin).destinations.isEmpty())
            return new ArrayList<>();

        Collections.reverse(path);

        return path;
    }

    /**
     * Reports the number of vertices in the Graph.
     *
     * @return a non-negative number.
     */
    @Override
    public int size() {
        return map.size();
    }

    /**
     * Provides a collection of vertex names currently in the graph.
     *
     * @return The names of the vertices within the graph.
     */
    @Override
    public Iterable<V> vertices() {
        LinkedList<V> list = new LinkedList<>();
        for(V v : map.keySet())
            list.add(v);
        return list;
    }

    /**
     * Produces a graph of only those vertices and edges reachable
     * from the origin vertex.
     *
     * @param origin The vertex to build the graph from
     * @return A new graph with only the Vertices and Edges
     * reachable from the parameter Vertex.
     * @throws NoSuchElementException if the origin vertex
     * is not present in this graph
     */
    @Override
    public IGraph<V> connectedGraph(V origin) {
        DirectedGraph<V> connectedGraph = new DirectedGraph<>();
        try {
            Vertex a = null;
            Iterator<Vertex> vertexIterator = map.values().iterator();
            while(vertexIterator.hasNext()) {
                Vertex v = vertexIterator.next();
                if(origin.compareTo(v.vertexName) == 0)
                    a = v;
            }
            if(a == null)
                vertexIterator.next();
            connectedGraph.add(origin);
            V previous = origin;
            for(V v : map.keySet()) {
                if(isConnected(previous, v)) {
                    connectedGraph.add(v);
                    connectedGraph.connect(previous, v);
                    previous = v;
                }
            }
        } catch(NoSuchElementException e) {
            throw e;
        }
        return connectedGraph;
    }
    public StringBuilder toString(DirectedGraph<String> directedGraph, LinkedList<String> names) {
        StringBuilder sb = new StringBuilder();
        for (String location : names) {
            sb.append("Location: " + location + "\n");
            sb.append("=================================================================================\n");
            sb.append("Neighbors: " + directedGraph.neighbors(location) + "\n\n");
        }
        return sb;
    }
}
