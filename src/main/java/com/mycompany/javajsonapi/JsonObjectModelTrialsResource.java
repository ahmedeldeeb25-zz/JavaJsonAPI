/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.javajsonapi;

import java.io.Reader;
import java.io.StringWriter;
import java.util.List;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
 
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;

/**
 * REST Web Service
 *
 * @author ash
 */
@Path("/jsontrials")
@Stateless
public class JsonObjectModelTrialsResource {

    @PersistenceContext(unitName = "com.mycompany_JavaJsonAPI_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @POST
    @Path("object")
    @Consumes("application/json")
    public void tryJsonObject(Reader reader) {

        JsonReader jsonReader = Json.createReader(reader);
        JsonObject object = jsonReader.readObject();

        System.out.println(object.getString("name"));
        System.out.println(object.getInt("age"));

        JsonObject address = object.getJsonObject("address");
        System.out.println(address.getString("country"));
        System.out.println(address.getString("state"));
        System.out.println(address.getString("city"));
        System.out.println(address.getInt("zipcode"));
        jsonReader.close();
    }

    @POST
    @Path("array")
    @Consumes("application/json")
    public void tryJsonArray(Reader reader) {

        JsonReader jsonReader = Json.createReader(reader);
        JsonArray array = jsonReader.readArray();
        List<JsonNumber> percentageList = array.getValuesAs(JsonNumber.class);
        for (JsonNumber jsonNumber : percentageList) {
            System.out.println(jsonNumber.intValue());
        }
        jsonReader.close();
    }

   
    @GET
    @Path("{key}")
    @Produces("application/json")
    public String getStaff(@PathParam("key") int key) {
       
        Staff staff = em.find(Staff.class, key);
        JsonObject object = Json.createObjectBuilder()
                .add("staffid", staff.getStaffId())
                .add("name", staff.getName())
                .add("gender", staff.getGender())
                .add("salary", staff.getSalary())
                .build();
        StringWriter sw = new StringWriter();
        JsonWriter writer = Json.createWriter(sw);
        writer.writeObject(object);
        writer.close();
        return sw.toString();
        
    }
}
