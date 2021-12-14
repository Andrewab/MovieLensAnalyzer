package analyzer;
import data.Movie;
import data.Reviewer;
import graph.Graph;
import util.DataLoader;
import graph.GraphAlgorithms;

import javax.swing.text.html.Option;
import java.util.*;
/**
 * Please include in this comment you and your partner's name and describe any extra credit that you implement
 * Authors: Dominic Meconi, Andrew Blanchette
 */
public class MovieLensAnalyzer {
	public static void main(String[] args){
		if(args.length != 2){
			System.err.println("Usage: java MovieLensAnalyzer [ratings_file] [movie_title_file]");
			System.exit(-1);
		}
		System.out.print("========== Welcome to movie lens analyzer ==========\n");
		System.out.print("The files being analyzed are: \n");
		System.out.print(args[0] + "\n");
		System.out.print(args[1] + "\n\n");
		System.out.print("There are 3 choices for defining adjacency:\n");
		System.out.print("[Option 1] u and v are adjacent if the same 12 users gave the same rating to both movies\n");
		System.out.print("[Option 2] u and v are adjacent if the same 12 users watched both movies(regardless of rating)\n");
		System.out.print("Choose an option to build the graph (1-2): ");
		Scanner scr = new Scanner(System.in);
		int input = scr.nextInt();
		DataLoader loader = new DataLoader();
		loader.loadData(args[0],args[1]);
		Graph<Integer> graph = new Graph<>();;
		Map<Integer,Reviewer> reviewers = loader.getReviewers();
		Map<Integer,Movie> movies = loader.getMovies();
		//Create graph based upon inputs
		System.out.println("Creating Graph");
		if(input == 1) {
			graph = createGraphOption1(movies, reviewers);
		}
        else if (input == 2) {
			graph = createGraphOption2(movies,reviewers);
		}
		System.out.println("Graph Created!");
		while (true){
			System.out.println("[Option 1] Print out statistics for a graph");
			System.out.println("[Option 2] Print Node Information");
			System.out.println("[Option 3] Display Shortest Path");
			System.out.println("[Option 4] Quit");
			input = scr.nextInt();
			switch (input){
				case 1: input = 1;
					System.out.println("Graph Statistics:");
					System.out.println("|V| = " + graph.numVertices());
					System.out.println("|E| = " + graph.numEdges());
					System.out.println("Density = " + ((double) graph.numEdges() / (double) graph.numVertices() * (double) graph.numVertices() - 1));
					int edgeCount = Integer.MIN_VALUE;
					int edgeWithMost = Integer.MIN_VALUE;
					for (Integer vert : graph.getVertices()){
						if (graph.degree(vert) > edgeCount){
							edgeCount = graph.degree(vert);
							edgeWithMost = vert;
						}
					}
					System.out.println("The Max Degree = " + edgeCount + " Of Edge: " + edgeWithMost);
					int[][] shortestPath = GraphAlgorithms.floydWarshall(graph);
					int start = 0;
					int end = 0;
					int diameter = 0;
					int sum = 0;
					int numPaths = 0;
					for (int i = 0; i < shortestPath.length; i++) {
						for (int j = 0; j < shortestPath.length; j++) {
							if(shortestPath[i][j] != Integer.MAX_VALUE) {
								sum = sum + shortestPath[i][j];
								++numPaths;
								if (shortestPath[i][j] > diameter) {
									start = i;
									end = j;
									diameter = shortestPath[i][j];
								}
							}
						}
					}
					double avgPathLength = (double) sum / (double) numPaths;
					System.out.println("Diameter : " + diameter + " Start : " + start + " End : " + end + " Average Path Length : " + avgPathLength);
					break;
				case 2: input = 2;
					int size = graph.numVertices();
					System.out.println("Enter Movie Id (1-"+ size +") : ");
					int inputNode = scr.nextInt();
					System.out.println(movies.get(inputNode).toString());
					System.out.println("Neighbors: ");
					for (Integer element : graph.getNeighbors(inputNode)){
						System.out.println(movies.get(element).getTitle());
					}
					break;
				case 3: input = 3;
					//System.out.print("hello");
					int numVertices = graph.numVertices();
					System.out.print("Enter starting node (1-" + numVertices + ") : ");
					int startingNode = scr.nextInt();
					System.out.print("Enter ending node (1-" + numVertices + ") : ");
					int endingNode = scr.nextInt();
					int[] previous = GraphAlgorithms.dijkstrasAlgorithm(graph, startingNode);
					int found = -1;
					for (int i = 0; i < previous.length; i++){
						if ( endingNode == previous[i] && movies.get(endingNode).getTitle().equals(movies.get(previous[i]))) {
							found = i;
						}
					}
					try {
						int previousId = previous[endingNode];
						System.out.println(movies.get(endingNode).getTitle() + "==>" + (movies.get(previous[endingNode])).getTitle());
						while (previousId != 0) {
							int movieIdNext = movies.get(previous[previousId]).getMovieId();
							System.out.println(movies.get(previousId).getTitle() + "==>" + movies.get(movieIdNext).getTitle());
							previousId = movies.get(previous[previousId]).getMovieId();
						}
					}
					catch (Exception e){

					}
					/*
					try{
						int previousId = movies.get(previous[endingNode]).getMovieId();
						System.out.println(movies.get(startingNode).getTitle() + "==>" + movies.get(previousId).getTitle());
						//int movieIdNext;
						while (previousId != 0) {
							int movieIdNext = movies.get(previous[previousId]).getMovieId();
							System.out.println(movies.get(previousId).getTitle() + "==>" + movies.get(movieIdNext).getTitle());
							previousId = movies.get(previous[previousId]).getMovieId();
						}
						System.out.println(movies.get(previousId).getTitle() + "==>" + movies.get(endingNode).getTitle());
					}
					catch (Exception err) {
						System.out.println("Not possible " + err);
					}
					 */
						break;
				case 4: input = 4;
					System.exit(-1);
			}
		}
	}

	/**
	 *
	 * @param movies
	 * @param reviewers
	 * @return returns a graph with edges to each vertex based upon i each movie has been rated the same as another movie by at least 12 other people
	 */
	private static Graph createGraphOption1(Map<Integer, Movie> movies, Map<Integer, Reviewer> reviewers){
		Graph tmpGraph = new Graph();
		//makes vertex for each movie thus building the graph
		for (Map.Entry<Integer,Movie> movie : movies.entrySet()){
			tmpGraph.addVertex(movie.getValue().getMovieId());
		}
		for (Map.Entry<Integer,Movie> movieEntry1 : movies.entrySet()){
			Map<Integer, Double> reviewerSet1 = movieEntry1.getValue().getRatings();
			for (Map.Entry<Integer,Movie> movieEntry2 : movies.entrySet()){
				Map<Integer, Double> reviewerSet2 = movieEntry2.getValue().getRatings();
				if (movieEntry1.getValue().getMovieId() != movieEntry2.getValue().getMovieId()){
					int reviewCount = 0;
					for (Map.Entry<Integer,Reviewer> reviewer : reviewers.entrySet()){
						//if they have seen the same movie and gave the same rating go into the if statement
						if ( -1 != reviewer.getValue().getMovieRating(movieEntry1.getValue().getMovieId()) && reviewer.getValue().getMovieRating(movieEntry1.getValue().getMovieId()) == reviewer.getValue().getMovieRating(movieEntry2.getValue().getMovieId()))
							++reviewCount;
						if (reviewCount >= 12 ){
							tmpGraph.addEdge(movieEntry1.getValue().getMovieId(),movieEntry2.getValue().getMovieId());
							break;
						}
					}
				}
			}
		}
		return tmpGraph;
	}

	/**
	 *
	 * @param movies
	 * @param reviewers
	 * @return returns a graph with an edge for each vertex if at least 12 people have seen both movies
	 */
	private static  Graph createGraphOption2(Map<Integer, Movie> movies, Map<Integer,Reviewer> reviewers) {
		Graph tmpGraph = new Graph();
		for(Map.Entry<Integer,Movie> movie : movies.entrySet()) {
			tmpGraph.addVertex(movie.getValue().getMovieId());
		}
		int numSeenBothMovies;
		for(Map.Entry<Integer, Movie> movie1 : movies.entrySet()) {
			for(Map.Entry<Integer,Movie> movie2 : movies.entrySet()) {
				numSeenBothMovies = 0;
				for (Map.Entry<Integer,Reviewer> reviewer : reviewers.entrySet()) {
					if ((reviewer.getValue().ratedMovie(movie1.getValue().getMovieId()) == true) && reviewer.getValue().ratedMovie(movie2.getValue().getMovieId()) == true) {
						numSeenBothMovies++;
						if (numSeenBothMovies == 12) {
							tmpGraph.addEdge(movie1.getValue().getMovieId(),movie2.getValue().getMovieId());
							break;
						}
					}
				}
			}
		}
		return tmpGraph;
	}
}
