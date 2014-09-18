package dxat.xinos.game;

import java.net.Socket;

public class Player {
	Client client;
	int score;
	String ID;
	
	public Player(Client c, String i){
		client=c;
		ID=i;
	}
	
	void connectToServer(){
		client.connect();
		System.out.print("[PLAYER]> Received: "+client.sendMessage("PLAY"+ID)+"\n");
	}
}
