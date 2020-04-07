package objectclasses;

import utils.Defs;

import static utils.Defs.FILE_DELIMITER;

public class Application
{
    private Defs.ApplicationType applicationType;
    private String applicationID;
    private String adminEnjinUsername;
    private String enjinUsername;
    private String gw2Username;
    private String alias;
    private String discordUsername;

    /**
     * Standard constructor for applications that include a Discord username.
     * @param applicationID Application ID retrieved from the getApplications list.
     * @param adminEnjinUsername The Enjin username of the admin that approved the application.
     * @param enjinUsername The Enjin username of the approved user.
     * @param gw2Username The Guild Wars 2 username of the approved user.
     * @param alias The alias of the approved user.
     * @param discordUsername The Discord username of the approved user.
     */
    public Application(String applicationID, String adminEnjinUsername, String enjinUsername, String gw2Username, String alias, String discordUsername)
    {
        this.applicationType = Defs.ApplicationType.STANDARD;
        this.applicationID = applicationID;
        this.adminEnjinUsername = adminEnjinUsername;
        this.enjinUsername = enjinUsername;
        this.gw2Username = gw2Username;
        this.alias = alias;
        this.discordUsername = discordUsername;
    }

    /**
     * Constructor for legacy applications that don't include a Discord username.
     * @param applicationID Application ID retrieved from the getApplications list.
     * @param adminEnjinUsername The Enjin username of the admin that approved the application.
     * @param enjinUsername The Enjin username of the approved user.
     * @param gw2Username The Guild Wars 2 username of the approved user.
     * @param alias The alias of the approved user.
     */
    public Application(String applicationID, String adminEnjinUsername, String enjinUsername, String gw2Username, String alias)
    {
        this.applicationType = Defs.ApplicationType.LEGACY;
        this.applicationID = applicationID;
        this.adminEnjinUsername = adminEnjinUsername;
        this.enjinUsername = enjinUsername;
        this.gw2Username = gw2Username;
        this.alias = alias;
    }

    /**
     * Constructor for very old applications that don't include an alias or Guild Wars 2 / Discord username.
     * @param applicationID Application ID retrieved from the getApplications list.
     * @param adminEnjinUsername The Enjin username of the admin that approved the application.
     * @param enjinUsername The Enjin username of the approved user.
     */
    public Application(String applicationID, String adminEnjinUsername, String enjinUsername)
    {
        this.applicationType = Defs.ApplicationType.ANCIENT;
        this.applicationID = applicationID;
        this.adminEnjinUsername = adminEnjinUsername;
        this.enjinUsername = enjinUsername;
    }

    @Override
    public String toString()
    {
        String applicationToString = "\"" + applicationID.replaceAll("\"", "\\\"") + "\"" + FILE_DELIMITER +
                "\"" + adminEnjinUsername.replaceAll("\"", "\\\"") + "\"" + FILE_DELIMITER +
                "\"" + enjinUsername.replaceAll("\"", "\\\"") + "\"" + FILE_DELIMITER;

        switch (applicationType)
        {
            case ANCIENT:
                applicationToString += FILE_DELIMITER + FILE_DELIMITER;
                break;

            case LEGACY:
                applicationToString += "\"" + gw2Username.replaceAll("\"", "\\\"") + "\"" + FILE_DELIMITER +
                        "\"" + alias.replaceAll("\"", "\\\"") + "\"" + FILE_DELIMITER;
                break;

            case STANDARD:
                applicationToString += "\"" + gw2Username.replaceAll("\"", "\\\"") + "\"" + FILE_DELIMITER +
                        "\"" + alias.replaceAll("\"", "\\\"") + "\"" + FILE_DELIMITER +
                        "\"" + discordUsername.replaceAll("\"", "\\\"") + "\"";
                break;
        }

        return applicationToString;
    }
}