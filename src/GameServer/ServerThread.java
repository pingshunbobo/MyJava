package GameServer;

import java.io.*;
import java.nio.channels.SelectionKey;

/**
 * Description:
 * <br/>��վ: <a href="http://www.crazyit.org">���Java����</a>
 * <br/>Copyright (C), 2001-2014, Yeeku.H.Lee
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @author  Yeeku.H.Lee kongyeeku@163.com
 * @version  1.0
 */
// ������ÿ���߳�ͨ�ŵ��߳���
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
					
					//�ȴ������е���Ϣ����ȡ�ÿ���Ȩ��
					Server.UserProcessQueue.wait();
					user = Server.UserProcessQueue.poll();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//��ʽ����ͻ�����
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