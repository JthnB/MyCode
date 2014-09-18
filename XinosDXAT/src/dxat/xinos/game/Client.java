package dxat.xinos.game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	String serverresponse = "";
	Socket socket;
	String ip;
	int port;

	public Client(String i, int p) {
		ip = i;
		port = p;
	}
	
	public void connect(){
		if(socket!=null){
			try {
				socket = new Socket(ip,port);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	String sendMessage(String message) {
		BufferedReader serverresponse;
		DataOutputStream petition;
		if(socket!=null){
			try {
				System.out.println("[CLIENT]--> Client sending msg");
				petition = new DataOutputStream(socket.getOutputStream());
				petition.writeBytes(message+"\n");
				petition.flush();
				System.out.println("[CLIENT]--> Client has sent the message");
				serverresponse = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				return serverresponse.readLine();
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	public void disconnect(){
		System.out.println("[CLIENT]--> Closing client socket...");
		System.out.println("[CLIENT]--> Closing client socket...");
		try {
			socket.close();
			System.out.println("[CLIENT]-> Client socket closed!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
