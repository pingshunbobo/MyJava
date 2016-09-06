package study;

import java.net.*;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.io.*;
import java.util.*;

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
public class MyServer
{
	// ���屣������Socket��ArrayList
	public static ArrayList<Socket> socketList
		= new ArrayList<Socket>();
    public static void main(String[] args)
		throws IOException
    {
		ServerSocket ss = new ServerSocket(30001);
		ServerSocketChannel serversocketChannel = ss.getChannel();
		while(true)
		{
			// ���д������������һֱ�ȴ����˵�����
			Socket s = ss.accept();
			SocketChannel socketChannel = s.getChannel();
			socketList.add(s);
			// ÿ���ͻ������Ӻ�����һ��ServerThread�߳�Ϊ�ÿͻ��˷���
			Thread th1 = new Thread(new ServerThread(s));
			th1.start();
		}
    }
}
