package net.mistwood.FarmingPlugin.Utils;

import com.mongodb.ConnectionString;

public class MongoURIBuilder
{

    private String Host;
    private int Port;
    private String Name;
    private String Username;
    private String Password;

    public static MongoURIBuilder Builder ()
    {
        return new MongoURIBuilder ();
    }

    public ConnectionString Build ()
    {
        // TODO: Allow multiple methods, such as no auth, etc.
        String URI = "mongodb://" + Username + ":" + Password + "@" + Host + ":" + Port + "/" + Name;
        return new ConnectionString (URI);
    }

    public MongoURIBuilder Host (String Host)
    {
        this.Host = Host;
        return this;
    }

    public MongoURIBuilder Host (String Host, int Port)
    {
        this.Host = Host;
        this.Port = Port;
        return this;
    }

    public MongoURIBuilder Port (int Port)
    {
        this.Port = Port;
        return this;
    }

    public MongoURIBuilder Database (String Name)
    {
        this.Name = Name;
        return this;
    }

    public MongoURIBuilder User (String Username, String Password)
    {
        this.Username = Username;
        this.Password = Password;
        return this;
    }

    public MongoURIBuilder Username (String Username)
    {
        this.Username = Username;
        return this;
    }

    public MongoURIBuilder Password (String Password)
    {
        this.Password = Password;
        return this;
    }

}
