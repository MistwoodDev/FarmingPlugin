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
import org.bson.Document;

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

    public DatabaseManager (Config Config)
    {
        this.Config = Config;
    }

    public void Connect ()
    {
        ClusterSettings Cluster = ClusterSettings.builder ().hosts (asList (new ServerAddress (Config.DatabaseHost, Config.DatabasePort))).build ();
        MongoCredential Credential = MongoCredential.createMongoCRCredential (Config.DatabaseUsername, Config.DatabaseName, Config.DatabasePassword.toCharArray ());
        MongoClientSettings Settings = MongoClientSettings.builder ().clusterSettings (Cluster).credentialList (asList (Credential)).build ();

        Client = MongoClients.create (Settings);
        Database = Client.getDatabase (Config.DatabaseName);

        PlayersCollection = Database.getCollection (Config.DatabasePlayersCollection);
        PlayersCollection = Database.getCollection (Config.DatabaseFarmsCollection);
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

    public Map<String, Object> Get (UUID ID, DatabaseCollection Collection)
    {
        final Document Result = new Document ();

        SingleResultCallback<Document> ReturnDocumentMap = (Doc, Throwable) -> Result.putAll (Doc);

        switch (Collection)
        {
            case PlayersCollection: PlayersCollection.find (eq ("ID", ID.toString ())).first (ReturnDocumentMap); break;
            case FarmsCollection: FarmsCollection.find (eq ("ID", ID.toString ())).first (ReturnDocumentMap); break;
        }

        return Result;
    }

    public boolean Exists (UUID ID, DatabaseCollection Collection)
    {
        final boolean[] DoesExist = new boolean[1];

        SingleResultCallback<Long> CheckExist = (Count, Throwable) -> DoesExist[0] = (Count > 0);

        switch (Collection)
        {
            case PlayersCollection: PlayersCollection.count (new Document ("ID", ID.toString ()), CheckExist);
            case FarmsCollection: FarmsCollection.count (new Document ("ID", ID.toString ()), CheckExist);
        }

        return DoesExist[0];
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
