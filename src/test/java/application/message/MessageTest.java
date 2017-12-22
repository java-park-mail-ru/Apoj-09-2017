package application.message;

import application.mechanic.requests.FinishGame;
import application.mechanic.requests.InitMultiGame;
import application.mechanic.requests.InitSingleGame;
import application.mechanic.requests.JoinGame;
import application.mechanic.snapshots.ClientSnap;
import application.mechanic.snapshots.ServerSnap;
import application.websocket.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class MessageTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String CLIENT_MESSAGE = '{' +
            "\"type\": \"LISTENING\"," +
            "\"data\": \"badtrip\"" +
            '}';
    private static final String SERVER_MESSAGE = '{' +
            "\"class\":\"ServerSnap\"," +
            "\"type\":\"LISTENING\"," +
            "\"data\":\"badtrip\"" +
            '}';
    private static final String FINISH_GAME_MESSAGE = '{' +
            "\"class\":\"FinishGame\"," +
            "\"type\":\"RESULT\"," +
            "\"result\":true," +
            "\"score\":1000" +
            '}';
    private static final String INIT_SINGLE_GAME_MESSAGE = '{' +
            "\"class\":\"InitSingleGame$Request\"," +
            "\"type\":\"RECORDING\"," +
            "\"data\":\"badtrip\"" +
            '}';
    private static final String INIT_MULTI_GAME_MESSAGE = '{' +
            "\"class\":\"InitMultiGame$Request\"," +
            "\"type\":\"PRE_GAME_DATA\"," +
            "\"role\":\"SINGER\"," +
            "\"secondUser\":\"nagibator228\"" +
            '}';

    @Test
    public void clientSnapTest() throws IOException {
        final ObjectNode node = objectMapper.readValue(CLIENT_MESSAGE, ObjectNode.class);
        final Message message = new ClientSnap(node.get("type").asText(), node.get("data").asText());
        Assert.assertNotNull(message);
    }

    @Test
    public void joinGameTest() throws IOException {
        final String clientSnapStr = "{ " +
                "\"mode\": \"MULTI\"" +
                '}';
        final ObjectNode node = objectMapper.readValue(clientSnapStr, ObjectNode.class);
        final Message message = new JoinGame.Request(node.get("mode").asText());
        Assert.assertNotNull(message);
    }

    @Test
    public void serverSnapTest() throws JsonProcessingException {
        final ServerSnap snap = new ServerSnap("LISTENING", "badtrip");
        Assert.assertEquals(SERVER_MESSAGE, objectMapper.writeValueAsString(snap));
    }

    @Test
    public void finishGameTest() throws JsonProcessingException {
        final FinishGame message = new FinishGame(true, 1000);
        Assert.assertEquals(FINISH_GAME_MESSAGE, objectMapper.writeValueAsString(message));
    }

    @Test
    public void initSingleGameTest() throws JsonProcessingException {
        final InitSingleGame.Request message = new InitSingleGame.Request("badtrip");
        Assert.assertEquals(INIT_SINGLE_GAME_MESSAGE, objectMapper.writeValueAsString(message));
    }

    @Test
    public void initMultiGameTest() throws JsonProcessingException {
        final InitMultiGame.Request message = new InitMultiGame.Request("SINGER", "nagibator228");
        Assert.assertEquals(INIT_MULTI_GAME_MESSAGE, objectMapper.writeValueAsString(message));
    }
}
