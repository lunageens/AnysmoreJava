package managers;

import dataProviders.CSVFileReader;
import dataProviders.ConfigFileReader;


/**
 * To control object creation of all readers, we use the SingleTonPattern
 * It's appropriate to have exactly one instance of a class that has a global point of access in the system.
 * In our case, we have ConfigReaderFile, which should be accessed globally.
 * But later on in this Selenium Cucumber Framework series, we will be having many more file readers.
 * So it is better to have a File Reader Manager above all the File Readers. And it is better to make the manager class as singleton.
 */
public class FileReaderManager {

    /**
     * One instance of FileReaderManager that used in the whole system.
     */
    private static FileReaderManager fileReaderManager = new FileReaderManager(); // only instance of this class

    /**
     * Instance of ConfigFileReader that is used.
     */
    private static ConfigFileReader configFileReader;

    private static CSVFileReader csvFileReader;

    /**
     * Private constructor of FileReaderManager
     */
    private FileReaderManager() { // private constructor to restrict initiation of the class from other classes
    }

    /**
     * Get one instance of FileReaderManager in whole system.
     * Will be used to globally access that one instance.
     *
     * @return FileReaderManager Instance of FileReaderManager that is in use
     */
    public static FileReaderManager getInstance() { // this is public, only global access point to get the instance
        return fileReaderManager;
    }

    /**
     * Get the one ConfigReader in whole system
     * If there is not a ConFigFileReader created yet, make a new one.
     * Otherwise, use the one we have.
     * This allows us to not make many instances of ConfigFileReader.
     *
     * @return ConfigFileReader The one instance of this class that is used in the system.
     */
    public ConfigFileReader getConfigReader() { // if we don't have one, make one. Otherwise, use the one we have
        return (configFileReader == null) ? new ConfigFileReader() : configFileReader;
    }

    /**
     * Get the one CSVFileReader in the whole system
     * If we dont have one yet, make one.
     * @return CSVFileReader The one instance of this class that is used in the system.
     */
    public CSVFileReader getCsvFileReader(){
        return (csvFileReader == null) ? new CSVFileReader() : csvFileReader;
    }
}