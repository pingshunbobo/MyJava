package GameServer;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

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
	// ���嵱ǰ�߳��������Socket
	Socket s = null;
	SocketChannel sc = null;
	// ���߳��������Socket����Ӧ��������
	ByteBuffer buf = ByteBuffer.allocate(1024);
	public ServerThread(Socket s) throws IOException
	{
		this.sc = s.getChannel();		
	}
	public void run()
	{
		try{
			// ����ѭ�����ϴ�Socket�ж�ȡ�ͻ��˷��͹���������
			while ((readFromClient()) != -1){
				try{
					System.out.println("write");
					this.sc.write(buf);
				}
				catch(SocketException e){
					e.printStackTrace();
					// ɾ����Socket��
					this.s.close();
				}
			}
		}
		catch (IOException e){
			e.printStackTrace();
		}

	}
//�����ȡ�ͻ������ݵķ���
	private int readFromClient() throws IOException
	{
		try
		{
			int bytesRead = this.sc.read(buf);
			while (bytesRead != 0) {
				System.out.println("Read " + bytesRead);
				buf.flip();
		
				while(buf.hasRemaining()){
					System.out.print((char) buf.get());
				}
				buf.clear();
				bytesRead = this.sc.read(buf);
			}
			System.out.println("read over!");
			return bytesRead;
		}
		// �����׽���쳣��������Socket��Ӧ�Ŀͻ����Ѿ��ر�
		catch (IOException e)
		{
			e.printStackTrace();
			s.close();
		}
		return -1;
	}
}