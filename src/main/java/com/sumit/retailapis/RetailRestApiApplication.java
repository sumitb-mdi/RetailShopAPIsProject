package com.sumit.retailapis;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.sumit.retailapis.resources.ItemResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 * Created by sumitb_mdi on 10/31/15.
 */
public class RetailRestApiApplication extends Application<RetailRestApiConfiguration> {

    public static void main(String[] args) throws Exception {
        new RetailRestApiApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<RetailRestApiConfiguration> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle<RetailRestApiConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(RetailRestApiConfiguration configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });
    }

    @Override
    public void run(RetailRestApiConfiguration retailRestApiConfiguration, Environment environment) throws Exception {
        final String dbUri = String.format(retailRestApiConfiguration.getDbServerConnectionStringTemplate(),
                retailRestApiConfiguration.getDbUsername(), retailRestApiConfiguration.getDbPassword(),
                retailRestApiConfiguration.getDatabaseName());

        final MongoClientURI mongoClientURI = new MongoClientURI(dbUri);
        final MongoClient mongoClient = new MongoClient(mongoClientURI);

//        final MongoClient mongoClient =  new MongoClient(retailRestApiConfiguration.getMongoServer());

        final Morphia morphia = new Morphia();
        morphia.mapPackage("com.sumit.retailapis.core");

        final Datastore datastore = morphia.createDatastore(mongoClient, retailRestApiConfiguration.getDatabaseName());
        datastore.ensureIndexes();  //ensure indexes for "locality" field. (Mentioned as index in entity class)

        environment.jersey().register(new ItemResource(datastore));
    }
}
