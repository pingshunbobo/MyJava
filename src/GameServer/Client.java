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
	        
	        while(true){
	        	//发送数据到服务端。
		        OutputStream outToServer = client.getOutputStream();
		        DataOutputStream out =
		                       new DataOutputStream(outToServer);
		        //产生一个随机数。
		        java.util.Random RandomNumber = new java.util.Random();
		        int RandomN = RandomNumber.nextInt(65535);
		        
		        System.out.print("Sent to erver: " + RandomN + "\t");
		        out.writeBytes( (int)RandomN + "\r\n");
		        //out.writeBytes( "abcd" + "\r\n");
		        
		        //接收来自服务端的数据
		        InputStream inFromServer = client.getInputStream();
		        DataInputStream in =
		        		new DataInputStream(inFromServer);
		        BufferedReader br = new BufferedReader(new InputStreamReader(
		     			in , "utf-8"));
		        System.out.println("Server says :" + br.readLine());
		        
	         }
	      }
		  catch(IOException e)
	      {
			  client.close();
			  e.printStackTrace();
	      }
	}
}
