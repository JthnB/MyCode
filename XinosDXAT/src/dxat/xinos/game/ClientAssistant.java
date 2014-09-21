package dxat.xinos.game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

public class ClientAssistant implements Runnable{

	Socket socket;
	String incommingsentence;
	public String responsemsg="ACK";
	Game game;
	String playerid;
	
	public ClientAssistant(Socket s, Game g){
		socket=s;
		game=g;
	}
	
	@Override
	public void run() {
		BufferedReader br;
		String firstarg,secondarg,thirdarg = null;
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while(true){
				incommingsentence=br.readLine();
				String[] s=splitMessage(incommingsentence);
				firstarg=s[0].toUpperCase();
				secondarg=s[1].toUpperCase();
				if(s.length>2)
					thirdarg=s[2].toUpperCase();
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

	String[] splitMessage(String message){
		return message.split("_");
	}
	
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
