package GameServer;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//正式处理客户请求。
				DataProcess(user);
			}
		}
	}
	private void DataProcess(User user){		
		ByteBuffer buf = User.bufin;
		buf.flip();				//将buf内容做屏幕输出。
		while(buf.hasRemaining()){
			char ch = (char) buf.get();
			User.bufout.put((byte) (ch + 1));
		}
		buf.clear();
		
	    try {
			user.sc.configureBlocking(false);
		    user.sc.register(Server.selector, SelectionKey.OP_WRITE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}