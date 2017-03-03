package GameServer;

import java.net.*;
import java.io.*;

public class Client {
	Socket connsocket = null;
	int id = 0 ;
	static int port = 8088;
	static String serverName = "127.0.0.1";
    
	public Client(int id) {
		try {
			this.id = id;
	        System.out.println("Connecting to " + serverName
	                + " on port " + port);
	        
			this.connsocket = new Socket(serverName, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void TalkToServer() throws IOException{
		
		//循环收发数据。
		while(true){
			//发送数据到服务端。
			OutputStream outToServer = connsocket.getOutputStream();
			DataOutputStream out =
					new DataOutputStream(outToServer);
			out.writeBytes( "abcd\r\n" + "abcd\r\n");

			//接收来自服务端的数据
			InputStream inFromServer = connsocket.getInputStream();
			DataInputStream in =
					new DataInputStream(inFromServer);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					in , "utf-8"));
			
			System.out.println(this.id + ": Server says :" + br.readLine());
		}
	}
}

