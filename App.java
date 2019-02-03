import edu.sdsu.cs.datastructures.DirectedGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class App extends DirectedGraph {

    public static void main(String args[]) {

        DirectedGraph<String> directedGraph = new DirectedGraph();
        LinkedList<String> names = new LinkedList<>();
        String csvFile = "./layout.csv";

        if (args.length >= 1) {
            csvFile = args[0];
        }

        try {
            Scanner fileScanner = new Scanner(new File(csvFile));
            while (fileScanner.hasNext()){
                String line = fileScanner.nextLine();
                String[] tokens = line.split(",");

                if(tokens.length == 1) {
                    directedGraph.add(tokens[0]);
                    names.add(tokens[0]);
                }

                else if(tokens.length == 2 && !tokens[1].isEmpty()) {
                    directedGraph.add(tokens[0]);
                    directedGraph.add(tokens[1]);
                    if(!names.contains(tokens[0])) {
                        names.add(tokens[0]);
                    }
                    if(!names.contains(tokens[1])) {
                        names.add(tokens[1]);
                    }
                    directedGraph.connect(tokens[0], tokens[1]);
                }
            }
            fileScanner.close();

            System.out.println(directedGraph.toString(directedGraph, names));

            Scanner userScanner = new Scanner(System.in);
            System.out.print("Enter starting vertice: "  );
            String startLocation = userScanner.nextLine();
            System.out.print("Enter destination vertice: "  );
            String destinationLocation = userScanner.nextLine();

            userScanner.close();

            if (!(directedGraph.contains(startLocation) && directedGraph.contains(destinationLocation))) {
                System.out.println("Invalid location entered");
                System.exit(-1);
            }

            List<String> shortestPath = directedGraph.shortestPath(startLocation, destinationLocation);
            System.out.println("\nThe shortest distance between " + startLocation + " and " + destinationLocation
                    + " is " + (shortestPath.size() - 1) + " with a path of " + shortestPath.toString());

        } catch(FileNotFoundException e) {
            System.out.println("Unable to open " + csvFile + ". Verify the file exists, is\n" +
                    "accessible, and meets the syntax requirements.");
        } catch(Exception e) {
            System.out.println("An error occurred");
            e.printStackTrace();
        }
    }
}
