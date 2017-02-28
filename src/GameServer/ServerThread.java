package GameServer;

import java.io.*;
import java.nio.ByteBuffer;

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
					e.printStackTrace();
				}
				
				//��ʽ����ͻ�����
				DataProcess(user);
			}
		}
	}
	
	private void DataProcess(User user){
		DataEcho(user);
		user.WriteRegister();
	}
	
	//�򵥽����븴�Ƶ������
	private void DataEcho(User user){
		ByteBuffer buf = User.bufin;
		buf.flip();				//��buf��������Ļ�����
		while(buf.hasRemaining()){
			char ch = (char) buf.get();
			User.bufout.put((byte) ch);
		}
		buf.clear();
	}
}