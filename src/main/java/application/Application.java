package application;

import application.db.UserDao;
import application.db.UserDaoImpl;
import application.websocket.GameSocketHandler;

import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        WebSocketPolicy.newServerPolicy().setMaxTextMessageSize(500 * 1024);
        WebSocketPolicy.newClientPolicy().setMaxBinaryMessageSize(500 * 1024);
        SpringApplication.run(new Object[]{WebSocketConfig.class, Application.class}, args);
    }

    @Bean
    public UserDao database() {
        return new UserDaoImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSocketHandler gameWebSocketHandler() {
        return new PerConnectionWebSocketHandler(GameSocketHandler.class);
    }

}
