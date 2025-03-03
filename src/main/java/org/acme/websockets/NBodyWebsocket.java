package org.acme.websockets;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import org.acme.model.Body;
import org.acme.repository.BodyRepository;
import org.acme.service.NBodyService;
import com.google.gson.Gson;

@ServerEndpoint("/ws")
@ApplicationScoped
public class NBodyWebsocket {

    @Inject NBodyService nBodyService;
    @Inject BodyRepository bodyRepository;

    private Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        sessions.put(session.getId(), session);
        session.getAsyncRemote().sendText("Connexion WebSocket établie !");
        System.out.println("Nouvelle connexion WebSocket : " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        if( message.equals("[getBodies]")) {
            var data = nBodyService.computeNextStep();
            Gson gson = new Gson();
            String json = gson.toJson(data);
            session.getAsyncRemote().sendText(json);
        } else if(message.startsWith("[setSpeed]")) {
            try {
                String speedStr = message.substring("[setSpeed]".length()).trim();
                int speed = Integer.parseInt(speedStr);
                bodyRepository.setSpeed(1.0/speed);
                session.getAsyncRemote().sendText("Speed set to " + speed);
            } catch (NumberFormatException e) {
                session.getAsyncRemote().sendText("Invalid speed value: " + message);
            }
        } else if(message.startsWith("[addBody]")) {
            try {
                String[] parts = message.substring("[addBody]".length()).trim().split(",");
                if (parts.length != 5) {
                    session.getAsyncRemote().sendText("Invalid number of arguments for addBody");
                    return;
                }

                double x = Double.parseDouble(parts[0].trim());
                double y = 250 - (Double.parseDouble(parts[1].trim()));
                double mass = Double.parseDouble(parts[2].trim());
                double velocityX = Double.parseDouble(parts[3].trim());
                double velocityY = Double.parseDouble(parts[4].trim());

                Body body = new Body(mass, x, y, velocityX, velocityY);
                bodyRepository.addBody(body);
                session.getAsyncRemote().sendText("Body added successfully");
            } catch (NumberFormatException e) {
                session.getAsyncRemote().sendText("Invalid argument format for addBody: " + message);
            }
        } else if(message.startsWith("[reset]")) {
            bodyRepository.clear();
            session.getAsyncRemote().sendText("Bodies reset");
        } else {
            System.out.println("Message reçu : " + message);
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session.getId());
        System.out.println("Connexion fermée : " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Erreur WebSocket : " + throwable.getMessage());
    }
}
