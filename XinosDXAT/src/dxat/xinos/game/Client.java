package dxat.xinos.game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class Client implements Runnable{
	Socket socket;
	String ip;
	int port;
	boolean DISCONNECT = false;
	String ID;
	Random r = new Random();
	
	protected Client(String i, int p, String id){
		ip = i;
		port = p;
		ID=id;
	}
	
	protected void connect(){
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

	void sendMessage(String message) {
		DataOutputStream petition;
		if(socket!=null){
			try {
				System.out.println("[PLAYERCLIENT "+ID+"]-> Client sending msg: "+message);
				petition = new DataOutputStream(socket.getOutputStream());
				petition.writeBytes(message+"\n");
				petition.flush();
				System.out.println("[PLAYERCLIENT "+ID+"]-> Client has sent the message: "+message);
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	protected void disconnect(){
		System.out.println("[PLAYERCLIENT "+ID+"]-> Closing client socket...");
		System.out.println("[PLAYERCLIENT "+ID+"]-> Closing client socket...");
		try {
			socket.close();
			System.out.println("[PLAYERCLIENT "+ID+"]-> Client socket closed!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	int getBet(int totalcoin){
		return r.nextInt(totalcoin+1);
	}

	@Override
	public void run() {
		BufferedReader serverresponse;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		connect();
		sendMessage(("PLAY_"+ID).toUpperCase());
		while(true){
			if(DISCONNECT){
				disconnect();
				break;
			}
			try {
				serverresponse = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String sr = serverresponse.readLine();
				System.out.println("[PLAYERCLIENT "+ID+"]-> "+sr);
				
				if(sr.toUpperCase().equals("YOUR_BET")){
					System.out.println("[PLAYERCLIENT "+ID+"]-> Your bet: ");
					String s1 = br.readLine();
					System.out.println("[PLAYERCLIENT "+ID+"]-> Your ammount: ");
					String s2 = br.readLine();
					sendMessage("MYBET_"+s2+"_"+s1);
				}
				
				else if(sr.toUpperCase().equals("YOUWIN")||sr.toUpperCase().equals("YOULOSE")){
					DISCONNECT=true;
				}
				
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
