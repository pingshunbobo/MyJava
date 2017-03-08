package GameServer;

public class ClientTest {
	public static void main(String [] args) {
		int connects = 0;
		int counts = 0;
		Thread[] WorkThread = new Thread[10];
		System.out.println("!!!Client test start!!!!");

		if(args.length != 2){
			System.out.println("Usage: ClientTest conns times");
			return;
		}
		connects = Integer.parseInt(args[0]);
		counts = Integer.parseInt(args[1]);

		//根据参数，建立线程组
        for(int i=0; i<10; i++){
    		WorkThread[i] = new Thread(new ClientThread(i, connects, counts));
    		WorkThread[i].start();
        };
        
        //等待所有线程结束！
        for(int i=0; i<10; i++){
        	try {
				WorkThread[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }  
        
        System.out.println("!!!CLIENT TEST OVER!!!");
	}
}