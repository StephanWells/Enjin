import utils.Defs;
import utils.EnjinAPI;
import utils.FileIO;

import java.io.IOException;
import java.util.List;

public class Main
{
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
        writeApplicationIDsToFile(applicationIDs);
    }

    /**
     * Write the applicationIDs returned from the getApplicationIDs method to a file.
     * @param applicationIDs A list of strings of application IDs. These should be just IDs that are not in our local
     *                       copy of the IDs but were returned from the API.
     */
    private static void writeApplicationIDsToFile(List<String> applicationIDs)
    {
        try
        {
            System.out.print("Writing application IDs to file... ");
            FileIO.writeToFile(applicationIDs);
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
}