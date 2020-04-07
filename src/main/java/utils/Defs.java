package utils;

public class Defs
{
    // API Values
    public static final String API_JSONRPC = "2.0"; // A mandatory key for Enjin requests that should have a value of "2.0".
    public static final String API_ID = "12345"; // An arbitrary ID number that is echoed in the API response.
    public static final String API_KEY = "6e7aac53b751cc230df25ddf093caea295957081c328b339"; // API key retrieved from https://rti.enjin.com/admin/settings/api.
    public static final String API_SESSION_ID = "c5p3fflrqnqq5g8mocr0jormj4"; // Session ID retrieved from running User.login with an "email" and "password".
    public static final String API_DOMAIN = "http://rti.enjin.com/api/v1/api.php"; // The API URL retrieved from https://rti.enjin.com/admin/settings/api.
    public static final String FILE_DELIMITER = ",";

    /**
     * Applications have varied over the years as we've changed different things we ask from applicants.
     */
    public enum ApplicationType
    {
        ANCIENT,
        LEGACY,
        STANDARD
    }
}