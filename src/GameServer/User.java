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
	int sahashCode = 0;
	static ByteBuffer bufin = null;
	static ByteBuffer bufout = null;

	public User(Socket s) throws IOException {
	    
		//´´½¨socket Channel¡£
	    SocketChannel socketch = SocketChannel.open();
	    socketch = s.getChannel();
	    socketch.configureBlocking(false);
	    socketch.register(Server.selector, SelectionKey.OP_READ);
	    
		sc = socketch;
		sahashCode = sa.hashCode();
    	bufin = ByteBuffer.allocate(1024);
    	bufout = ByteBuffer.allocate(1024);
	}

}
