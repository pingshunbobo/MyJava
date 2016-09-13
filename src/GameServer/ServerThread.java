package GameServer;

import java.io.*;
import java.nio.channels.SelectionKey;

/**
 * Description:
 * <br/>网站: <a href="http://www.crazyit.org">疯狂Java联盟</a>
 * <br/>Copyright (C), 2001-2014, Yeeku.H.Lee
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @author  Yeeku.H.Lee kongyeeku@163.com
 * @version  1.0
 */
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
				ProcessData(user);
			}
		}
	}
	public void ProcessData(User user){
		User.bufout.putChar('v');
		User.bufout.putChar('v');
		User.bufout.putChar('v');
		User.bufout.putChar('v');
		User.bufout.putChar('v');
	    try {
			user.sc.configureBlocking(false);
		    user.sc.register(Server.selector, SelectionKey.OP_WRITE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}