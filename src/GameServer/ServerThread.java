package GameServer;

import java.io.*;
import java.nio.ByteBuffer;

// 负责处理每个线程通信的线程类
public class ServerThread implements Runnable
{
	public ServerThread() throws IOException
	{
		//System.out.println("A new thread.\n");
	}
	public void run()
	{
		User user = null;
		synchronized(Server.UserProcessQueue){
			while(0 == Server.UserProcessQueue.size()){
				try {
					//等待队列中的消息，并取得控制权。
					Server.UserProcessQueue.wait();
					user = Server.UserProcessQueue.poll();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				//正式处理客户请求。
				DataProcess(user);
			}
		}
	}
	
	private void DataProcess(User user){
		DataEcho(user);
		user.WriteRegister();
	}
	
	//简单讲输入复制到输出。
	private void DataEcho(User user){
		ByteBuffer buf = User.bufin;
		buf.flip();				//将buf内容做屏幕输出。
		while(buf.hasRemaining()){
			char ch = (char) buf.get();
			User.bufout.put((byte) ch);
		}
		buf.clear();
	}
}