package application.mechanic;

public class Config {
    public static final String SINGLE_MODE = "singleplayer";
    public static final String MULTI_MODE = "multiplayer";
    public static final String SINGER_ROLE = "singer";
    public static final String LISTENER_ROLE = "listener";
    public static final String LEAVE = "leave";

    public enum Step { PRE_GAME_DATA, RECORDING, SECOND_RECORDING, LISTENING, RESULT }
}
