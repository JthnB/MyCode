package dxat.xinos.game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
		BufferedReader br;
		try {
			while(true){
				
				System.out.println("-> Server connected and listenning\n");
				Socket s = welcomesocket.accept();
				br = new BufferedReader(new InputStreamReader(s.getInputStream()));
				DataOutputStream response = new DataOutputStream(s.getOutputStream());
				
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
