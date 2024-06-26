package unibo.springIntro23;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;


/*
For Spring application to forward client requests to the endpoint,
we need to register the handler.
Class WebSocketConfig is a customized configuration class that implements
interface WebSocketConfigurer.
WebSocketConfigurer interface defines callback methods to configure the
WebSocket request handling (example: adding WebSocket handler)
via @EnableWebSocket annotation.
 */

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    public static final WebSocketHandler wshandler = new WebSocketHandler();

/*
    public WebSocketConfiguration(){
        new Thread(){ //To test ws-connection
            public void run(){
                wshandler.sendToAll("WebSocketConfiguration created" );
            }
        }.start();
    }
*/
    //Necessario per l'invio di immagini
    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxBinaryMessageBufferSize(1024000);
        return container;
    }


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(wshandler, "/socket").setAllowedOrigins("*");
    }
}
