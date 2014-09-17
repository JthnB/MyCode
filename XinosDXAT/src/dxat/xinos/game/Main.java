package dxat.xinos.game;

import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		int NUMMAXPLAYERS=10;
		Server server = new Server(5021);
		List<Player> listplayers = new ArrayList<Player>();
		Thread t1 = new Thread(server);
		t1.start();
		try {
			Thread.sleep(1000);
			
			for(int i=0;i<NUMMAXPLAYERS;i++){
				listplayers.add(new Player(new Client("localhost",5021+i)));
			}
		} 
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
