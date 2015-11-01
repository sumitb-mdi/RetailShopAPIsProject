package com.sumit.retailapis;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

/**
 * Created by sumitb_mdi on 10/31/15.
 */
public class RetailRestApiConfiguration extends Configuration {

    private String mongoServer;
    private String dbUsername;
    private String dbPassword;
    private String databaseName;
    private String dbServerConnectionStringTemplate;

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;


    @JsonProperty
    public String getMongoServer() {
        return mongoServer;
    }

    @JsonProperty
    public void setMongoServer(String mongoServer) {
        this.mongoServer = mongoServer;
    }

    @JsonProperty
    public String getDatabaseName() {
        return databaseName;
    }

    @JsonProperty
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDbServerConnectionStringTemplate() {
        return dbServerConnectionStringTemplate;
    }

    public void setDbServerConnectionStringTemplate(String dbServerConnectionStringTemplate) {
        this.dbServerConnectionStringTemplate = dbServerConnectionStringTemplate;
    }
}
