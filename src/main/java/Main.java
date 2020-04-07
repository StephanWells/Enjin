import objectclasses.Application;
import utils.Defs;
import utils.EnjinAPI;
import utils.FileIO;

import java.io.IOException;
import java.util.List;

public class Main
{
    /**
     * Main method.
     */
    public static void main(String[] args)
    {
        // Set up the interface for making requests to Enjin's API.
        EnjinAPI enjinAPI = new EnjinAPI(
                Defs.API_JSONRPC,
                Defs.API_ID,
                Defs.API_KEY,
                Defs.API_SESSION_ID,
                Defs.API_DOMAIN);

        List<String> applicationIDs = enjinAPI.getApplicationIDs();
        updateApplicationIDsFile(applicationIDs);
        List<Application> applications = enjinAPI.getApplicationsFromIDList(applicationIDs);
        updateApplicationsFile(applications);
    }

    /**
     * Write the applicationIDs returned from the getApplicationIDs method to a file.
     * @param applicationIDs A list of strings of application IDs. These should be just IDs that are not in our local
     *                       copy of the IDs but were returned from the API.
     */
    private static void updateApplicationIDsFile(List<String> applicationIDs)
    {
        try
        {
            System.out.print("Writing application IDs to file... ");
            FileIO.writeIDsToFile(applicationIDs);
            System.out.println("Done!");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Error, class not found!\n" + e.getMessage());
        }
        catch (IOException e)
        {
            System.out.println("Error, IO exception!\n" + e.getMessage());
        }
    }

    /**
     * Write the applications returned from the getApplicationsFromIDList method to a file.
     * @param applications A list of Application objects. These should be just applications that are not in our local
     **                    copy of the applications but were returned from the API.
     */
    private static void updateApplicationsFile(List<Application> applications)
    {
        try
        {
            System.out.print("Writing applications to file... ");
            FileIO.writeApplicationsToFile(applications);
            System.out.println("Done!");
        }
        catch (IOException e)
        {
            System.out.println("Error, IO exception!\\n" + e.getMessage());
        }
    }
}