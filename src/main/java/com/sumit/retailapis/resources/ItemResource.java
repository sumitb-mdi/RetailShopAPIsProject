package com.sumit.retailapis.resources;

import com.codahale.metrics.annotation.Timed;
import com.sumit.retailapis.core.Item;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

/**
 * Created by sumitb_mdi on 10/31/15.
 */

@Path("/item")
@Produces(MediaType.APPLICATION_JSON)
@Api(
        value = "/item",
        description = "REST service for \"item\" collection for \"retail_db\""
)
public class ItemResource {
    private static final int LIMIT_OF_NEAREST_ITEMS = 10;
    private static final int LIMIT_OF_BEST_DEAL_ITEMS = 3;

    private final Datastore datastore;

    public ItemResource(Datastore datastore) {
        this.datastore = datastore;
    }


    @Path("/nearest")
    @Timed
    @GET
    @ApiOperation(
            value = "Get the nearest items",
            notes = "Given the value of Latitude and Longitude" +
                    "this API returns the closest Items available."
    )
    public List<Item> getNearestItems(@QueryParam("latitude") float latitude, @QueryParam("longitude") float longitude) {
        return this.datastore
                .createQuery(Item.class)
                .field("locality")
                .near(latitude, longitude)
                .limit(LIMIT_OF_NEAREST_ITEMS)
                .asList();
    }




    @Path("/best_deals")
    @Timed
    @GET
    @ApiOperation(
            value = "Get best deals.",
            notes = "Returns the top items whose difference between the " +
                    "origninal_price and price is maximum"
    )
    public List<Item> getBestDeals() {
        return this.datastore
                .createQuery(Item.class)
                .order("-difference")
                .limit(LIMIT_OF_BEST_DEAL_ITEMS)
                .asList();
    }




    @Timed
    @PUT
    @ApiOperation("Update an item. (Provide the ObjectId.")
    public String updateItem(Item item) {
        if (item.getId() == null) {
            return "Please provide the _id of the Document to update.";
        }
        Item existingItem = this.datastore.find(Item.class).field(Mapper.ID_KEY).equal(new ObjectId(item.getId())).get();
        if (existingItem != null && existingItem.getId().equals(item.getId())) {
            Query<Item> updateQuery = this.datastore.createQuery(Item.class).field(Mapper.ID_KEY).equal(new ObjectId(item
                    .getId()));
            UpdateOperations<Item> updateOperations = this.datastore.createUpdateOperations(Item.class);
            updateOperations.set("title", item.getTitle());
            updateOperations.set("price", item.getPrice());
            updateOperations.set("purchaseYear", item.getPurchaseYear());
            updateOperations.set("originalPrice", item.getOriginalPrice());
            updateOperations.set("locality", item.getLocality());
            updateOperations.set("picsLink", item.getPicsLink());
            updateOperations.set("difference", item.getOriginalPrice() - item.getPrice());
            updateOperations.set("lastUpdatedAt", new Date());
            this.datastore.update(updateQuery, updateOperations);

            return "Item updated in the DB.";
        }

        return "The given _id not found in db.";
    }



    @Timed
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(
            value = "createItem Operation.",
            notes = "Receives an item in JSON format and saves it into the " +
                    "persistence (MongoDB in this case). On Success returns the " +
                    "String Key of the newly created document."

    )
    public String createItem(Item item) {
//        if (item.getId() != null) {
//            return "Cannot accept the auto generated field _id";
//        }
        Key key = this.datastore.save(item);
        return key.getId().toString();
    }


    /*
    Sample data for Post request:
    {
        "title": "Item1",
        "price": 120,
        "purchase_year": 2010,
        "original_price": 150,
        "locality": [20.1231, 34.188],
        "pics_link": ["http://sample_s3_bucket_link_1", "http://sample_s3_bucket_link_2"]
    }
     */


}
