import objectclasses.Application;
import org.json.JSONException;
import utils.Defs;
import utils.EnjinAPI;
import utils.FileIO;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main
{
    /**
     * Main method.
     */
    public static void main(String[] args)
    {
        Defs.init(); // Initialise any dynamic API values.

        // Set up the interface for making requests to Enjin's API.
        EnjinAPI enjinAPI = new EnjinAPI(
                Defs.API_JSONRPC,
                Defs.API_ID,
                Defs.API_KEY,
                Defs.API_SESSION_ID,
                Defs.API_DOMAIN);

        try
        {
            updateApplications(enjinAPI);
        }
        catch (JSONException e)
        {
            System.out.println("JSON Error!\n" + e.getMessage());

            // If session ID is expired.
            if (e.getMessage().contains("Authentication Failed"))
            {
                System.out.print("Please enter new sessionId: ");
                Scanner sc = new Scanner(System.in);
                Defs.API_SESSION_ID = sc.next();
                FileIO.updateSessionId(Defs.API_SESSION_ID);
                System.out.println("Retrying...");

                enjinAPI = new EnjinAPI(
                        Defs.API_JSONRPC,
                        Defs.API_ID,
                        Defs.API_KEY,
                        Defs.API_SESSION_ID,
                        Defs.API_DOMAIN);

                updateApplications(enjinAPI);
            }
        }
    }

    private static void updateApplications(EnjinAPI enjinAPI)
    {
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