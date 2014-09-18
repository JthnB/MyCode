package dxat.xinos.game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientAssistant implements Runnable{

	Socket socket;
	String incommingsentence;
	public String responsemsg="ACK";
	Game game;
	int playerid;
	
	public ClientAssistant(Socket s, Game g){
		socket=s;
		game=g;
	}
	
	@Override
	public void run() {
		BufferedReader br;
		String firstarg = "",secondarg="";
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			DataOutputStream response = new DataOutputStream(socket.getOutputStream());
			while(true){
				incommingsentence=br.readLine();
				splitMessage(incommingsentence,firstarg,secondarg);
				if(firstarg.toUpperCase().equals("PLAY")&&game.listplayers.size()==0){
					response.writeBytes("WAIT");
					game.listplayers.add(secondarg);
				}
				else if(firstarg.toUpperCase().equals("PLAY")&&game.listplayers.size()>0&&checkExistingPlayer(secondarg)){
					response.writeBytes("YOUR_BET");
				}
				else
					response.writeBytes("CANNOT_UNDERSTAND_YOUR_MESSAGE");
				System.out.println("[SERVER]-> ACK sent");
				System.out.println("[SERVER]->"+incommingsentence);
			}
		} 
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		};
	}
	
	void splitMessage(String message, String fa, String sa){
		String[] msgsplitted = message.split("_");
		
		fa= msgsplitted[0];
		sa=msgsplitted[1];
	}
	
	boolean checkExistingPlayer(String id){
		for(String s : game.listplayers){
			if(id.toUpperCase().equals(s))
				return true;
		}
		
		return false;
	}
}
