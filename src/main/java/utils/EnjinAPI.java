package utils;

import objectclasses.Application;
import org.json.JSONArray;
import org.json.JSONException;
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
    private static final String METHOD_GET_APPLICATIONS_LIST = "Applications.getList";
    private static final String METHOD_GET_APPLICATION = "Applications.getApplication";

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
    public static final String KEY_USER_DATA = "user_data";

    /**
     * Constructor.
     * @param jsonRPC A mandatory key for Enjin requests that should have a value of "2.0".
     * @param id An arbitrary ID number that is echoed in the API response.
     * @param apiKey API key retrieved from https://rti.enjin.com/admin/settings/api.
     * @param sessionId Session ID retrieved from running User.login with an "email" and "password".
     * @param domain The API URL retrieved from https://rti.enjin.com/admin/settings/api.
     */
    public EnjinAPI(String jsonRPC, String id, String apiKey, String sessionId, String domain)
    {
        this.jsonRPC = jsonRPC;
        this.id = id;
        this.apiKey = apiKey;
        this.sessionId = sessionId;
        this.domain = domain;
        this.client = HttpClient.newHttpClient();
    }

    /**
     * Method for retrieving new application IDs from the Enjin APIs. These can be used to get actual application info.
     * @return Returns a list of applications that are not already in the applicationIDs file.
     */
    public List<String> getApplicationIDs()
    {
        List<String> applicationIds = new ArrayList<>();

        try
        {
            int page = 0;
            String lastEntry = "";

            try
            {
                System.out.println("Reading last entry from file...");
                lastEntry = FileIO.getLastEntry();
            }
            catch (IOException e)
            {
                System.out.println("Error reading from file!\n" + e.getMessage());
            }
            catch (ClassNotFoundException e)
            {
                System.out.println("Error converting file to class!\n" + e.getMessage());
            }

            System.out.println("Getting application IDs...");
            boolean active = true;

            while (active)
            {
                page++;
                Iterator<Object> applicationIterator = getApplicationIdPage(page);

                // If it actually found items in the page, add their IDs to the list.
                if (applicationIterator.hasNext())
                {
                    while (applicationIterator.hasNext())
                    {
                        JSONObject currentApplication = (JSONObject)applicationIterator.next();
                        String entry = currentApplication.getString(KEY_APPLICATION_ID);

                        if (!entry.equals(lastEntry)) // If we haven't found an entry that we already have.
                        {
                            applicationIds.add(currentApplication.getString(KEY_APPLICATION_ID));
                        }
                        else
                        {
                            System.out.print("Entry [" + entry + "] already exists... ");
                            active = false;
                            break;
                        }
                    }

                    System.out.println("Done! Size of list: " + applicationIds.size());
                }
                else // If we reach an empty page.
                {
                    System.out.println("Empty page! Size of list: " + applicationIds.size());
                    active = false;
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("IO Error!\n" + e.getMessage());
        }
        catch (InterruptedException e)
        {
            System.out.println("Interrupted Error!\n" + e.getMessage());
        }

        return applicationIds;
    }

    /**
     * Helper method for getApplicationIds that calls the Applications.getApplications API method.
     * @param page The page of the applications to request.
     * @return Returns an iterator for a page of Enjin applications.
     * @throws IOException Generic IOException.
     * @throws InterruptedException The call to the API failed.
     */
    private Iterator<Object> getApplicationIdPage(int page) throws IOException, InterruptedException
    {
        System.out.print("Page: " + page + "... ");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(domain))
                .POST(HttpRequest.BodyPublishers.ofString(buildApplicationListRequestBody(page)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject responseJson = new JSONObject(response.body());
        JSONArray applicationsJson = responseJson.getJSONObject("result").getJSONArray("items");

        return applicationsJson.iterator();
    }

    /**
     * Building the request body that the getApplications API method requires.
     * @param page The page of the applications to request.
     * @return Returns a string with the body to put in the POST request to the API.
     */
    private String buildApplicationListRequestBody(int page)
    {
        JSONObject request = new JSONObject();
        JSONObject params = new JSONObject();

        params.put(KEY_API_KEY, apiKey);
        params.put(KEY_SESSION_ID, sessionId);
        params.put(KEY_TYPE, "approved"); // We only want to consider applications already approved in Enjin.
        params.put(KEY_PAGE, page);

        request.put(KEY_JSONRPC, jsonRPC);
        request.put(KEY_ID, id);
        request.put(KEY_PARAMS, params);
        request.put(KEY_METHOD, METHOD_GET_APPLICATIONS_LIST);

        return request.toString();
    }

    public List<Application> getApplicationsFromIDList(List<String> applicationIDs)
    {
        List<Application> applicationsList = new ArrayList<>();
        int counter = 0;
        int total = applicationIDs.size();

        try
        {
            for (String applicationID : applicationIDs)
            {
                counter++;
                System.out.print(counter + "/" + total + ": Getting application with ID " + applicationID + "... ");
                applicationsList.add(getApplicationFromID(applicationID));
                System.out.println("Done!");
            }
        }
        catch (IOException e)
        {
            System.out.println("IO Error!\n" + e.getMessage());
        }
        catch (InterruptedException e)
        {
            System.out.println("Interrupted Error!\n" + e.getMessage());
        }

        return applicationsList;
    }

    /**
     * Helper method for getApplications that calls the Applications.getApplication API method.
     * @param applicationID The ID of the application to request.
     * @return Returns an application from the API response.
     * @throws IOException Generic IOException.
     * @throws InterruptedException The call to the API failed.
     */
    private Application getApplicationFromID(String applicationID) throws IOException, InterruptedException
    {
        Application application;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(domain))
                .POST(HttpRequest.BodyPublishers.ofString(buildApplicationGetRequestBody(applicationID)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject responseJson = new JSONObject(response.body());
        JSONObject resultJson = responseJson.getJSONObject("result");
        application = jsonToApplication(resultJson);

        return application;
    }

    /**
     * Converts a JSON result object to an application object.
     * @param result The result JSON object retrieved from a getApplication API call.
     * @return Returns an application that dynamically changes its ApplicationType depending on how many details we were
     * able to get out of the API.
     */
    private Application jsonToApplication(JSONObject result)
    {
        String applicationID = result.getString(KEY_APPLICATION_ID);
        String adminEnjinUsername = result.getString(KEY_ADMIN_ENJIN_USERNAME);
        String enjinUsername = result.getString(KEY_ENJIN_USERNAME);

        try
        {
            JSONObject userData = result.getJSONObject(KEY_USER_DATA);
            String gw2Username = userData.getString(KEY_GW2_USERNAME);
            String alias = userData.getString(KEY_ALIAS);
            // If we got this far the application isn't an ancient application but might be a legacy one.
            try
            {
                String discordUsername = userData.getString(KEY_DISCORD_USERNAME);
                return new Application(applicationID, adminEnjinUsername, enjinUsername,
                                gw2Username, alias, discordUsername); // If we got this far, the application is a standard one.
            }
            catch (JSONException e) // We failed to get the Discord name, so the application is a legacy application.
            {
                return new Application(applicationID, adminEnjinUsername, enjinUsername, gw2Username, alias);
            }
        }
        catch (JSONException e)
        {
            // We failed to get the gw2Username and alias, so the application is an ancient application.
            return new Application(applicationID, adminEnjinUsername, enjinUsername);
        }
    }

    /**
     * Building the request body that the getApplication API method requires.
     * @param applicationID The application ID as a string retrieved from file or the API.
     * @return Returns a string with the body to put in the POST request to the API.
     */
    private String buildApplicationGetRequestBody(String applicationID)
    {
        JSONObject request = new JSONObject();
        JSONObject params = new JSONObject();

        params.put(KEY_API_KEY, apiKey);
        params.put(KEY_SESSION_ID, sessionId);
        params.put(KEY_APPLICATION_ID, applicationID);

        request.put(KEY_JSONRPC, jsonRPC);
        request.put(KEY_ID, id);
        request.put(KEY_PARAMS, params);
        request.put(KEY_METHOD, METHOD_GET_APPLICATION);

        return request.toString();
    }
}