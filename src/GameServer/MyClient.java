package GameServer;

import java.net.*;
import java.io.*;

public class MyClient {
	public static void main(String [] args){
		String serverName = "127.0.0.1";
		int port = 8088;
		try
	      {
	         System.out.println("Connecting to " + serverName
	                             + " on port " + port);
	         Socket client = new Socket(serverName, port);
	         
	         //������ӳɹ���Ϣ
	         System.out.println("Just connected to "
	                      + client.getRemoteSocketAddress());
	         
	         //�������ݵ�����ˡ�
	         OutputStream outToServer = client.getOutputStream();
	         DataOutputStream out =
	                       new DataOutputStream(outToServer); 
	         out.writeUTF("Hello from "
	                      + client.getLocalSocketAddress()+"\r\n");        
	         
	         //�������Է���˵�����
	         InputStream inFromServer = client.getInputStream();
	         DataInputStream in =
	                        new DataInputStream(inFromServer);
	         BufferedReader br = new BufferedReader(new InputStreamReader(
	     			in , "utf-8"));
	         System.out.println("Server says :" + br.readLine());
	         client.close();
	      }catch(IOException e)
	      {
	         e.printStackTrace();
	      }
	}
}
