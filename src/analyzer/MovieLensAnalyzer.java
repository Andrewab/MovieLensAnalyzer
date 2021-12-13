package analyzer;
import data.Movie;
import data.Reviewer;
import graph.Graph;
import util.DataLoader;

import java.util.*;
/**
 * Please include in this comment you and your partner's name and describe any extra credit that you implement 
 */
public class MovieLensAnalyzer {
	public static void main(String[] args){
		// Your program should take two command-line arguments: 
		// 1. A ratings file
		// 2. A movies file with information on each movie e.g. the title and genre

		if(args.length != 2){
			System.err.println("Usage: java MovieLensAnalyzer [ratings_file] [movie_title_file]");
			System.exit(-1);
		}
		// FILL IN THE REST OF YOUR PROGRAM
		System.out.print("========== Welcome to movie lens analyzer ==========\n");
		System.out.print("The fiels being analyzed are: \n");
		System.out.print(args[0] + "\n");
		System.out.print(args[1] + "\n\n");
		System.out.print("There are 3 choices for defining adjacency:\n");
		System.out.print("[Option 1] u and v are adjacent if the same 12 users gave the same rating to both movies\n");
		System.out.print("[Option 2] u and v are adjacent if the same 12 users watched both movies(regardless of rating)\n");
		//System.out.print("[Option 3] u is adjacent to v if at least 33.0% of the users that rated u gave the same rating to v\n");
		System.out.print("Choose an option to build the graph (1-3): ");
		Scanner scr = new Scanner(System.in);
		int input = scr.nextInt();
		DataLoader loader = new DataLoader();
		loader.loadData(args[0],args[1]);
		Graph graph;
		Map reviewers = loader.getReviewers();
		Map movies = loader.getMovies();
		if(input == 1) {
			graph = createGraphOption1(movies, reviewers);
		}
        else if (input == 2) {
			graph = createGraphOption2(movies,reviewers);
	}
}

	private static Graph createGraphOption1(Map<Integer,Movie> movies, Map<Integer, Reviewer> reviewers) {
		Graph tmpGraph = new Graph();
		for (Map.Entry<Integer,Movie> movie : movies.entrySet()) {
			tmpGraph.addVertex(movie.getValue());
		}
		for (Map.Entry<Integer,Movie> movie1 : movies.entrySet()) {
			for(Map.Entry<Integer,Movie> movie2 : movies.entrySet()) {
				if(TwelveUsersRatedSame(reviewers, movie1.getValue(),movie2.getValue())) {
					tmpGraph.addEdge(movie1.getValue(),movie2.getValue());
				}
			}
		}
	return tmpGraph;
	}
	//returns true if reviewer1 and reviewer2 rated movie the  same, and also did not rate the movie -1
	private static boolean ratedMovieSame(Reviewer reviewer1, Reviewer reviewer2, Movie movie) {
		if((reviewer1.getMovieRating(movie.getMovieId()) == reviewer2.getMovieRating(movie.getMovieId())) && (reviewer1.getMovieRating(movie.getMovieId()) != -1)) {
			return true;
		}
		return  false;
	}
	//returns true if twelve users rated two seperate movies the same
	private static boolean TwelveUsersRatedSame(Map<Integer,Reviewer> reviewers, Movie movie1, Movie movie2) {
		int numRatedMovieSame = 0;
		ArrayList<Reviewer> reviewersWhoRatedMovieSame = new ArrayList<>();
		for(Map.Entry<Integer,Reviewer>reviewer1 : reviewers.entrySet()) {
			for(Map.Entry<Integer,Reviewer> reviewer2 : reviewers.entrySet()) {
				if(ratedMovieSame(reviewer1.getValue(),reviewer2.getValue(),movie1)){
				//if((reviewer1.getValue().getMovieRating(movie1.getMovieId()) == reviewer2.getValue().getMovieRating(movie1.getMovieId())) && reviewer1.getValue().getMovieRating(movie1.getMovieId()) != -1) {
					numRatedMovieSame++;
					reviewersWhoRatedMovieSame.add(reviewer2.getValue());
				}
				if (numRatedMovieSame >= 12) {
					if(TwelveUsersRatedSameHelper(reviewersWhoRatedMovieSame,movie2)) {
						return true;
					}
				}
			}
			numRatedMovieSame = 0;
			reviewersWhoRatedMovieSame = new ArrayList<>();
		}
		return false;
	}
	//helper to TwelveUsersRatedSame, returns true if reviewers have all rated the movie the same rating
	private static boolean TwelveUsersRatedSameHelper(ArrayList<Reviewer> reviewers, Movie movie) {
		for (Reviewer reviewer1 : reviewers) {
			for (Reviewer reviewer2 : reviewers) {
				if(reviewer1.getMovieRating(movie.getMovieId()) != reviewer2.getMovieRating(movie.getMovieId())) {
					return false;
				}
			}
		}
		return  true;
	}

	private static Graph createGraphOption2(Map<Integer,Movie> movies, Map<Integer,Reviewer> reviewers) {
		Graph tmpGraph = new Graph();
		for(Map.Entry<Integer,Movie> movie : movies.entrySet()) {
			tmpGraph.addVertex(movie.getValue());
		}
		for (Map.Entry<Integer,Movie> movie1 : movies.entrySet()) {
			for(Map.Entry<Integer,Movie> movie2 : movies.entrySet()) {
				if(TwelveUsersHaveSeen(movie1.getValue(),movie2.getValue(),reviewers)) {
					tmpGraph.addEdge(movie1.getValue(),movie2.getValue());
				}
			}
		}
		return tmpGraph;
	}
	//Returns true if twelve seperate users have seen two seperate movies
	private static boolean TwelveUsersHaveSeen(Movie movie1, Movie movie2, Map<Integer, Reviewer> reviewers) {
		int numPeopleSeemSameMovie = 0;
		ArrayList<Reviewer> reviewersWhoHaveSeenSameMovie = new ArrayList<>();
		for (Map.Entry<Integer,Reviewer> reviewer1 : reviewers.entrySet()) {
			for (Map.Entry<Integer,Reviewer> reviewer2 : reviewers.entrySet()) {
				if(haveBothSeenMovie(reviewer1,reviewer2,movie1)) {
					numPeopleSeemSameMovie++;
					reviewersWhoHaveSeenSameMovie.add(reviewer2.getValue());
				}
				if(numPeopleSeemSameMovie >= 12) {
					if(TwelveUsersHaveSeenHelper(reviewersWhoHaveSeenSameMovie,movie2)) {
						return true;
					}
				}
			}
			numPeopleSeemSameMovie = 0;
			reviewersWhoHaveSeenSameMovie = new ArrayList<>();
		}
		return false;
	}
	//Helper function to TwelveUsersHaveSeen, returns true if the list of reiverwers have seen all seeen the same movie
	private static boolean TwelveUsersHaveSeenHelper(ArrayList<Reviewer> reviewersWhoHaveSeenSameMovie, Movie movie) {
		for (Reviewer reviewer1 : reviewersWhoHaveSeenSameMovie) {
			for (Reviewer reviewer2 : reviewersWhoHaveSeenSameMovie) {
				if(reviewer1.ratedMovie(movie.getMovieId()) != reviewer2.ratedMovie(movie.getMovieId())) {
					return false;
				}
			}
		}
		return true;
	}

	private static boolean haveBothSeenMovie(Map.Entry<Integer, Reviewer> reviewer1, Map.Entry<Integer, Reviewer> reviewer2, Movie movie) {
		if((reviewer1.getValue().ratedMovie(movie.getMovieId()) == reviewer2.getValue().ratedMovie(movie.getMovieId())) && (reviewer1.getValue().ratedMovie(movie.getMovieId()) != false)) {
			return true;
		}
		return false;
	}

}
