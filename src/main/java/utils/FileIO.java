package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileIO
{
    private static final String applicationIdPath = "src/main/resources/applicationIDs.tmp";

    /**
     * Retrieves the last ID in the file, i.e. the latest application ID in our local version of application IDs.
     * @return Returns a string containing the last application ID entry.
     * @throws IOException Generic IOException.
     * @throws ClassNotFoundException Couldn't convert the file data to a list of strings.
     */
    public static String getLastEntry() throws IOException, ClassNotFoundException
    {
        List<String> applicationIDs = readFile();

        return applicationIDs.size() > 0 ? applicationIDs.get(0) : "";
    }

    /**
     * Writes the application IDs to the file in the filepath defined by applicationIdPath.
     * @param applicationIDs The list of strings of application IDs to write to the file.
     * @throws IOException Generic IOException.
     * @throws ClassNotFoundException Couldn't convert the file data to a list of strings.
     */
    public static void writeToFile(List<String> applicationIDs) throws IOException, ClassNotFoundException
    {
        List<String> applicationIDsFile = readFile();

        applicationIDs.addAll(applicationIDsFile);

        FileOutputStream fs = new FileOutputStream(applicationIdPath);
        ObjectOutputStream os = new ObjectOutputStream(fs);
        os.writeObject(applicationIDs);
    }

    /**
     * Reads the application IDs from the file in the filepath defined by applicationIdPath.
     * @return Returns the list of strings of application IDs found from the file.
     * @throws IOException Generic IOException.
     * @throws ClassNotFoundException Couldn't convert the file data to a list of strings.
     */
    public static List<String> readFile() throws IOException, ClassNotFoundException
    {
        FileInputStream fi = new FileInputStream(applicationIdPath);
        try (ObjectInputStream oi = new ObjectInputStream(fi))
        {
            return (ArrayList<String>)oi.readObject();
        }
        catch (EOFException e)
        {
            return new ArrayList<String>();
        }
    }
}