package dataProviders;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads the TestData.csv file
 */
public class CSVFileReader {


    /**
     * Path in system to Configuration.properties file
     */
    private final String testDataFilePath = "configs//TestData.csv";

    private List<String> Lines;


    /**
     * Constructor of CSVFileReader class.
     */
    public CSVFileReader() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(testDataFilePath)); // try loading the csv file

            this.Lines = new ArrayList<>();
            String line;
            try {
                while ((line = reader.readLine()) != null) { // try reading the file
                    Lines.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace(); // cannot read it
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("TestData.csv not found at " + testDataFilePath); // if ít doesnt find the file
        }

    }

    /**
     * Get student names from test data file
     * @return List of strings that are the full names
     */
    public List<String> getStudentNames() {
        List<String> studentNames = new ArrayList<>();

        // For all lines in csv file
        for (String line : Lines) {
            String[] itemsInLine = line.split(","); // A list of all different items of this line

            if (itemsInLine.length >= 1) { // If it specifies a studentName
                String studentName = itemsInLine[0].trim(); // Removes white spaces tabs and so on before and after name
                studentNames.add(studentName);
            }

        }

        return studentNames;
    }

    /**
     * Get student group numbers from test data file
     * @return List of integers that are group numbers of the student
     */
    public List<Integer> getStudentGroupNumbers() {
        List<Integer> studentGroupNumbers = new ArrayList<>();

        // For all lines in csv file
        for (String line : Lines) {
            String[] itemsInLine = line.split(","); // A list of all different items of this line

            if (itemsInLine.length >= 2) { // If it specifies a student group number
                String studentGroupNumberStr = itemsInLine[1].trim(); // Removes white spaces tabs and so on before and after name

                try {
                    int studentGroupNumber = Integer.parseInt(studentGroupNumberStr);
                    studentGroupNumbers.add(studentGroupNumber);
                } catch (NumberFormatException e) {// ignore invalid integer values
                }
            }

        }

        return studentGroupNumbers;
    }


}
