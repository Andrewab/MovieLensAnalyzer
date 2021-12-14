package graph;


import java.util.ArrayList;
import java.util.Map;
//import java.util.PriorityQueue;
import java.util.Set;

import data.Movie;
import util.PriorityQueue;

public class GraphAlgorithms {
	// FILL IN
    Graph graph;
    public GraphAlgorithms(int type, Map reviewers, Map<Integer,Movie> movies){

}

    /**
     * Takes in a graph and finds the shortest path in a directed weighted graph.
     * @param graph
     * @return
     */

    //uses a matrix to represent the problem

    /**
     *
     * @param graph
     * @return
     */
    public static int[][] floydWarshall(Graph<Integer> graph) {
        int solution[][] = new int[graph.numVertices()][graph.numVertices()];

        for (int i = 0; i < graph.numVertices(); i++){
            for(Integer vert :  graph.getNeighbors(i+1)){
                solution[i][vert-1] = 1;
            }
        }
        //Sets the largest integer distance as default if and only if the edge is not to itself. If it is to itself set the value to 0.
        for (int i = 0; i < graph.numVertices(); i++) {
            for (int j = 0; j < graph.numVertices(); j++) {
                if (i == j){
                    solution[i][j] = 0;
                }
                else if (solution[i][j] == 0 && i != j) {
                    solution[i][j] = Integer.MAX_VALUE;
                }
            }
        }
        for(int i = 0; i < graph.numVertices(); i ++){
            for (int k = 0; k < graph.numVertices(); k++){
                for (int j =0; j < graph.numVertices(); j++){

                    //maintains the max val if they need to be added by not adding them otherwise add
                    if (solution[k][i] != Integer.MAX_VALUE && Integer.MAX_VALUE != solution[i][j] && solution[k][j] > solution[k][i] + solution[i][j]){
                        solution[k][j] = solution[k][i] + solution[i][j];
                    }
                }
            }
        }

        return solution;
    }

    /**
     *
     * @param graph
     * @param source
     * @return
     */
    public static int[] dijkstrasAlgorithm(Graph<Integer> graph, int source){
        PriorityQueue queue = new PriorityQueue();

        //TODO
        int distance[] = new int[graph.numVertices()];
        int previous[] = new int[graph.numVertices()];

        Set<Integer> vertices = graph.getVertices();

        for (int i = 0; i < graph.numVertices(); i++){
            distance[i] = Integer.MAX_VALUE - graph.numVertices();
        }
        distance[source] = 0;

        for(Integer vertex : vertices){
            queue.push(distance[vertex-1],vertex);
        }
//Got 36 as adjacent node second time
        while (!queue.isEmpty()){
            if(queue.topElement()== graph.numVertices()){
                break;
            }
            int topElement = queue.topElement()-1;
            queue.pop();
            //not possible to get to 0 since no vertex exists since its 1-1001 1k elems
            if(topElement == 0){
                break;
            }
            ArrayList<Integer> nodeNeighbors = graph.getNeighbors(topElement);
            for (Integer adjacentNode : nodeNeighbors) {
                int temp = distance[topElement]+1; //The distance from the top element will be +1
                if(adjacentNode == 1000){
                   break;
                }
                if(temp < distance[adjacentNode]){
                    distance[adjacentNode]= temp;
                    previous[adjacentNode] = topElement;
                    queue.changePriority(temp,adjacentNode+1);
                }
            }
        }
        //previous is the node we are at to the previous nodes! if we can link them like a linked list that could solve our problem
        return previous;
    }
}