package GameServer;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

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
	// 定义当前线程所处理的Socket
	Socket s = null;
	SocketChannel sc = null;
	// 该线程所处理的Socket所对应的输入流
	ByteBuffer buf = ByteBuffer.allocate(1024);
	public ServerThread() throws IOException
	{
		this.sc = s.getChannel();
	}
	public void run()
	{
		try{
			// 采用循环不断从Socket中读取客户端发送过来的数据
			while ((readFromClient()) != -1){
				try{
					System.out.println("write");
					this.sc.write(buf);
				}
				catch(SocketException e){
					e.printStackTrace();
					// 删除该Socket。
					this.s.close();
				}
			}
		}
		catch (IOException e){
			e.printStackTrace();
		}

	}
//定义读取客户端数据的方法
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
		// 如果捕捉到异常，表明该Socket对应的客户端已经关闭
		catch (IOException e)
		{
			e.printStackTrace();
			s.close();
		}
		return -1;
	}
}