package utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EnjinAPI
{
    private String jsonRPC;
    private String id;
    private String apiKey;
    private String sessionId;
    private String domain;
    private HttpClient client;

    // API Methods
    private static final String METHOD_GETAPPLICATIONSLIST = "Applications.getList";
    private static final String METHOD_GETAPPLICATION = "Applications.getApplications";

    // API Request Keys
    public static final String KEY_JSONRPC = "jsonrpc";
    public static final String KEY_ID = "id";
    public static final String KEY_PARAMS = "params";
    public static final String KEY_METHOD = "method";
    public static final String KEY_API_KEY = "api_key";
    public static final String KEY_SESSION_ID = "session_id";
    public static final String KEY_TYPE = "type";
    public static final String KEY_PAGE = "page";

    // API Response Keys
    public static final String KEY_APPLICATION_ID = "application_id";
    public static final String KEY_ADMIN_ENJIN_USERNAME = "admin_username";
    public static final String KEY_ENJIN_USERNAME = "username";
    public static final String KEY_GW2_USERNAME = "mqjpat5kz4";
    public static final String KEY_ALIAS = "3r5onxb4rg";
    public static final String KEY_DISCORD_USERNAME = "5smwqmm6wu";

    public EnjinAPI(String jsonRPC, String id, String apiKey, String sessionId, String domain)
    {
        this.jsonRPC = jsonRPC;
        this.id = id;
        this.apiKey = apiKey;
        this.sessionId = sessionId;
        this.domain = domain;
        this.client = HttpClient.newHttpClient();
    }

    public List<String> getApplicationIds()
    {
        List applicationIds = new ArrayList<String>();

        try
        {
            int page = 0;

            System.out.println("Getting application IDs...");
            while (true)
            {
                page++;
                System.out.print("Page: " + page + "... ");
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(domain))
                        .POST(HttpRequest.BodyPublishers.ofString(buildApplicationListRequestBody(page)))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JSONObject responseJson = new JSONObject(response.body());
                JSONArray applicationsJson = responseJson.getJSONObject("result").getJSONArray("items");
                Iterator applicationIterator = applicationsJson.iterator();

                // If it actually found items in the page, add their IDs to the list.
                if (applicationIterator.hasNext())
                {
                    while (applicationIterator.hasNext())
                    {
                        JSONObject currentApplication = (JSONObject)applicationIterator.next();
                        applicationIds.add(currentApplication.getString(KEY_APPLICATION_ID));
                    }
                }
                else break; // If we reach an empty page.

                System.out.println("Done! Size of list: " + applicationIds.size());
            }
        }
        catch (IOException e)
        {
            System.out.println("Error!\n" + e.getMessage());
        }
        catch (InterruptedException e)
        {
            System.out.println("Error!\n" + e.getMessage());
        }

        return applicationIds;
    }

    private String buildApplicationListRequestBody(int page)
    {
        JSONObject request = new JSONObject();
        JSONObject params = new JSONObject();

        params.put(KEY_API_KEY, apiKey);
        params.put(KEY_SESSION_ID, sessionId);
        params.put(KEY_TYPE, "approved");
        params.put(KEY_PAGE, page);

        request.put(KEY_JSONRPC, jsonRPC);
        request.put(KEY_ID, id);
        request.put(KEY_PARAMS, params);
        request.put(KEY_METHOD, METHOD_GETAPPLICATIONSLIST);

        return request.toString();
    }
}