package GameServer;

import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.io.*;

public class Client2 {
	
	public static void connect() throws IOException{
		String serverName = "127.0.0.1";
		int port = 8088;
        Socket client = null;
        System.out.println("Connecting to " + serverName
                + " on port " + port);
		try
		{
			client = new Socket(serverName, port);
	        	//输出连接成功信息
		        System.out.println("Just connected to "
		        		+ client.getRemoteSocketAddress());

		        SocketChannel socketch = SocketChannel.open();
		        socketch = client.getChannel();
			    //socketch.configureBlocking(false);
				
				ByteBuffer buf = ByteBuffer.allocate(1024);
				int ReadBytes = socketch.read(buf);
				System.out.println("read " + ReadBytes);

				/*
		        //接收来自服务端的数据
		        InputStream inFromServer = client.getInputStream();
		        DataInputStream in =
		        		new DataInputStream(inFromServer);
		        BufferedReader br = new BufferedReader(new InputStreamReader(
		     			in , "utf-8"));
		        System.out.println("Server says :" + br.readLine());
		        */
		        //发送数据到服务端。

				OutputStream outToServer = client.getOutputStream();
		        DataOutputStream out =
		                       new DataOutputStream(outToServer);
		        out.writeUTF("Hello from "
		        		+ client.getLocalSocketAddress() + "\r\n");
	      }
		  catch(IOException e)
	      {
			  client.close();
			  e.printStackTrace();
	      }
	}
	
	public static void main(String [] args) {
		try {
			connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
