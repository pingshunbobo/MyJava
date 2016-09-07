/*
 * main thread.
 * 完成新tcp连接和socket io。
 * 
 * */

package GameServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;

public class Server {
	static ByteBuffer buf = null;
    public static void main(String[] args)
		throws IOException
    {
    	buf = ByteBuffer.allocate(1024);
    	//主函数中开启一个选择器，用于监听多事件。
    	Selector selector = Selector.open();
        ServerSocketChannel server = ServerSocketChannel.open();
        server.configureBlocking(false);
        
		// 开启10个ServerThread线程为该客户端服务。
        for(int i=0;i<10;i++){
    		//Thread NewThread = new Thread(new ServerThread());
    		//NewThread.start();
        }
  
        //绑定通道到指定端口  
        ServerSocket socket = server.socket();  
        InetSocketAddress address = new InetSocketAddress(8088);
        socket.bind(address);
        
        //向Selector中注册监听事件  
        server.register(selector, SelectionKey.OP_ACCEPT);
        
		while(true){
			
			selector.select();	//阻塞等待。
			Set <SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator <SelectionKey> keyIterator = selectedKeys.iterator();
			while(keyIterator.hasNext()) {
				SelectionKey key = keyIterator.next();
				if(key.isAcceptable()) {
					Socket s = socket.accept();
				    	
				    //创建socket Channel。
				    SocketChannel socketch = SocketChannel.open(); 
				    socketch = s.getChannel();
				    socketch.configureBlocking(false);
				    socketch.register(selector, SelectionKey.OP_READ);
				  //System.out.println("isAcceptable");

				} else if (key.isReadable()) {
					Gameread((SocketChannel)key.channel(), selector);
					//System.out.println("isReadable");
					
				} else if (key.isWritable()) {
					GameWrite((SocketChannel)key.channel(), selector);
					//System.out.println("isWritable");
					((SelectionKey) key).cancel();
				}
				keyIterator.remove();
			}
		}
	}//end main function.   
    
	static int Gameread(SocketChannel sc, Selector selector) throws IOException{
    	
    	int bytesRead = sc.read(buf);
		while (bytesRead > 0) {
			System.out.println("Read " + bytesRead);
			
			//将buf内容做屏幕输出。
			buf.flip();
			while(buf.hasRemaining()){
				System.out.print((char) buf.get());
			}
			buf.capacity();
			bytesRead = sc.read(buf);
		}
		//System.out.println("read over!");
	    sc.configureBlocking(false);
	    sc.register(selector, SelectionKey.OP_WRITE);
		return 0;
    }
    
    static int GameWrite(SocketChannel sc, Selector selector) throws IOException{
    	
    	buf.flip();
    	int byteswrite = sc.write(buf);
		System.out.println("Write " + byteswrite);
		//System.out.println("Write over!");
		buf.clear();
		return 0;
    }
}
