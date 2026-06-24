import java.util.*;

public class Populations {
    HashMap<String, Integer> populations;

    Populations(HashMap<String, Integer> populations){
        this.populations = populations;
    }

    public int average(){
        //find the average

        int sum=0;
        for(int value: populations.values()){
            sum+=value;
        }
        return sum/populations.size();
    }

    public String biggest(){

        int max = Integer.MIN_VALUE;
        String country = "";

        for (Map.Entry<String, Integer> entry : populations.entrySet()){
            if (entry.getValue() > max){
                max = entry.getValue();
                country = entry.getKey();
            }
        }

        return country + ": " + max;
    }

    public String smallest(){

        int min = Integer.MAX_VALUE;
        String country = "";

        for (Map.Entry<String, Integer> entry : populations.entrySet()){
            if (entry.getValue() < min){
                min = entry.getValue();
                country = entry.getKey();
            }
        }

        return country + ": " + min;
    }

    public String[] sorted(){

        //return sorted list of names and they're populations
        List<Map.Entry<String, Integer>> list = new ArrayList<>(populations.entrySet());
        list.sort((a, b) -> b.getValue() - a.getValue());
        String[] result = new String[list.size()];
        int i = 0;

        for (Map.Entry<String, Integer> entry : list){
            result[i] = entry.getKey() + ": " + entry.getValue();
            i++;
        }

        return result;
    }
}

/*
* This class uses methods so that they can be called on when the class is implemented - Unit 1 (Methods)
* This class uses String Object, int primitive and Integer Object data types. Integer is used for the HashMap as HashMaps can only take
Object data types - Unit 2 (data types)
* This class uses encapsulation by making all the methods public so that they can be used throughout the package - Unit 3 (Encapsulation)
* This class uses conditional if, else statements for the method logic - Unit 3 (Conditionals)
* This class uses for loops to form an array into a String output for each item and loop through each item in the Array of country
populations- Unit 3 (Loops)
* This class uses arrays in the form of String[] in order to store the population for each country to iterate and apply methods to
the array - Unit 4 (Arrays)
* This class uses lists in the form of an ArrayList. I converted the contents of the HashMap into an arraylist in order to sort the
populations easily with the sort method of the ArrayList Object - Unit 7 (Lists)
* This class uses Maps in the form of HashMap to receive the countries input by the user in the CountryApp class and their corresponding
population - Unit 8 (Maps)
 */