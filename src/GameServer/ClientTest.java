package GameServer;


public class ClientTest {
	public static void main(String [] args) {
		Thread WorkThread = null;
		
		System.out.println("Client Test!");

        for(int i=0; i<10000; i++){
			WorkThread = new Thread(new ClientThread(i));
    		WorkThread.start();
        };
	}
}