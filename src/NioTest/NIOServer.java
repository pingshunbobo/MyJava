/*
 * https://github.com/wojiushimogui/Selector
 * */
package NioTest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

	//通道选择器
	private Selector selector;
	public NIOServer(){
		try {
			selector = Selector.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void listen() throws IOException {
		System.out.println("server running....");
		while(true){
			selector.select();
			Set<SelectionKey> set = selector.selectedKeys();
			Iterator<SelectionKey> ite = set.iterator();
			while(ite.hasNext()){
				SelectionKey selectionKey = (SelectionKey) ite.next();
                ite.remove(); 
                if(selectionKey.isAcceptable()){
                	ServerSocketChannel serverSocketChannel = (ServerSocketChannel)selectionKey.channel();
                	SocketChannel socketChannel = serverSocketChannel.accept();
                	System.out.println("有客户端连接到服务器！！！");
                	socketChannel.configureBlocking(false);
                	socketChannel.write(ByteBuffer.wrap(new String("hello client!").getBytes()));
                	socketChannel.register(selector, SelectionKey.OP_READ);
                }
                else if(selectionKey.isReadable()){
                	SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                	ByteBuffer buf = ByteBuffer.allocate(128);
                	socketChannel.read(buf);
            		byte[] receData = buf.array();
            		String msg = new String(receData).trim();
            		System.out.println("接收来自客户端的数据为："+msg);
            		buf.clear();
                	socketChannel.write(ByteBuffer.wrap(new String("收到信息!!!").getBytes()));
                }
			}
			
		}
		
	}

	private void init(int port) {
		try {
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			
			serverSocketChannel.socket().bind(new InetSocketAddress(port));
			serverSocketChannel.configureBlocking(false);
			
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		NIOServer server = new NIOServer();
		server.init(9999);
		server.listen();
	}

}
