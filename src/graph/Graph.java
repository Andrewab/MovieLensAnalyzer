package graph;

import java.util.*;





/*
*Hw 4 Directed Graph
* Author: Andrew Blanchette
* Date 10/8/2021
* Professor Chambers
* Algorithms CS361
 */

public class Graph<V> implements GraphIfc<V>{
    protected Map<V, ArrayList<V>>graph;


    public Graph() {
        this.graph = new HashMap<>();

    }

    /**
     * Returns the number of vertices in the graph
     * @return The number of vertices in the graph
     */
    public int numVertices() {
        
        return this.graph.size();
    }

    /**
     * Returns the number of edges in the graph
     * @return The number of edges in the graph
     */
    public int numEdges() {
        int i = 0;
        for(V key : this.graph.keySet()) {
            for(int j = 0; j < this.graph.get(key).size(); j++) {
                i++;
            }
        }
        return i;
    }

    /**
     * Removes all vertices from the graph
     */
    public void clear() {
        this.graph.clear();
    }

    /**
     * Adds a vertex to the graph. This method has no effect if the vertex already exists in the graph.
     * @param v The vertex to be added
     */
    public void addVertex(V v) {
        ArrayList <V> edges = new ArrayList();
        this.graph.put(v, edges);
    }

    /**
     * Adds an edge between vertices u and v in the graph.
     * @param u A vertex in the graph
     * @param v A vertex in the graph
     * @throws IllegalArgumentException if either vertex does not occur in the graph.
     */
	public void addEdge(V u, V v) {
        if(!this.graph.containsKey(u) || !this.graph.containsKey(v)){
            throw new IllegalArgumentException("A vertex does not exist");
        }
        ArrayList<V> tmp = this.graph.get(u);
        tmp.add(v);
        this.graph.replace(u,tmp);

    }

    /**
     * Returns the set of all vertices in the graph.
     * @return A set containing all vertices in the graph
     */
    @Override
    public Set<V> getVertices() {
        return this.graph.keySet();
    }

    /**
     * Returns the neighbors of v in the graph. A neighbor is a vertex that is connected to
     * v by an edge. If the graph is directed, this returns the vertices u for which an
     * edge (v, u) exists.
     *
     * @param v An existing node in the graph
     * @return All neighbors of v in the graph.
     * @throws IllegalArgumentException if the vertex does not occur in the graph
     */
    @Override
    public ArrayList<V> getNeighbors(V v) {
        if(!this.graph.containsKey(v)){
            throw new IllegalArgumentException("Vertex: " + v + " does not exist");
        }
        return this.graph.get(v);

    }

    /**
     * Determines whether the given vertex is already contained in the graph. The comparison
     * is based on the <code>equals()</code> method in the class V.
     *
     * @param v The vertex to be tested.
     * @return True if v exists in the graph, false otherwise.
     */
    @Override
    public boolean containsVertex(V v) {
        return this.graph.containsKey(v);
    }

    /**
     * Determines whether an edge exists between two vertices. In a directed graph,
     * this returns true only if the edge starts at v and ends at u.
     * @param v A node in the graph
     * @param u A node in the graph
     * @return True if an edge exists between the two vertices
     * @throws IllegalArgumentException if either vertex does not occur in the graph
     */
    @Override
    public boolean edgeExists(V v, V u) {
        if(this.graph.containsKey(v) && this.graph.containsKey(u)) {
            if(this.graph.get(v).contains(u) && this.graph.get(u).contains(v)) {
                return true;
            }
            return false;
        }
        throw new IllegalArgumentException("Vertexes: " + v + " or" + u + "does not exist");
    }

    /**
     * Returns the degree of the vertex. In a directed graph, this returns the outdegree of the
     * vertex.
     * @param v A vertex in the graph
     * @return The degree of the vertex
     * @throws IllegalArgumentException if the vertex does not occur in the graph
     */
    @Override
    public int degree(V v) {
        if(this.graph.containsKey(v)) {
            return this.graph.get(v).size();
        }
        throw new IllegalArgumentException("Degree: " + v + " does not exist");
    }

    /**
     * Returns a string representation of the graph. The string representation shows all
     * vertices and edges in the graph.
     * @return A string representation of the graph
     */
    @Override
    public String toString() {
        String str = "";
        for (V point : this.graph.keySet()) {
            String key = point.toString();
            String value = this.graph.get(point).toString();
            str += ("Key: " +key + "Value: " + value + "\n");
        }
        return str;
    }

}

