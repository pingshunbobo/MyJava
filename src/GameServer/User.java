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
	
	SocketChannel sc=null;
	SocketAddress sa=null;
	static ByteBuffer bufin = null;
	static ByteBuffer bufout = null;

	public User(Socket s) {
	    
		//´´½¨socket Channel¡£
		try{
		    SocketChannel socketch = SocketChannel.open();
		    socketch = s.getChannel();
		    socketch.configureBlocking(false);
		    socketch.register(Server.selector, SelectionKey.OP_READ);
			sc = socketch;
		}
		catch(IOException e){
			e.printStackTrace();
		}
	    
		sa = s.getRemoteSocketAddress();
    	bufin = ByteBuffer.allocate(1024);
    	bufout = ByteBuffer.allocate(1024);
    	System.out.println("New User!");
	}

}
