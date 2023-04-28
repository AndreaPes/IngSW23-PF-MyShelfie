package it.polimi.ingsw.client;

import it.polimi.ingsw.client.clientMessage.ClientMessage;
import it.polimi.ingsw.model.gameState.events.BoardUpdateEvent;
import it.polimi.ingsw.model.gameState.events.CommonCardEvent;
import it.polimi.ingsw.server.serverMessage.*;

import java.io.*;
import java.net.Socket;

public class SocketListener implements Runnable{

    client client1;
    Socket socketServer;
    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;




    public SocketListener(client client1) {

        this.client1=client1;
        try {

            System.out.println("provo a connettermi al server");
            socketServer = new Socket("localhost",1235);
            System.out.println("client connesso al server");


            outputStream = new ObjectOutputStream(socketServer.getOutputStream());
            inputStream =new ObjectInputStream(socketServer.getInputStream());


        } catch (IOException ex) {
            System.err.println("server non è attivo");
        }



    }

    public synchronized void readFromStream() throws IOException, ClassNotFoundException {

        ServerMessage input = (ServerMessage) inputStream.readObject();
        System.out.println("--> nuovo messaggio dal server");
        if (input instanceof NumOfPlayerRequest){
           client1.handle((NumOfPlayerRequest) input);
        }
        else if (input instanceof CustomMessage){
            client1.handle((CustomMessage) input);
        }
        else if (input instanceof UsernameRequest){
            client1.handle((UsernameRequest) input);
        }
        else if (input instanceof BoardUpdateMessage){
            client1.handle((BoardUpdateMessage) input);
        }
        else if (input instanceof StartTurnMessage){
            client1.handle((StartTurnMessage) input);
        }
        else if (input instanceof MoveRequest){
            client1.handle((MoveRequest) input);
        }
        else if (input instanceof LibraryUpdateMessage){
            client1.handle((LibraryUpdateMessage) input);
        }
        else{
            client1.handle(input); //lo tratto come un ServerMessagge stampando il messaggio
        }

    }

    @Override
    public void run() {
        try{
            while(true){
                readFromStream();
            }

        }catch (IOException e){

        }
        catch (ClassNotFoundException e){

        }
    }

    /**
     * This method use socket to send a message to the server
     * @param clientMessage
     */
   public void send(ClientMessage clientMessage){
       try {
           outputStream.reset();
           outputStream.writeObject(clientMessage);
           outputStream.flush();

       } catch (IOException e) {
           System.out.println("invio non riuscito " + e.getMessage());
            e.printStackTrace();
       }
   }
}
