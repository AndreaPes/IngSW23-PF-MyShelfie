package it.polimi.ingsw.server.serverMessage;

public class PointsUpdateMessage implements ServerMessage {

    private final String message;
    private final String username;
    private final int points;

    public PointsUpdateMessage(String message, String username, int points) {
        this.message = message;
        this.username = username;
        this.points = points;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public void accept(ServerMessageHandler serverMessageHandler) {
        serverMessageHandler.handle(this);
    }
}
