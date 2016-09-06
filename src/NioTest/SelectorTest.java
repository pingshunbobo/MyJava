package NioTest;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SelectorTest {
	public static void main(String [] args) throws IOException{

		Selector selector = Selector.open();
		//创建可选择通道，并配置为非阻塞模式  
        ServerSocketChannel server = ServerSocketChannel.open();  
        server.configureBlocking(false);
  
        //绑定通道到指定端口  
        ServerSocket socket = server.socket();  
        InetSocketAddress address = new InetSocketAddress(8088);
        socket.bind(address);
        
        //向Selector中注册感兴趣的事件  
        SelectionKey key = server.register(selector, SelectionKey.OP_ACCEPT);
		
		while(true) {
		  int readyChannels = selector.select();
		  if(0 == readyChannels)
			  continue;
		  Set <SelectionKey> selectedKeys = selector.selectedKeys();
		  Iterator <SelectionKey> keyIterator = selectedKeys.iterator();
		  while(keyIterator.hasNext()) {
		    SelectionKey key2 = keyIterator.next();
		    if(key2.isAcceptable()) {
		    	System.out.println("isAcceptable"); 

		    	Socket s = socket.accept();
		    	
		    	//创建socket Channel。
		    	SocketChannel socketch = SocketChannel.open(); 
		    	socketch = s.getChannel();
		    	socketch.configureBlocking(false);
		    	socketch.register(selector, SelectionKey.OP_READ);
		    	
		        // a connection was accepted by a ServerSocketChannel.
		    } else if (key2.isConnectable()) {
		    	System.out.println("服务端一般不会用到");
		        // a connection was established with a remote server.
		    } else if (key2.isReadable()) {
		    	System.out.println("isReadable");
		        // a channel is ready for reading
		    } else if (key2.isWritable()) {
		    	System.out.println("isWritable");
		        // a channel is ready for writing
		    }
		    keyIterator.remove();
		  }
		}
	}
}
