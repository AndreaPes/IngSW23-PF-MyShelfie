package it.polimi.ingsw.model.gameState.Exceptions;

public class GameStartedException extends Exception {

    @Override
    public String getMessage() {
        return "la partità è già iniziata";
    }
}
