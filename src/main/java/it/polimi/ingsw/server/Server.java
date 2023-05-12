package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.server.serverMessage.CustomMessage;
import it.polimi.ingsw.server.serverMessage.ErrorMessage;
import it.polimi.ingsw.server.serverMessage.StartGameMessage;
import it.polimi.ingsw.view.cli.CLIConstants;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class represents the server of the game.
 */
public class Server {

    /**
     * The socket server.
     */
    private final SocketServer socketServer;
    /**
     * The list of the clients waiting to play.
     */
    private final List<SocketClientConnection> waiting;
    /**
     * The map of the clients' id and their virtual client.
     */
    private final Map<Integer, VirtualClient> ClientIdMapVirtualClient;
    /**
     * The map of the clients' id and their username.
     */
    private final Map<Integer, String> ClientIdMapUsername;
    /**
     * The map of the clients' username and their id.
     */
    private final Map<String, Integer> UsernameMapClientID;
    /**
     * The map of the virtual clients and their socket client connection.
     */
    private final Map<VirtualClient, SocketClientConnection> VirtualClientMapSocketClientConnection;
    /**
     * The next client id.
     */
    private Integer nextClientId;
    /**
     * The current game handler.
     */
    private GameHandler currentGameHandler;
    /**
     * The number of players.
     */
    private Integer numOfPlayers;
    /**
     * The port of the server.
     */
    private static final int PORT = 1235;

    /**
     * This is the constructor of the class.
     */
    public Server() {
        this.socketServer = new SocketServer(PORT, this);
        nextClientId = 1;
        ClientIdMapVirtualClient = new HashMap<>();
        ClientIdMapUsername = new HashMap<>();
        UsernameMapClientID = new HashMap<>();
        VirtualClientMapSocketClientConnection = new HashMap<>();
        waiting = new ArrayList<>();
    }

    /**
     * Get the socket server.
     *
     * @return the socket server
     */
    public synchronized SocketServer getSocketServer() {
        return socketServer;
    }

    /**
     * Get the game handler by client id.
     *
     * @param clientId is the client id
     * @return the game handler
     */
    public GameHandler getGameHandlerByClientId(int clientId) {
        return ClientIdMapVirtualClient.get(clientId).getGameHandler();
    }

    /**
     * Get the virtual client by client id.
     *
     * @param clientId is the client id
     * @return the virtual client
     */
    public VirtualClient getVirtualClientByClientId(int clientId) {
        return ClientIdMapVirtualClient.get(clientId);
    }

    /**
     * Get the username by client id.
     *
     * @param clientId is the client id
     * @return the username
     */
    public String getUsernameByClientId(Integer clientId) {
        return ClientIdMapUsername.get(clientId);
    }

    /**
     * Get the client id by username.
     *
     * @param username is the username
     * @return the client id
     */
    public Integer getClientIdByUsername(String username) {
        return UsernameMapClientID.get(username);
    }

    /**
     * Set the number of players.
     *
     * @param numOfPlayers is the number of players
     */
    public void setNumOfPlayers(Integer numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    /**
     * Assign the client id.
     *
     * @return the client id
     */
    public Integer assignClientId() {
        Integer clientId = nextClientId;
        nextClientId++;
        return clientId;
    }

    /**
     * This method register the client on the server based on the username.
     *
     * @param username is the username of the client
     * @param socketClientConnection is the socketClientConnection of the client
     * @return the clientId of the client
     */
    public synchronized Integer registerConnection(String username, SocketClientConnection socketClientConnection) {
        if (waiting.isEmpty()) {
            currentGameHandler = new GameHandler();
        }
        if (username == null || username.trim().isEmpty()) {
            socketClientConnection.send(new ErrorMessage(GameCreationErrors.ILLEGAL_USERNAME));
            return null;
        }
        if (currentGameHandler.getPlayersUsername().stream().anyMatch(username::equalsIgnoreCase)) {
            socketClientConnection.send(new ErrorMessage(GameCreationErrors.DUPLICATE_USERNAME));
            return null;
        }

        Integer clientId = assignClientId();
        VirtualClient virtualClient = new VirtualClient(socketClientConnection, username, clientId, currentGameHandler);

        currentGameHandler.addVirtualClient(virtualClient);
        currentGameHandler.addPlayer(username, clientId);
        ClientIdMapVirtualClient.put(clientId, virtualClient);
        UsernameMapClientID.put(username, clientId);
        ClientIdMapUsername.put(clientId, username);
        VirtualClientMapSocketClientConnection.put(virtualClient, socketClientConnection);
        System.out.println(virtualClient.getUsername() + " connected with clientId: " + virtualClient.getClientID());

        return clientId;
    }

    /**
     * This method manage the waiting list in order to start the game when the number of players is reached.
     *
     * @param socketClientConnection is the socketClientConnection of the client
     * @throws InterruptedException if the thread is interrupted
     */
    public synchronized void lobby(SocketClientConnection socketClientConnection) throws InterruptedException {

        waiting.add(socketClientConnection);
        if (waiting.size() == 1) {
            socketClientConnection.setUpNumOfPlayers();
        } else if (waiting.size() == numOfPlayers) {
            currentGameHandler.sendAll(new CustomMessage("The selected number of players has been reached. The game is starting..."));
            currentGameHandler.startGame();
            currentGameHandler.sendAll( new StartGameMessage());
            waiting.clear();

        } else {
            currentGameHandler.sendAll(new CustomMessage("Waiting for other players to join..." + CLIConstants.CYAN_BRIGHT + (numOfPlayers - waiting.size()) + CLIConstants.RESET + " players left"));
        }
    }

    /**
     * This method unregister the client on the server.
     *
     * @param clientID is the clientID of the client
     */
    public synchronized void unregisterClient(int clientID) {
        VirtualClient client = ClientIdMapVirtualClient.get(clientID);
        System.out.printf("Unregistering client %s with client id: %d...", client.getUsername(), client.getClientID());
        ClientIdMapVirtualClient.remove(clientID);
        UsernameMapClientID.remove(client.getUsername());
        waiting.remove(VirtualClientMapSocketClientConnection.get(client));
        ClientIdMapUsername.remove(client.getClientID());
        VirtualClientMapSocketClientConnection.remove(client);
        System.out.println("Client has been successfully unregistered.");
    }

    /**
     * This method is used to start the server.
     *
     * @param args are the arguments of the main method
     */
    public static void main(String[] args) {
        Server server = new Server();
        System.out.println(CLIConstants.GREEN_BRIGHT + "Server started" + CLIConstants.RESET);
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(server.socketServer);
    }
}
