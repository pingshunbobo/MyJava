package GameServer;

import java.net.*;
import java.io.*;

public class Client {
	int ClientId = 0 ;
	int connect_times = 0;
	int request_count = 0;
	
	int port = 8088;
	String serverName = "127.0.0.1";
	
	public Client(int id, int Counts, int Times) {
		this.ClientId = id;
		this.connect_times = Counts;
		this.request_count = Times;
	}
	
	public void work(){
		//建立 connectCount个连接。
		for(int i = 0; i < this.connect_times; i++){
			this.connect();
		}
	}
	
	public void connect(){
		try {
			Socket conn = new Socket(serverName, port);
			this.TalkToServer(conn);
			conn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void TalkToServer(Socket connsocket) throws IOException{
		
		//循环收发数据。
		for(int i = 0; i < this.request_count; i++){
			//发送数据到服务端。
			OutputStream outToServer = connsocket.getOutputStream();
			DataOutputStream out =
					new DataOutputStream(outToServer);
			out.writeBytes( "abcd\r\n");

			//接收来自服务端的数据
			InputStream inFromServer = connsocket.getInputStream();
			DataInputStream in =
					new DataInputStream(inFromServer);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					in , "utf-8"));
			
			//System.out.println("Server says :" + br.readLine());
		}
	}
	
}

