package GameServer;

//负责处理每个线程通信的线程类
public class ClientThread implements Runnable
{
	int thread_id = 0;
	int connect_times = 0;
	int request_count = 0;
	
	public ClientThread(int id, int times, int count) 
	{
		this.thread_id = id;
		this.connect_times = times;
		this.request_count = count;
	}
	
	public void run()
	{
		Client cli = new Client(thread_id, connect_times, request_count);
		cli.work();
		System.out.println("Thread " + this.thread_id + " work over!");
	}
	
}