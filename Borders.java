import java.util.ArrayList;
import java.util.HashMap;

public class Borders {
    HashMap<String, String[]> bordersMap;

    Borders(HashMap<String, String[]> bordersList){
        this.bordersMap = bordersList;
    }

    public String getAllBorders(){
        // country: borders
        String result = "";

        for (String country : bordersMap.keySet()) {
            String[] borders = bordersMap.get(country);
            result += country + ": " + String.join(", ", borders) + "\n";
        }

        return result;
    }

    public ArrayList<String> findShortestPath(String current, String target,
                                              HashMap<String, String[]> map,
                                              ArrayList<String> path,
                                              ArrayList<String> shortest) {


        path.add(current);

        // if target reached
        if (current.equals(target)) {
            if (shortest == null || path.size() < shortest.size()) {
                shortest = new ArrayList<>(path);
            }
        } else {
            String[] neighbors = map.get(current);
            if (neighbors != null) {
                for (String n : neighbors) {
                    if (!path.contains(n)) {
                        shortest = findShortestPath(n, target, map, path, shortest);
                    }
                }
            }
        }

        // backtrack
        path.remove(path.size() - 1);

        return shortest;
    }

}
/*
* This class uses methods on lines 11, 23 so that they can be called on when the class is implemented - Unit 1 (Methods)
* This class uses the String Object data type to store items in Arrays and Lists and to return results from methods - Unit 2 (Data types)
* This class uses encapsulation by making all the methods public so tht they can be used throughout the package - Unit 3 (Encapsulation)
* This class uses conditional if, else statements for the method logic - Unit 3 (Conditionals)
* This class uses for loop on line 15 to form an array into a String output for each item - Unit 3 (Loops)
* This class uses arrays in the form of String[] in order to store the borders for each country and iterate and apply methods to them - Unit 4 (Arrays)
* This class uses lists in the form of an ArrayList This was to store the path and shortest path of the findShortestPath method. This was
used because the path has a variable length in which case an Array is not suitable as it has to have a fixed length. - Unit 7 (Lists)
* This class uses Maps in the form of HashMap to receive the countries input by the user in the CountryApp class and their corresponding
borders - Unit 8 (Maps)
* This class uses recursion in the findShortestPath algorithm in order to implement backtracking to find the shortest path beetween 2 countries
through the other 3 countries entered by the user - Unit 10 (Recursion)
 */