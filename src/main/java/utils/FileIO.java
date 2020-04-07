package utils;

import objectclasses.Application;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileIO
{
    private static final String APPLICATION_IDS_PATH = "src/main/resources/applicationIDs.tmp";
    private static final String APPLICATIONS_PATH = "src/main/resources/applications.csv";

    /**
     * Retrieves the last ID in the file, i.e. the latest application ID in our local version of application IDs.
     * @return Returns a string containing the last application ID entry.
     * @throws IOException Generic IOException.
     * @throws ClassNotFoundException Couldn't convert the file data to a list of strings.
     */
    public static String getLastEntry() throws IOException, ClassNotFoundException
    {
        List<String> applicationIDs = readIDsFile();

        return applicationIDs.size() > 0 ? applicationIDs.get(0) : "";
    }

    /**
     * Writes the application IDs to the file in the filepath defined by applicationIdPath.
     * @param applicationIDs The list of strings of application IDs to write to the file.
     * @throws IOException Generic IOException.
     * @throws ClassNotFoundException Couldn't convert the file data to a list of strings.
     */
    public static void writeIDsToFile(List<String> applicationIDs) throws IOException, ClassNotFoundException
    {
        List<String> applicationIDsFile = readIDsFile();
        List<String> applicationIDsTotal = new ArrayList<>(applicationIDs);
        applicationIDsTotal.addAll(applicationIDsFile);

        FileOutputStream fs = new FileOutputStream(APPLICATION_IDS_PATH);
        ObjectOutputStream os = new ObjectOutputStream(fs);
        os.writeObject(applicationIDsTotal);
    }

    /**
     * Updates the file of application rows with the new applications.
     * @param applications A list of new Application objects to write to the file.
     * @throws IOException Generic IOException
     */
    public static void writeApplicationsToFile(List<Application> applications) throws IOException
    {
        List<String> applicationRows = applicationsListToRows(applications);
        List<String> applicationsFileRows = readApplicationsFile();
        applicationRows.addAll(applicationsFileRows);

        try (FileWriter writer = new FileWriter(APPLICATIONS_PATH))
        {
            for (String row : applicationRows)
            {
                writer.write(row + System.lineSeparator());
            }
        }
    }

    /**
     * Reads the application IDs from the file in the filepath defined by APPLICATION_IDS_PATH.
     * @return Returns the list of strings of application IDs found from the file.
     * @throws IOException Generic IOException.
     * @throws ClassNotFoundException Couldn't convert the file data to a list of strings.
     */
    private static List<String> readIDsFile() throws IOException, ClassNotFoundException
    {
        FileInputStream fi = new FileInputStream(APPLICATION_IDS_PATH);
        try (ObjectInputStream oi = new ObjectInputStream(fi))
        {
            return (ArrayList<String>) oi.readObject();
        }
        catch (EOFException e) // The file was empty right from the start.
        {
            return new ArrayList<>();
        }
    }

    /**
     * Reads the rows of applications from the file in the filepath defined by APPLICATIONS_PATH.
     * @return Returns a list of rows of applications found from the file.
     * @throws IOException Generic IOException.
     */
    private static List<String> readApplicationsFile() throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(new File(APPLICATIONS_PATH)));
        List<String> applicationRows = new ArrayList<>();
        String line;

        while ((line = br.readLine()) != null)
        {
            applicationRows.add(line);
        }

        return applicationRows;
    }

    /**
     * Converts a list of Application objects to a list of rows to write to the file.
     * @param applications A list of Application objects.
     * @return A list of string rows of applications, delimited using the FILE_DELIMITER constant in Defs.
     */
    private static List<String> applicationsListToRows(List<Application> applications)
    {
        List<String> applicationRows = new ArrayList<>();
        for (Application application : applications)
        {
            applicationRows.add(application.toString());
        }

        return applicationRows;
    }
}