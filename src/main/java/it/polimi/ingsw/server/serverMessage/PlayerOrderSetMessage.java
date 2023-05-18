package it.polimi.ingsw.server.serverMessage;

import java.io.IOException;
import java.util.List;

public class PlayerOrderSetMessage implements ServerMessage{

    List<String> playerOrder;

    public PlayerOrderSetMessage(List<String> playerOrder) {
        this.playerOrder = playerOrder;
    }

    public List<String> getPlayerOrder() {
        return playerOrder;
    }

    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public void accept(ServerMessageHandler serverMessageHandler)  {
        serverMessageHandler.handle(this);
    }
}
