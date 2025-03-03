package org.acme.websockets;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.websocket.*;
import org.junit.jupiter.api.*;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class NBodyWebsocketTest {

    private Session session;

    @BeforeEach
    void setup() throws Exception {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        // Use port 8081 since Quarkus runs on this port in test mode
        session = container.connectToServer(TestClient.class, URI.create("ws://localhost:8081/ws"));
    }


    @Test
    void testWebSocketReceivesBodies() throws Exception {
        session.getBasicRemote().sendText("[getBodies]");
        Thread.sleep(1000); // Wait for response

        assertTrue(TestClient.receivedMessage.contains("x"), "Response should contain body position data");
    }

    @AfterEach
    void tearDown() throws Exception {
        if (session != null) session.close();
    }

    @ClientEndpoint
    public static class TestClient {
        public static String receivedMessage = "";

        @OnMessage
        public void onMessage(String message) {
            receivedMessage = message;
        }
    }
}
