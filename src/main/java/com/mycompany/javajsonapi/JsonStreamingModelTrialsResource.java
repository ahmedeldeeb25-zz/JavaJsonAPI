/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.javajsonapi;

import java.io.Reader;
import java.io.StringWriter;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * REST Web Service
 *
 * @author ash
 */
@Path("/jsonstreamingtrials")
@RequestScoped
public class JsonStreamingModelTrialsResource {
    
    @PersistenceContext(unitName = "com.mycompany_JavaJsonAPI_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    
    @POST
    @Path("object")
    @Consumes("application/json")
    public void tryJsonObject(Reader reader) {
        JsonParser parser = Json.createParser(reader);
        while (parser.hasNext()) {
            JsonParser.Event event = parser.next();
            switch (event) {
                case START_ARRAY:
                    System.out.print("[");
                    break;
                case END_ARRAY:
                    System.out.print("]");
                    break;
                case START_OBJECT:
                    System.out.print("{");
                    break;
                case END_OBJECT:
                    System.out.print("}");
                    break;
                case VALUE_FALSE:
                    System.out.print("false");
                    break;
                case VALUE_NULL:
                    System.out.print("null");
                    break;
                case VALUE_TRUE:
                    System.out.print("true");
                    break;
                case KEY_NAME:
                    System.out.print(parser.getString());
                    break;
                case VALUE_STRING:
                    System.out.print(parser.getString());
                    break;
                case VALUE_NUMBER:
                    System.out.print(parser.getString());
                    break;
            }
        }
    }
    
   
    @GET
    @Path("{key}")
    @Produces("application/json")
    public String getStaff(@PathParam("key") int key) {
        Staff staff = em.find(Staff.class, key);
        
        StringWriter sw = new StringWriter();
        JsonGenerator gen = Json.createGenerator(sw);
        gen.writeStartObject()
                .write("name", staff.getName())
                .write("staffid", staff.getStaffId())
                .write("gender", staff.getGender())
                .write("salary", staff.getSalary())
        .writeEnd();
        gen.close();

        return sw.toString();
    }
}
