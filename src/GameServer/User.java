/*
 * use hashmap storage user RemoteAddress -> data.
 * 
 * */

package GameServer;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class User {
	
	Socket so = null;
	SocketChannel sc = null;
	SocketAddress sa = null;
	static ByteBuffer bufin = null;
	static ByteBuffer bufout = null;

	public User(Socket s) {
		this.so = s;
		
		sa = s.getRemoteSocketAddress();
    	bufin = ByteBuffer.allocate(1024);
    	bufout = ByteBuffer.allocate(1024);
    	
    	SocketChannelOpen(s);
    	System.out.print("New User @");
	    System.out.println(s.getRemoteSocketAddress().toString());
	}

	private void SocketChannelOpen(Socket s){
	    SocketChannel socketch;
	    
		try {
			socketch = SocketChannel.open();
		    socketch = s.getChannel();
		    socketch.configureBlocking(false);
		    this.sc = socketch;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void ReadRegister(){
		try{
		    this.sc.register(Server.selector, SelectionKey.OP_READ);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void WriteRegister(){
		try{
		    sc.register(Server.selector, SelectionKey.OP_WRITE);
		    //sc.register(Server.selector, 0);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void CancelRegister(){
		sc.keyFor(Server.selector).cancel();
	}
}
