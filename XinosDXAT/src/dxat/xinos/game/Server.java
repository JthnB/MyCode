package dxat.xinos.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable{

	String incommingsentence="";
	String ackmsg="OK";
	ServerSocket welcomesocket;
	List<ClientAssistant> clientlist = new ArrayList<ClientAssistant>();
	List<Thread> listthreads = new ArrayList<Thread>();
	Game game;
	Thread gamethread;
	
	public Server(int listenningport){
		try {
			welcomesocket = new ServerSocket(listenningport);
			game = new Game();
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
