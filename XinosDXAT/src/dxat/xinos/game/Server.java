package dxat.xinos.game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{

	String incommingsentence="";
	String ackmsg="OK";
	ServerSocket welcomesocket;
	
	public Server(int listenningport){
		try {
			welcomesocket = new ServerSocket(listenningport);
		} 
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(true){
			try {
				System.out.println("-> Server connected and listenning\n");
				ClientAssistant clientassistant = new ClientAssistant(welcomesocket.accept());
				Thread t = new Thread(clientassistant,"AssistantThread");
				t.start();
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
