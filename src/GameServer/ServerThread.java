package GameServer;

//负责处理每个线程通信的线程类
public class ServerThread implements Runnable
{
	public ServerThread()
	{
		//System.out.println("A new thread.\n");
	}
	public void run()
	{
		User user = null;
		synchronized(Server.UserProcessQueue){
			while(true){
				try {
						//等待队列中的消息，并取得控制权。
						Server.UserProcessQueue.wait();
						user = Server.UserProcessQueue.poll();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//正式处理客户请求。
				user.DataProcess();
			}
		}
	}
	
}