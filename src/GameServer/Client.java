package GameServer;

import java.net.*;
import java.util.Random;
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
			System.out.println(i);
			this.connect();
		}
	}
	
	//建立一个socket连接 
	public void connect(){
		try {
			Socket conn = new Socket(serverName, port);
			this.TalkToServer(conn);
			conn.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
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
			try {
				Random random = new Random();
				Thread.sleep(random.nextInt(65535)%10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}

