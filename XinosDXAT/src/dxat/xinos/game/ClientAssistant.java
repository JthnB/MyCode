package dxat.xinos.game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientAssistant implements Runnable{

	Socket socket;
	String incommingsentence;
	String responsemsg="";
	
	public ClientAssistant(Socket s){
		socket=s;
	}
	
	@Override
	public void run() {
		BufferedReader br;
		DataOutputStream response;
		while(true){
			try {
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				incommingsentence=br.readLine();
				response = new DataOutputStream(socket.getOutputStream());
				response.writeBytes(responsemsg);
				System.out.println("-> ACK sent\n");
				System.out.println(incommingsentence);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
