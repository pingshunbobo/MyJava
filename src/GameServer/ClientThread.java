package GameServer;

import java.io.IOException;

//负责处理每个线程通信的线程类
public class ClientThread implements Runnable
{
	int thread_id = 0;
	public ClientThread(int id)
	{
		thread_id = id;
		//System.out.println("A new thread.\n");
	}
	public void run()
	{
		Client conn = new Client(thread_id);
		try {
			conn.TalkToServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}