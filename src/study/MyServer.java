package study;

import java.net.*;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.io.*;
import java.util.*;

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
public class MyServer
{
	// 定义保存所有Socket的ArrayList
	public static ArrayList<Socket> socketList
		= new ArrayList<Socket>();
    public static void main(String[] args)
		throws IOException
    {
		ServerSocket ss = new ServerSocket(30001);
		ServerSocketChannel serversocketChannel = ss.getChannel();
		while(true)
		{
			// 此行代码会阻塞，将一直等待别人的连接
			Socket s = ss.accept();
			SocketChannel socketChannel = s.getChannel();
			socketList.add(s);
			// 每当客户端连接后启动一条ServerThread线程为该客户端服务
			Thread th1 = new Thread(new ServerThread(s));
			th1.start();
		}
    }
}

class ServerThread implements Runnable
{
	// 定义当前线程所处理的Socket
	Socket s = null;
	// 该线程所处理的Socket所对应的输入流
	BufferedReader br = null;
	public ServerThread(Socket s) throws IOException
	{
		this.s = s;
		// 初始化该Socket对应的输入流
		br = new BufferedReader(new InputStreamReader(
			s.getInputStream() , "utf-8"));   // ②
	}
	public void run()
	{
		try
		{
			String content = null;
			// 采用循环不断从Socket中读取客户端发送过来的数据
			while ((content = readFromClient()) != null)
			{
				//System.out.println("---" + Arrays.toString(content.getBytes("utf-8")));
				System.out.println("---" + content);
				// 遍历socketList中的每个Socket，
				// 将读到的内容向每个Socket发送一次
				for (Iterator<Socket> it = MyServer.socketList.iterator(); it.hasNext(); )
				{
					Socket s = it.next();
					try{

						OutputStream os = s.getOutputStream();
						os.write((content + "\n").getBytes("utf-8"));
					}
					catch(SocketException e)
					{
						e.printStackTrace();
						// 删除该Socket。
						this.s.close();
						it.remove();
						//System.out.println(MyServer.socketList);
					}
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	// 定义读取客户端数据的方法
	private String readFromClient() throws IOException
	{
		try
		{
			return br.readLine();
		}
		// 如果捕捉到异常，表明该Socket对应的客户端已经关闭
		catch (IOException e)
		{
			e.printStackTrace();
			s.close();
			// 删除该Socket。
			MyServer.socketList.remove(s);  // ①
		}
		return null;
	}
}
