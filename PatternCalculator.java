// Developed by Grace Ciambrone on 30/04/2019
// This program was developed for the 2019 SeaPerch Competition. It aided in placing
// my SeaPerch team in 3rd for the international competition.
//
// This program has two purposes :
// 	1 ) To calculate the number of average guesses it takes to discover a
//          1x3 "ship" on a 4x4 grid given a guessing pattern provided by 
//          the user
// 	2)  To search for the most efficient guessing pattern possible


import java.util.*;
import java.lang.Math.*;

// contains arrangements for all ships
class Fleet {

  private int[][] allShips = { 
	  
	  {1,2,3}, {5,6,7}, {9,10,11}, {13,14,15},
	  {2,3,4}, {6,7,8}, {10,11,12}, {14,15,16},
	  {1,5,9}, {2,6,10}, {3,7,11}, {4,8,12},
	  {5,9,13}, {6,10,14}, {7,11,15}, {8,12,16},
	  {1,6,11}, {6,11,16}, {13,10,7}, {10,4,7},
	  {9,6,3}, {14,11,8}, {5,10,15}, {2, 7, 12}

  };

  public int[][] getAllShips() { return allShips; }

  public ArrayList getShip(int x) { 
	  ArrayList ship = new ArrayList();
     	  for(int i = 0; i < 3; i++) ship.add(allShips[x][i]);
	  return ship;
  }

}

class SearchData {

// these variables hold the ship coordinates that have been discovered
	private int pt_1 = -1;
	private int pt_2 = -1;
	private int pt_3 = -1;
	private int count;
	int p1_count;

	int[] pattern;
	ArrayList ship;

	public SearchData(int[] p, ArrayList s) {
		pattern = p;
		ship = s;
	}
        
        int getFirstPoint() { return pt_1; }
        int getSecondPoint() { return pt_2; }
        int getThirdPoint() { return pt_3; }	
	int getCount() { return count; }

        void setFirstPoint(int p) { pt_1 = p; }
        void setSecondPoint(int p){  pt_2 = p; }
        void setThirdPoint(int p) { pt_3 = p; }
	void setCount(int c) { count = c; }
}

class AdjacentUtil {

//holds the points adjacent to each point on the 4x4 grid
	int[][] adjacents = {
        	{2,5,6},  //................1
		{3,6,7},  //................2
		{2,6,7},  //................3               
		{3,7,8},  //................4
		{6,9,10},   //..............5
		{3,7,10,11},   //...........6
		{2,6,10,11},   //...........7
		{7,11,12},  //..............8
		{6,10,13},   //.............9
		{6,7,11,15},   //..........10
		{6,7,10,14},   //..........11
		{7,8,11},  //..............12
 		{9,10,14},   //............13
		{10,11,15},   //...........14
		{10,11,14},   //...........15
		{11,12,15}  //.............16
	};

	public ArrayList<Integer> getAdjacents(int x) {

                ArrayList<Integer> a = new ArrayList<>();

        	for(int i = 0; i < adjacents[x-1].length; i++) {
                	a.add(adjacents[x-1][i]);
		}
		
		return a;

	}

	public boolean isAdjacent(int point, int in_question) {

                ArrayList a = getAdjacents(point);
		if(a.contains(in_question)) return true;
		return false;

	}

	public int getLeft(int x) {
		if(x != 1 & x != 5 & x != 9 & x!= 13) return x-1;
		return -1;
	}

	public int getRight(int x) {
		if(x != 4 & x != 8 & x != 12 & x!= 16) return x+1;
		return -1;
	}

	public int getAbove(int x) {
		if(x != 1 & x != 2 & x != 3 & x!= 4) return x-4;
		return -1;
	}

	public int getBelow(int x) {
		if(x != 13 & x != 14 & x != 15 & x!= 16) return x+4;
		return -1;
	}

	public int getLeftPosDiagonal(int x) {
		if(x != 1 & x != 5 & x != 9 & x!= 13
		&& x != 13 & x != 14 & x != 15 & x!= 16) return x+3;
		return -1;
	}
	
	public int getRightPosDiagonal(int x) {
		if(x != 4 & x != 8 & x != 12 & x!= 16
		&& x != 1 & x != 2 & x != 3 & x!= 4) return x-3;
		return -1;
	}

	public int getLeftNegDiagonal(int x) {
		if(x != 1 & x != 5 & x != 9 & x!= 13
		&& x != 1 & x != 2 & x != 3 & x!= 4) return x-5;
		return -1;
	}

	public int getRightNegDiagonal(int x) {
		if(x != 4 & x != 8 & x != 12 & x!= 16
		&& x != 13 & x != 14 & x != 15 & x!= 16) return x+5;
		return -1;
	}

        public ArrayList<Integer> getHorizontalValues(SearchData sd) {

		int pt1 = sd.getFirstPoint();
		int pt2 = sd.getSecondPoint();

		ArrayList<Integer> values = new ArrayList<>();

		if(pt1>pt2) {
                        values.add(getRight(pt1));
			values.add(getLeft(pt2));
		}
		if(pt2>pt1) {
                        values.add(getRight(pt2));
			values.add(getLeft(pt1));
		}

		if(values.size() != 0) { 
			for(int i = 0; i < values.size(); i++) {
                                       if(values.get(i) == -1) values.remove(i);
			}
		}

		return values;
	}

        public ArrayList<Integer> getVerticalValues(SearchData sd) {

		int pt1 = sd.getFirstPoint(); int pt2 = sd.getSecondPoint();

		ArrayList<Integer> values = new ArrayList<>();

		if(pt2>pt1) {
                        values.add(getAbove(pt1));
			values.add(getBelow(pt2));
		}
		if(pt2<pt1) {
                        values.add(getAbove(pt2));
			values.add(getBelow(pt1));
		}

		if(values.size() != 0) { 
			for(int i = 0; i < values.size(); i++) {
                        	if(values.get(i) == -1) values.remove(i);
				      
			}
		}

		return values;
	}

        public ArrayList<Integer> getPosDiagonalValues(SearchData sd) {

		int pt1 = sd.getFirstPoint();
		int pt2 = sd.getSecondPoint();

		ArrayList<Integer> values = new ArrayList<>();

		if(pt2>pt1) {
                        values.add(getLeftPosDiagonal(pt2));
			//System.out.println("POS LEFT DIAGONAL: " + getLeftPosDiagonal(pt2));
			values.add(getRightPosDiagonal(pt1));
			//System.out.println("POS RIGHT DIAGONAL: " + getRightPosDiagonal(pt1));
		}
		if(pt2<pt1) {
                        values.add(getLeftPosDiagonal(pt1));
			//System.out.println("POS LEFT DIAGONAL: " + getLeftPosDiagonal(pt1));
			values.add(getRightPosDiagonal(pt2));
			//System.out.println("POS RIGHT DIAGONAL: " + getRightPosDiagonal(pt2));
		}

		if(values.size() != 0) { 
			for(int i = 0; i < values.size(); i++) {
                        	if(values.get(i) == -1) values.remove(i);
			}
		}

		return values;
	}

        public ArrayList<Integer> getNegDiagonalValues(SearchData sd) {

		int pt1 = sd.getFirstPoint();
		int pt2 = sd.getSecondPoint();

		ArrayList<Integer> values = new ArrayList<>();

		if(pt2<pt1) {
                        values.add(getLeftNegDiagonal(pt2));
			//System.out.println("NEG LEFT DIAGONAL: " + getLeftNegDiagonal(pt2));
			values.add(getRightNegDiagonal(pt1));
			//System.out.println("NEG RIGHT DIAGONAL: " + getRightNegDiagonal(pt1));
		}
		if(pt2>pt1) {
                        values.add(getLeftNegDiagonal(pt1));
			//System.out.println("NEG LEFT DIAGONAL: " + getLeftNegDiagonal(pt1));
			values.add(getRightNegDiagonal(pt2));
			//System.out.println("NEG RIGHT DIAGONAL: " + getRightNegDiagonal(pt2));
		}

		if(values.size() != 0) { 
			for(int i = 0; i < values.size(); i++) {
                        	if(values.get(i) == -1) values.remove(i);
			}
		}

		return values;
	}
}

class Search {

	final String ANSI_RESET = "\u001B[0m";
	final String ANSI_RED = "\u001B[31m";
        final String ANSI_RED_BACKGROUND = "\u001B[41m";
	final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

	AdjacentUtil au = new AdjacentUtil();

	public int[] getRandomPattern() {

		ArrayList<Integer> listResult = new ArrayList<>();
	        for(int i = 2; i<= 15; i++) listResult.add(i);
		listResult.remove(2);
		listResult.remove(11);
		Collections.shuffle(listResult);
		int size = listResult.size();
		int[] result = new int[size];
		Integer[] temp = listResult.toArray(new Integer[size]);
		for (int n = 0; n < size; ++n) { result[n] = temp[n]; } 
		return result;

	}
        
	// Finds first point of ship
	public void PatternSearch(SearchData sd) {

        	for(int i = 0; i < sd.pattern.length; i++) {
			if(sd.ship.contains(sd.pattern[i])) {
				//System.out.println(ANSI_YELLOW_BACKGROUND + "  >  1st Ship found @ " + sd.pattern[i] + " with count " + (i+1) + ANSI_RESET);
				sd.setFirstPoint(sd.pattern[i]);
				sd.setCount(i+1);
				sd.p1_count = i+1;
				break;
			}
		}
	}
        
	// Finds second point of ship
	public void RandomSearch(SearchData sd) {

		int count = sd.getCount();
		int pt1 = sd.getFirstPoint();
		ArrayList<Integer> aList = au.getAdjacents(pt1);
                
                for(int i = 0; i < count; i++) 
			if(aList.contains(sd.pattern[i])) aList.remove(Integer.valueOf(sd.pattern[i]));
		
		Collections.shuffle(aList);

		for(int i = 0; i < aList.size(); i++) {

			if(sd.ship.contains(aList.get(i))) {
				//System.out.println(ANSI_YELLOW_BACKGROUND + "  >  2nd Ship found @ " + aList.get(i) + " with count " + (count+i+1) + ANSI_RESET);
				sd.setSecondPoint(aList.get(i));
				sd.setCount(count+i+1);
				break;
			}
		}
	}
        
	// Sets third point of ship
	public void setThird(SearchData sd, ArrayList<Integer> list) {
		
                for(int a = 0; a < sd.p1_count; a++) {
			if(list.contains(sd.pattern[a])) list.remove(Integer.valueOf(sd.pattern[a]));
		}

		Collections.shuffle(list);
		int count = sd.getCount();

		for(int i = 0; i < list.size(); i++) {
                	
			if(sd.ship.contains(list.get(i))) {
				//System.out.println(ANSI_GREEN_BACKGROUND + "  >  3rd Ship found @ " + list.get(i) + " with count " + (count+i+1) + ANSI_RESET);
				sd.setThirdPoint(list.get(i));
				sd.setCount(count+i+1);
				break;
			}
		}
	}
        
	// Finds third point of ship
	public void thirdPointSearch(SearchData sd) {

		ArrayList<Integer> possible_values = new ArrayList<>();

		int pt1 = sd.getFirstPoint();
		int pt2 = sd.getSecondPoint();

        	if(Math.abs(pt1-pt2) == 1) {
			//System.out.println("Ship Orientation : HORIZONTAL");
                        possible_values = au.getHorizontalValues(sd);
			setThird(sd, possible_values);
		}

        	if(Math.abs(pt1-pt2) == 4) {
			//System.out.println("Ship Orientation : VERTICAL");
                        possible_values = au.getVerticalValues(sd);
			setThird(sd, possible_values);
			
		}
        	if(Math.abs(pt1-pt2) == 3) {
			//System.out.println("Ship Orientation : POS_DIAGONAL");
                        possible_values = au.getPosDiagonalValues(sd);
			setThird(sd, possible_values);
		}

        	if(Math.abs(pt1-pt2) == 5) {
			//System.out.println("Ship Orientation : NEG_DIAGONAL");
                        possible_values = au.getNegDiagonalValues(sd);
			setThird(sd, possible_values);
		}
	}
        
	// returns the average number of guesses it took to find all 3 ship points
	public double getPatternAvg(int[] pattern, int iterations) {

		Fleet fleet = new Fleet();
		ArrayList<SearchData> data_list = new ArrayList<>();

		double avg = 0.0;
		// cycles through each ship
		for(int x = 0; x < iterations; x++) {
			for(int i = 0; i < 24; i++) {

				SearchData sd = new SearchData(pattern, fleet.getShip(i));
	
				//System.out.println("SHIP " + i);
				PatternSearch(sd);
		
				if(sd.getSecondPoint() == -1) RandomSearch(sd);
				if(sd.getSecondPoint() != -1) thirdPointSearch(sd);
	
				avg += sd.getCount();
	
			}
		}

		avg = avg/(iterations * 24);
		return avg;

	}
}

class ProgressBarTraditional extends Thread {
	static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_LOW_INTENSITY	= "\u001B[2m";
  	boolean showProgress = true;
  	public void run() {
    		String anim  = "===================";
    		int x = 0;
    		while (showProgress) {
      			System.out.print(ANSI_LOW_INTENSITY + "\r\tProcessing "
           			+ anim.substring(0, x++ % anim.length()) + " ");
      			try { Thread.sleep(100); }
      			catch (Exception e) {};
    		}
  	}
}

public class PatternCalculator {
	
	static final String ANSI_RESET = "\u001B[0m";
	static final String ANSI_BOLD = "\u001B[1m";
        static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_LOW_INTENSITY	= "\u001B[2m";

	static boolean random_calc = false;
	
	public static void readRandomPatternBoolean() {

		Scanner sc = new Scanner(System.in); 
		System.out.print("\tRun random patterns? (Y/N/Q) : ");
		String next = sc.next();
		if(next.compareTo("Y") == 0) { random_calc = true; }
		else { random_calc = false; }
		if(next.compareTo("Q") == 0) System.exit(0);

	}

	public static int[] getUserPattern() {

		Scanner sc = new Scanner(System.in); 
		System.out.print(ANSI_BOLD + "\tEnter pattern length: ");
		int length = sc.nextInt(); 
		int[] input = new int[length]; 

		System.out.print(ANSI_BOLD + "\tEnter pattern sequence: ");

		for (int i = 0; i < length; i++) { 
			int userInput = sc.nextInt(); 
			input[i] = userInput; 
		} 
		
		System.out.print(ANSI_RESET);
		return input;

	}

       public static void handleUserInteractions() {
	       
	       Search search = new Search();
               System.out.println(); 

	       readRandomPatternBoolean(); 

	       if(random_calc == true) {

		       Scanner sc = new Scanner(System.in); 
		       System.out.print(ANSI_BOLD + "\tRandomly generate _____ patterns : ");
		       int generate = sc.nextInt(); 

		       System.out.print("\tTest each pattern  _____ times : ");
		       int iterations = sc.nextInt(); 

		       System.out.print("\tPrint averages less than : ");
		       double max = sc.nextDouble();
		       System.out.println(ANSI_RESET);
		       
		       for(int i = 0 ; i < generate; i++) {
		       		int[] random_pattern = search.getRandomPattern();
		       		double random_avg = search.getPatternAvg(random_pattern, iterations);
		
		       		if(random_avg < max) {
			       		System.out.print("\t\tPATTERN " + i + " ) ");
			       		for(int x = 0; x<random_pattern.length; x++) System.out.print(random_pattern[x] + " ");
		       			System.out.println(ANSI_RED_BACKGROUND + "\tAVERAGE COUNT: " + random_avg + ANSI_RESET);
		       		} 
		       }
	       }
	       else { 
		       int[] pattern = getUserPattern(); 
		       Scanner sc = new Scanner(System.in); 
		       System.out.print(ANSI_BOLD + "\tTest the pattern ______ times : ");
		       int iterations = sc.nextInt(); 
		       System.out.print(ANSI_RESET);
		       double pattern_avg = -1;
		       ProgressBarTraditional pb2 = new ProgressBarTraditional();
		       pb2.start();
		       pattern_avg = search.getPatternAvg(pattern, iterations);
		       pb2.showProgress = false;
		       System.out.println("\033[2K" + ANSI_RESET + ANSI_RED_BACKGROUND + "\r\tAVERAGE COUNT: " + pattern_avg + ANSI_RESET);
	       }
               
	       System.out.println(ANSI_BOLD + ANSI_YELLOW + "\n > NEW CALCULATION " + ANSI_RESET); 
	       handleUserInteractions();

       }

       public static void main(String[] args) {

	       /* PATTERN
		* must be between 1 and 16
		* must gaurantee at least one hit
		* should not contain repeats
	       */
	       
	       System.out.print("\033[H\033[2J");  
               System.out.flush(); 
	       System.out.println(ANSI_BOLD + ANSI_BLUE + "\n\n\tSEA PERCH PATTERN CALCULATOR" 
			       + ANSI_RESET + ANSI_LOW_INTENSITY + " by grace ciambrone | ciambronegrace@gmail.com" + ANSI_RESET);
	       System.out.println("\tGiven a pattern, this program calculates the number"
			       + " of guesses, on average, it takes to discover a hidden 1x3 ship on a 4x4 grid.");

	       System.out.println("\n\t  ---------------------");
	       System.out.println("\t  |  1 |  2 |  3 |  4 |");
	       System.out.println("\t  ---------------------");
	       System.out.println("\t  |  5 |  6 |  7 |  8 |");
	       System.out.println("\t  ---------------------");
	       System.out.println("\t  |  9 | 10 | 11 | 12 |");
	       System.out.println("\t  ---------------------");
	       System.out.println("\t  | 13 | 14 | 15 | 16 |");
	       System.out.println("\t  ---------------------");
                
               handleUserInteractions();

       }
}       
