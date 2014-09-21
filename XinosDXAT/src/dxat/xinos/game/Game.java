package dxat.xinos.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Game implements Runnable{
	public boolean GAMESTARTED = false;
	protected List<Player> listplayers =  new ArrayList<Player>();
	protected HashMap<String,ClientAssistant> idtoclientassistant = new HashMap<String,ClientAssistant>();
	Server server;
	
	protected Game(Server s){
		server=s;
	}
	
	protected boolean checkExistingPlayer(String id){
		for(Player p : listplayers){
			if(p.ID.toUpperCase().equals(id))
				return true;
		}
		return false;
	}
	
	protected void sendMulticastMessage(List<Player> players, String message){
		for(Player p : listplayers){
			ClientAssistant ca = idtoclientassistant.get(p.ID);
			ca.sendMessage(message);
		}
	}
	
	protected Player findPlayerReg(String id){
		for(Player pr : listplayers){
			if(pr.ID.equals(id))
				return pr;
		}
		return null;
	}

	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(checkAllReady()){
				Player p = solveWinner();
				if(p!=null){
					List<Player> tmplist = listplayers;
					idtoclientassistant.get(p.ID).sendMessage("YOUWIN");
					tmplist.remove(p);
					sendMulticastMessage(listplayers, "YOULOSE");
					resetPlayers();
					System.out.println("[GAME]-> Closing ClientAssistant sockets...");
//					for(ClientAssistant ca : server.clientlist){
//						try {
//							ca.socket.close();
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
				}
			}
		}
	}
	
	Player solveWinner(){
		int totalamount=0;
		int difference,lastdifference=3;
		Player winner=null;
		for(Player p : listplayers){
			totalamount=totalamount+p.tokens;
		}
		for(Player p : listplayers){
			difference=totalamount-p.totaltokens;
			if(difference<lastdifference){
				difference=totalamount-p.totaltokens;
				winner=p;
			}
		}
		return winner;
	}
	
	boolean checkAllReady(){
		for(Player p : listplayers){
			if(p.tokens==-1)
				return false;
		}
		return true;
	}
	
	void resetPlayers(){
		for(Player p : listplayers){
			p.score=0;
			p.tokens=-1;
			p.totaltokens=-1;
		}
	}
}
