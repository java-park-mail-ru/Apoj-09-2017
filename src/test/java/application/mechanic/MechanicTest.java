package application.mechanic;

import application.mechanic.internal.ClientSnapService;
import application.mechanic.internal.GameSessionService;
import application.mechanic.music.Music;
import application.mechanic.snapshots.ClientSnap;
import application.services.AccountService;
import application.utils.requests.SignupRequest;
import application.websocket.RemotePointService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Base64;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@SuppressWarnings("MissortedModifiers")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringJUnit4ClassRunner.class)
public class MechanicTest {
    @MockBean
    private RemotePointService remotePointService;
    @MockBean
    private MechanicsExecutor mechanicsExecutor;
    @Autowired
    private GameMechanics gameMechanics;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientSnapService snapService;
    @Autowired
    private GameSessionService gameSessionService;
    @Autowired
    private Music music;
    private Long user1;
    private Long user2;


    @Before
    public void setUp() {
        gameMechanics.reset();
        accountService.clear();
        when(remotePointService.isConnected(any())).thenReturn(true);
        user1 = accountService.addUser(new SignupRequest("user1", "qwerty123", "user1@mail.ru"));
        user2 = accountService.addUser(new SignupRequest("user2", "qwerty123", "user2@mail.ru"));
    }

    @Test
    public void startMultiGame() {
        gameMechanics.addUser(user1, Config.MULTI_MODE);
        gameMechanics.addUser(user2, Config.MULTI_MODE);
        gameMechanics.gmStep();
        final Set<MultiGameSession> gameSession = gameSessionService.getMultiSessions();
        Assert.assertFalse(gameSession.isEmpty());
    }

    @Test
    public void startSingleGame() {
        gameMechanics.addUser(user1, Config.SINGLE_MODE);
        gameMechanics.gmStep();
        final Set<SingleGameSession> gameSession = gameSessionService.getSingleSessions();
        Assert.assertFalse(gameSession.isEmpty());
    }

    @Test
    public void singleGame() {
        gameMechanics.addUser(user1, Config.SINGLE_MODE);
        gameMechanics.gmStep();
        final SingleGameSession gameSession = gameSessionService.getSingleSessions().iterator().next();
        Assert.assertEquals(gameSession.getStatus(), Config.STEP_1);
        Assert.assertFalse(gameSession.getResult());
        final String songName = gameSession.getSongName();
        final String data = "aaaaaaaaaaaaaaaaaaaaaa" + Base64.getEncoder().encodeToString(music.getSong(songName));
        snapService.pushClientSnap(user1, new ClientSnap(Config.STEP_1, data));
        snapService.processSnapshotsFor(gameSession);
        Assert.assertEquals(gameSession.getStatus(), Config.STEP_2);
        Assert.assertFalse(gameSession.getResult());
        snapService.pushClientSnap(user1, new ClientSnap(Config.STEP_2, songName));
        snapService.processSnapshotsFor(gameSession);
        Assert.assertEquals(gameSession.getStatus(), Config.FINAL_STEP);
        Assert.assertTrue(gameSession.getResult());
    }

    @Test
    public void multiGame() {
        gameMechanics.addUser(user1, Config.MULTI_MODE);
        gameMechanics.addUser(user2, Config.MULTI_MODE);
        gameMechanics.gmStep();
        final MultiGameSession gameSession = gameSessionService.getMultiSessions().iterator().next();
        Assert.assertEquals(gameSession.getStatus(), Config.STEP_1);
        Assert.assertFalse(gameSession.getResult());
        final String songName = gameSession.getSongName();
        final String data = "aaaaaaaaaaaaaaaaaaaaaa" + Base64.getEncoder().encodeToString(music.getSong(songName));
        snapService.pushClientSnap(user1, new ClientSnap(Config.STEP_1, data));
        snapService.processSnapshotsFor(gameSession);
        Assert.assertEquals(gameSession.getStatus(), Config.STEP_1_5);
        Assert.assertFalse(gameSession.getResult());
        snapService.pushClientSnap(user2, new ClientSnap(Config.STEP_1_5, data));
        snapService.processSnapshotsFor(gameSession);
        Assert.assertEquals(gameSession.getStatus(), Config.STEP_2);
        Assert.assertFalse(gameSession.getResult());
        snapService.pushClientSnap(user2, new ClientSnap(Config.STEP_2, songName));
        snapService.processSnapshotsFor(gameSession);
        Assert.assertEquals(gameSession.getStatus(), Config.FINAL_STEP);
        Assert.assertTrue(gameSession.getResult());
    }

}
