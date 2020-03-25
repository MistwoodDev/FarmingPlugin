package net.mistwood.FarmingPlugin.Database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.connection.ClusterSettings;
import net.mistwood.FarmingPlugin.Config;
import net.mistwood.FarmingPlugin.Utils.MongoURIBuilder;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;
import static java.util.Arrays.asList;

public class DatabaseManager
{

    private Config Config;

    private MongoClient Client;
    private MongoDatabase Database;
    private MongoCollection<Document> PlayersCollection;
    private MongoCollection<Document> FarmsCollection;
    private MongoCollection<Document> AuthKeysCollection;

    public DatabaseManager (Config Config)
    {
        this.Config = Config;
    }

    public void Connect ()
    {
        ClusterSettings Cluster = ClusterSettings.builder ()
                .hosts (asList (new ServerAddress (Config.DatabaseHost, Config.DatabasePort)))
                .build ();
        MongoCredential Credential = MongoCredential.createCredential (Config.DatabaseUsername, Config.DatabaseName, Config.DatabasePassword.toCharArray ());
        MongoClientSettings Settings = MongoClientSettings.builder ()
                .clusterSettings (Cluster)
                .credentialList (asList (Credential))
                .build ();

        Client = MongoClients.create (Settings);
        Database = Client.getDatabase (Config.DatabaseName);

        PlayersCollection = Database.getCollection (Config.DatabasePlayersCollection);
        FarmsCollection = Database.getCollection (Config.DatabaseFarmsCollection);
        AuthKeysCollection = Database.getCollection (Config.DatabaseAuthKeysCollection);
    }

    public void Insert (Map<String, Object> Objects, DatabaseCollection Collection)
    {
        Document Doc = new Document (Objects);

        switch (Collection)
        {
            case PlayersCollection: PlayersCollection.insertOne (Doc, (Result, Throwable) -> { }); break;
            case FarmsCollection: FarmsCollection.insertOne (Doc, (Result, Throwable) -> { }); break;
        }
    }

    public void Get (UUID ID, DatabaseCollection Collection, SingleResultCallback<Document> Callback)
    {
        switch (Collection)
        {
            case PlayersCollection: PlayersCollection.find (eq ("ID", ID.toString ())).first (Callback); break;
            case FarmsCollection: FarmsCollection.find (eq ("ID", ID.toString ())).first (Callback); break;
        }
    }

    public void Exists (UUID ID, DatabaseCollection Collection, SingleResultCallback<Long> Callback)
    {
        switch (Collection)
        {
            case PlayersCollection: PlayersCollection.count (eq ("ID", ID.toString ()), Callback); break;
            case FarmsCollection: FarmsCollection.count (eq ("ID", ID.toString ()), Callback); break;
        }
    }

    public void Update (UUID ID, Map<String, Object> Objects, DatabaseCollection Collection)
    {
        SingleResultCallback<UpdateResult> UpdateObject = (UpdateResult, Throwable) -> { };

        switch (Collection)
        {
            case PlayersCollection: PlayersCollection.updateOne (eq ("ID", ID.toString ()), new Document ("$set", new Document (Objects)), UpdateObject); break;
            case FarmsCollection: FarmsCollection.updateOne (eq ("ID", ID.toString ()), new Document ("$set", new Document (Objects)), UpdateObject); break;
        }
    }

    public void Remove (UUID ID, DatabaseCollection Collection)
    {
        SingleResultCallback<DeleteResult> RemoveObject = (UpdateResult, Throwable) -> { };

        switch (Collection)
        {
            case PlayersCollection: PlayersCollection.deleteOne (eq ("ID", ID.toString ()), RemoveObject); break;
            case FarmsCollection: FarmsCollection.deleteOne (eq ("ID", ID.toString ()), RemoveObject); break;
        }
    }

}
