package net.mistwood.FarmingPlugin.Database;

import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.connection.ClusterSettings;

import org.bson.Document;

import net.mistwood.FarmingPlugin.Config;

import java.util.Map;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;
import static java.util.Arrays.asList;

public class DatabaseManager {

    private final Config config;

    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> playersCollection;
    private MongoCollection<Document> farmsCollection;
    private MongoCollection<Document> authKeysCollection;

    public DatabaseManager(Config config) {
        this.config = config;
    }

    public void connect() {
        ClusterSettings cluster = ClusterSettings.builder()
                .hosts(asList(new ServerAddress(config.databaseConfig.host, config.databaseConfig.port)))
                .build();
        MongoCredential credential = MongoCredential.createCredential(config.databaseConfig.username, config.databaseConfig.name, config.databaseConfig.password.toCharArray());
        MongoClientSettings settings = MongoClientSettings.builder()
                .clusterSettings(cluster)
                .credentialList(asList(credential))
                .build();

        this.client = MongoClients.create(settings);
        this.database = client.getDatabase(config.databaseConfig.name);

        this.playersCollection = database.getCollection(config.databaseConfig.playersCollection);
        this.farmsCollection = database.getCollection(config.databaseConfig.farmsCollection);
        this.authKeysCollection = database.getCollection(config.databaseConfig.authKeysCollection);
    }

    public void insert(Map<String, Object> objects, DatabaseCollection collection) {
        Document document = new Document(objects);

        switch (collection) {
            case PLAYERS:
                playersCollection.insertOne(document, (Result, Throwable) -> {
                });
                break;
            case FARMS:
                farmsCollection.insertOne(document, (Result, Throwable) -> {
                });
                break;
        }
    }

    public void get(UUID id, DatabaseCollection collection, SingleResultCallback<Document> callback) {
        switch (collection) {
            case PLAYERS:
                playersCollection.find(eq("ID", id.toString())).first(callback);
                break;
            case FARMS:
                farmsCollection.find(eq("ID", id.toString())).first(callback);
                break;
        }
    }

    public void exists(UUID id, DatabaseCollection collection, SingleResultCallback<Long> callback) {
        switch (collection) {
            case PLAYERS:
                playersCollection.count(eq("ID", id.toString()), callback);
                break;
            case FARMS:
                farmsCollection.count(eq("ID", id.toString()), callback);
                break;
        }
    }

    public void update(UUID id, Map<String, Object> objects, DatabaseCollection collection) {
        SingleResultCallback<UpdateResult> callback = (result, t) -> { };

        switch (collection) {
            case PLAYERS:
                playersCollection.updateOne(eq("ID", id.toString()), new Document("$set", new Document(objects)), callback);
                break;
            case FARMS:
                farmsCollection.updateOne(eq("ID", id.toString()), new Document("$set", new Document(objects)), callback);
                break;
        }
    }

    public void remove(UUID id, DatabaseCollection collection) {
        SingleResultCallback<DeleteResult> callback = (result, t) -> { };

        switch (collection) {
            case PLAYERS:
                playersCollection.deleteOne(eq("ID", id.toString()), callback);
                break;
            case FARMS:
                farmsCollection.deleteOne(eq("ID", id.toString()), callback);
                break;
        }
    }

}
