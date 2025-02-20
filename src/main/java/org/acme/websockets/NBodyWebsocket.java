package org.acme.websockets;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.acme.service.NBodyService;
import com.google.gson.Gson;

@ServerEndpoint("/ws")
@ApplicationScoped
public class NBodyWebsocket {

    @Inject
    NBodyService nBodyService;

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
