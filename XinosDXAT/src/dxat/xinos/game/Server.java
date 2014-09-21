package dxat.xinos.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that waits and manages new connections for the clients
 * @author Jonathan
 *
 */
public class Server implements Runnable{

	ServerSocket welcomesocket;												//Welcome socket
	List<ClientAssistant> clientlist = new ArrayList<ClientAssistant>();	//List of client assistants (connections)
	List<Thread> listthreads = new ArrayList<Thread>();						//List of threads of client assistant
	Game game;																//Pointer to the Game object
	Thread gamethread;														//Game thread
	
	public Server(int listenningport){
		try {
			welcomesocket = new ServerSocket(listenningport);
			game = new Game(this);
		} 
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			gamethread = new Thread(game,"GAMETHREAD");
			gamethread.start();
			while(true){
				
				System.out.println("[SERVER]-> Server connected and listenning\n");
				Socket s = welcomesocket.accept();
				
				/*
				 * When a new connection is accepted a new ClientAssistant is created and the server
				 * delegates the connection to it.
				 */
				ClientAssistant clientassistant = new ClientAssistant(s,game);
				clientlist.add(clientassistant);
				Thread t = new Thread(clientassistant,"AssistantThread");
				listthreads.add(t);
				t.start();
			}
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
