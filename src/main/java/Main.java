import org.json.*;
import utils.Defs;
import utils.EnjinAPI;

import java.util.List;

public class Main
{
    public static void main(String[] args)
    {
        EnjinAPI enjinAPI = new EnjinAPI(
                Defs.API_JSONRPC,
                Defs.API_ID,
                Defs.API_KEY,
                Defs.API_SESSION_ID,
                Defs.API_DOMAIN);
        List<String> appIds = enjinAPI.getApplicationIds();

        System.out.println(appIds);
    }
}