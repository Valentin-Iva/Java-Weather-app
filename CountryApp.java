import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.*;

public class CountryApp extends JFrame {
    RestCountriesWrapper rcw = new RestCountriesWrapper();
    JTextArea displayArea = new JTextArea(5, 20);
    String[] countries;
    CountryApp(){
        setTitle("CountryStats");
        setLayout(new BorderLayout(5, 5));

        JLabel heading = new JLabel("Country Stats App");
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        heading.setHorizontalAlignment(JLabel.CENTER);
        add(heading, BorderLayout.NORTH);

        JTextField country1 = new JTextField(10);
        JTextField country2 = new JTextField(10);
        JTextField country3 = new JTextField(10);
        JTextField country4 = new JTextField(10);
        JTextField country5 = new JTextField(10);
        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener(e -> {
            countries = new String[]{
                    country1.getText(),
                    country2.getText(),
                    country3.getText(),
                    country4.getText(),
                    country5.getText()
            };
            boolean validInput = true;
            for(String val : countries){
                if ("".equals(val)) {
                    validInput = false;
                }
            }
            if (validInput){
                displayArea.setText("Submitted!");
            }else{
                displayArea.setText("Invalid input! Enter 5 countries");
                countries = null;
            }

        });

        displayArea.setEditable(false);
        displayArea.setFont(new Font("Arial", Font.BOLD, 15));
        add(displayArea, BorderLayout.CENTER);
        add(selectPanel(countries), BorderLayout.SOUTH);

        JPanel inputPanel = new JPanel(new GridLayout(6,1));

        inputPanel.add(country1);
        inputPanel.add(country2);
        inputPanel.add(country3);
        inputPanel.add(country4);
        inputPanel.add(country5);
        inputPanel.add(submitButton);

        add(inputPanel, BorderLayout.WEST);
        displayArea.setText("<-Enter 5 countries...");
    }

    public JButton statOption(String stat, String statType){
        class Listener implements ActionListener {
            final String stat;
            final String statType;
            public Listener(String stat, String statType) {
                this.stat = stat;
                this.statType = statType;
            }
            public void actionPerformed(ActionEvent e){

            if (countries == null) {
                displayArea.setText("Invalid input:\nenter 5 countries and press Submit");
                return;
            }

            if (stat.equals("population")){
                HashMap<String, Integer> popMap =new HashMap<>();

                for (String val:countries){
                    String data = null;
                    try {
                        data = rcw.getCountryByName(val, "population");
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }

                    //System.out.println(data);

                    if (data != null && data.contains("\"population\"")) {

                        int max = 0;

                        String[] parts = data.split("}");

                        for (String part : parts) {
                            int index = part.indexOf(":");
                            if (index != -1) {
                                String numStr = part.substring(index + 1).replaceAll("[^0-9]", "");
                                if (!numStr.isEmpty()) {
                                    int value = Integer.parseInt(numStr);
                                    if (value > max) {
                                        max = value;
                                    }
                                }
                            }
                        }

                        popMap.put(val, max);
                    }
                }
                Populations populations = new Populations(popMap);
                if (statType.equals("AVG")){displayArea.setText("Average: " + populations.average());}
                if (statType.equals("SMALLEST")){displayArea.setText("Smallest: " + populations.smallest());}
                if (statType.equals("BIGGEST")){displayArea.setText("Biggest: " + populations.biggest());}
                String sortResult = "Sorted: " + Arrays.toString(populations.sorted());
                sortResult = sortResult.replace("[","").replace("]","");
                if (statType.equals("SORT")){displayArea.setText(sortResult);}

            }
            else if(stat.equals("borders")){
                HashMap<String, String[]> borderMap =new HashMap<>();
                for (String val:countries){
                    String data = null;
                    try {
                        data = rcw.getCountryByName(val, "borders");
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    if (data != null && data.contains("\"borders\"")) {
                       int start = data.indexOf(":") + 2;
                       int end = data.indexOf("]}", start);
                       String bordersStr = data.substring(start, end).trim();
                       String[] countryBorders = bordersStr.split(",");
                       String[] newBorders = new String[countryBorders.length];
                       for (int i = 0; i < countryBorders.length; i++){
                           String countryCode = countryBorders[i];
                           countryCode = countryCode.replace("\"", "");
                           try {
                               data = rcw.getCountryByCode(countryCode, "name");

                           } catch (Exception ex) {
                               throw new RuntimeException(ex);
                           }
                           if (data != null && data.contains("\"name\"")){
                               int nameStart = data.indexOf(":") + 11;
                               int nameEnd = data.indexOf(",", nameStart);
                               String nameStr = data.substring(nameStart, nameEnd).trim();
                               nameStr = nameStr.replace("\"","");
                               newBorders[i] = nameStr;
                               //System.out.println(nameStr);
                           }
                       }
                       borderMap.put(val,newBorders);

                    }
                }
                Borders borders = new Borders(borderMap);
                if(statType.equals("ALL")){displayArea.setText("All borders: \n" + borders.getAllBorders());}
                ArrayList<String> path = new ArrayList<>();
                ArrayList<String> shortest =null;
                String resultPath = "Shortest path between " + countries[0]+" and "+countries[4]+" through entered countries: \n" + (borders.findShortestPath(countries[0],countries[4], borderMap,path,shortest));
                resultPath = resultPath.replace("[","").replace("]","").replace(",","->");
                if(statType.equals("PATH")){displayArea.setText(resultPath);}
            }
            }}
        JButton jb = new JButton(stat+statType);
        jb.addActionListener(new Listener(stat, statType));
        return jb;
    }

    public JPanel selectPanel(String[] countries){
        JPanel selectPanel = new JPanel();

        selectPanel.add(statOption("population",  "AVG"));
        selectPanel.add(statOption("population",  "SMALLEST"));
        selectPanel.add(statOption("population", "BIGGEST"));
        selectPanel.add(statOption("population", "SORT"));
        selectPanel.add(statOption("borders", "ALL"));
        selectPanel.add(statOption("borders", "PATH"));

        return selectPanel;
    }

    public static void main() throws IOException{
        CountryApp CountryApp = new CountryApp();
        CountryApp.pack();
        CountryApp.setVisible(true);
        CountryApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
/*
**
INTRODUCTION:
This app allows the user to enter 5 countries. The programme then outputs statistics about the 5 countries such as average population, sorted by populations, shortest path between the countries and more.
**
* This class uses the main method as an entry point to my programme - Unit 1 (main method)
* This class uses Objects(instances of classes) such as populations(line 122), borders(line 168) - Unit 1 (Objects)
* This class uses different methods such as statOption, selectPanel and more. This was used to organise the code. For example statOption was used multiple times in selectPanel - Unit 1 (methods)

* This class uses a variety of primitive data types such as int and Object data types such as String(line 149 and more).
 Using int as appose to long in line 117 was because there was no country population that exceeds the recommended limit for int which is 2.5 billion therefore int was more efficient.
 It was also easer to work with in the populations class where I made a lot of calculations with the value - Unit 2 (data types)
* This class uses inheritence on line 11 to use methods from the JFrame class in the Java Swing library to create the GUI. - Unit 2 (Inheritance)

* This class uses conditionals in the form of if, else statements on lines 45,93 and more to validate input and decide which operations to use - Unit 3 (conditionals)
* This class uses for loops. These were used to operate on each item of the list of 5 countries entered by the user. - Unit 3 (loops)

* This class uses arrays in the form of String[]. These arrays were used to store information about the coutnries entered by users. The user will always
  enter 5 countries hence why I used arrays since they have a fixed size. They can alsoe easily be indexed and looped through and have more memory efficiency that Lists  - Unit 4 (Arrays)

* This class uses exception handling through try and catch hooks on lines 92 and 149. This was used when recieving data from the API through the RestCountriesWrapper - Unit 5 (Exceptions, Input & Output)

* This class uses 2D Graphics through inheriting the JFrame class from the Java Swing library. This includes objectsa and methods like JTextArea, JLabel, setFont, BorderLayout and more between
  lines 11 and 70. I also used elememts from the Abstract Window Toolkit library as it provides methods like ActionEvent and ActionListener to make the GUI more interactive   - Unit 6 (2D Graphics)

* This class briefly uses lists in the form of ArrayList object on lines 170. There were used in the implementation of methods from the Borders class (More examples of ArrayLists are used there. - Unit 7 (Lists)

* This class uses Maps in the form of a HashMap on lines 88 and 132. On 88, they were used to store countries inputed and their populations and use it as an argument for the populations object
On 132, a hashmap was used to store countries and they're corresponding array of borders - Unit 8 (Maps)

* This class uses GUI events with actionListener interface which was implemented on line 73. This was used for the functionality of the buttons -  Unit 9 (GUI Events)

 */