package edu.uclm.esi.tfg.comunicacion;

import javax.servlet.http.*;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class HttpSessionConfigurator extends ServerEndpointConfig.Configurator{

	@Override
    public void modifyHandshake(ServerEndpointConfig config, 
              HandshakeRequest request, HandshakeResponse response) {
      
        HttpSession httpSession = (HttpSession)request.getHttpSession();
        config.getUserProperties().put(HttpSession.class.getName(),httpSession);
    }
}

