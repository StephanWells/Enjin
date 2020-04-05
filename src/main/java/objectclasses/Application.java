package objectclasses;

import utils.Defs;

public class Application
{
    private Defs.ApplicationType applicationType;
    private String applicationId;
    private String adminEnjinUsername;
    private String enjinUsername;
    private String gw2Username;
    private String alias;
    private String discordUsername;

    public Application(String applicationId, String adminEnjinUsername, String enjinUsername, String gw2Username, String alias, String discordUsername)
    {
        this.applicationType = Defs.ApplicationType.STANDARD;
        this.applicationId = applicationId;
        this.adminEnjinUsername = adminEnjinUsername;
        this.enjinUsername = enjinUsername;
        this.gw2Username = gw2Username;
        this.alias = alias;
        this.discordUsername = discordUsername;
    }

    public Application(String applicationId, String adminEnjinUsername, String enjinUsername, String gw2Username, String alias)
    {
        this.applicationType = Defs.ApplicationType.LEGACY;
        this.applicationId = applicationId;
        this.adminEnjinUsername = adminEnjinUsername;
        this.enjinUsername = enjinUsername;
        this.gw2Username = gw2Username;
        this.alias = alias;
    }

    public Application(String applicationId, String adminEnjinUsername, String enjinUsername)
    {
        this.applicationType = Defs.ApplicationType.ANCIENT;
        this.applicationId = applicationId;
        this.adminEnjinUsername = adminEnjinUsername;
        this.enjinUsername = enjinUsername;
    }

    public Defs.ApplicationType getApplicationType()
    {
        return applicationType;
    }

    public String getApplicationId()
    {
        return applicationId;
    }

    public String getAdminEnjinUsername()
    {
        return adminEnjinUsername;
    }

    public String getEnjinUsername()
    {
        return enjinUsername;
    }

    public String getGw2Username()
    {
        return gw2Username;
    }

    public String getAlias()
    {
        return alias;
    }

    public String getDiscordUsername()
    {
        return discordUsername;
    }
}