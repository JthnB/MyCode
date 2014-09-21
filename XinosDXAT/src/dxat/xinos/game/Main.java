package dxat.xinos.game;

import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		int NUMMAXPLAYERS=10;
		Server server = new Server(5021);
		List<Client> listclients = new ArrayList<Client>();
		Thread tserver = new Thread(server,"ServerThread");
		tserver.start();
		for(int i=0;i<NUMMAXPLAYERS;i++){
			listclients.add(new Client("localhost",5021,""+i));
		}
		Thread tclient1 = new Thread(listclients.get(0),"Client1Thread");
		tclient1.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Thread tclient2 = new Thread(listclients.get(1),"Client2Thread");
		tclient2.start();
	}

}
