package GameServer;

import java.io.IOException;

public class ClientTest {
	public static void main(String [] args) {
		System.out.println("Client Test!");

		Client clientarray[] = new Client[10];
		for(int i = 0; i< 10; i++){
			clientarray[i] = new Client(i);
			System.out.println(clientarray[i].port);
			System.out.println(clientarray[i].serverName);
			try {
				clientarray[i].TalkToServer();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}