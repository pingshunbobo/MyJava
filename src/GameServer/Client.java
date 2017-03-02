package GameServer;

import java.net.*;
import java.io.*;

public class Client {
	public static void main(String [] args) {
		try {
			connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
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
	        client.setTcpNoDelay(false);
	        
	        while(true){
	        	//发送数据到服务端。
		        OutputStream outToServer = client.getOutputStream();
		        DataOutputStream out =
		                       new DataOutputStream(outToServer);

		        //out.writeBytes( "abcd\r\n" );
		        out.writeBytes( "abcd\r\n" + "abcd\r\n");
		        
		        //接收来自服务端的数据
		        InputStream inFromServer = client.getInputStream();
		        DataInputStream in =
		        		new DataInputStream(inFromServer);
		        BufferedReader br = new BufferedReader(new InputStreamReader(
		     			in , "utf-8"));
		        System.out.println("Server says :" + br.readLine());

		        /*
		        try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
	         }
	      }
		  catch(IOException e)
	      {
			  client.close();
			  e.printStackTrace();
	      }
	}
}
