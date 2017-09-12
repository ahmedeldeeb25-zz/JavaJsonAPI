/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.javajsonapi.websocket.eg;

import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author ash
 */
@ServerEndpoint("/groupchatjsonendpoint")
public class GroupChatJsonEndpoint {

    @OnMessage
    public void onMessage(Session session, String message) {

        StringReader reader = new StringReader((message));
        JsonReader jsonReader = Json.createReader(reader);
        JsonObject object = jsonReader.readObject();

        switch (object.getString("type")) {
            case "login":
                String name = object.getString("name");
                session.getUserProperties().put("name", name);
                sendMessage(session, message);
                break;

            case "chat":
                sendMessage(session, message);
                break;

            case "logout":
                sendMessage(session, message);
                try {
                    session.close();
                } catch (IOException ex) {
                    Logger.getLogger(GroupChatJsonEndpoint.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;

            case "private":
                String to = object.getString("to");
                for(Session s : session.getOpenSessions()){
                    if(s.isOpen()){
                        String nameInSession = s.getUserProperties().get("name").toString();
                        if(nameInSession.equals(to)){
                            try {
                                s.getBasicRemote().sendText(message);
                            } catch (IOException ex) {
                                Logger.getLogger(GroupChatJsonEndpoint.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
                break;

            default:
                throw new AssertionError();
        }

    }

    private void sendMessage(Session session, String message) {
        for (Session s : session.getOpenSessions()) {
            try {
                if (s.isOpen()) {
                    s.getBasicRemote().sendText(message);
                }
            } catch (IOException ex) {
                Logger.getLogger(GroupChatJsonEndpoint.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
