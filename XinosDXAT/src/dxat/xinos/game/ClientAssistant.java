package dxat.xinos.game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

/**
 * Class that contains all elements and functions to assist the client connected
 * @author Jonathan
 *
 */
public class ClientAssistant implements Runnable{

	Socket socket;			//Socket related with the client connection		
	Game game;				//Pointer to Game object			
	String playerid;		//ID related with the player. It's is used to match the connection with the player.
	
	public ClientAssistant(Socket s, Game g){
		socket=s;
		game=g;
	}
	
	@Override
	public void run() {
		BufferedReader br;
		String firstarg=null,secondarg=null,thirdarg = null;
		String incommingsentence=null;
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while(true){
				incommingsentence=br.readLine();
				String[] s=splitMessage(incommingsentence);
				firstarg=s[0].toUpperCase();
				secondarg=s[1].toUpperCase();
				if(s.length>2)
					thirdarg=s[2].toUpperCase();
				/*
				 * Here are the actions corresponding with the incoming message
				 */
				if(firstarg.equals("PLAY")&&game.listplayers.size()==0){
					sendMessage("WAIT");
					playerid=secondarg;
					game.listplayers.add(new Player(secondarg));
					game.idtoclientassistant.put(secondarg,this);
					System.out.println("[CLIENT ASSISTANT "+playerid+"]-> Message: "+firstarg+" ID: "+secondarg);
				}
				else if(firstarg.toUpperCase().equals("PLAY")&&game.listplayers.size()>0&&!game.checkExistingPlayer(secondarg)){
					if(playerid==null)
						playerid=secondarg;
					game.listplayers.add(new Player(secondarg));
					game.idtoclientassistant.put(secondarg,this);
					game.sendMulticastMessage(game.listplayers,"YOUR_BET");
					System.out.println("[CLIENT ASSISTANT "+playerid+"]-> Message: "+firstarg+" ID: "+secondarg);
				}
				
				else if(firstarg.toUpperCase().equals("MYBET")&&secondarg.toUpperCase()!=null&&thirdarg!=null){
					Player pr = game.findPlayerReg(playerid);
					pr.tokens=Integer.parseInt(secondarg);
					pr.totaltokens=Integer.parseInt(thirdarg);
				}
				else
					sendMessage("[CLIENT ASSISTANT]-> CANNOT_UNDERSTAND_YOUR_MESSAGE");
			}
		} 
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		};
	}
	
	/**
	 * This function splits a message separated with '_'
	 * @param message Message to split
	 * @return An string array with the parts of the message
	 */
	String[] splitMessage(String message){
		return message.split("_");
	}
	
	/**
	 * Sends a message through the socket
	 * @param message Message to send
	 */
	void sendMessage(String message){
		try {
			DataOutputStream response = new DataOutputStream(socket.getOutputStream());
			response.writeBytes(message+"\n");
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
