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
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Server {
	
	//TCP 多路监听连接 
	static Selector selector;
	
	//存储核心数据
	static HashMap< String , User> usermap = 
		      new HashMap< String, User>();

	//任务队列，供线程池异步接受任务
	static Queue <User> UserProcessQueue 
		= new LinkedList<User>();
	
    public static void main(String[] args){
    	//主线程中开启一个选择器，用于监听多事件。
    	try {
			Server.selector = Selector.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
        ServerSocketChannel server = null;
		try {
			server = ServerSocketChannel.open();
	        server.configureBlocking(false);
		} catch (IOException e) {
			e.printStackTrace();
		}

		//创建线程池
		ThreadPool();
  
        //绑定通道到指定端口  
        ServerSocket socket = server.socket();  
        InetSocketAddress address = new InetSocketAddress(8088);
        try {
			socket.bind(address);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        //向Selector中注册监听事件  
        try {
			server.register(selector, SelectionKey.OP_ACCEPT);
		} catch (ClosedChannelException e) {
			System.out.println("Bind port error!");
			e.printStackTrace();
		}
        
		while(true){
			try {
				//阻塞等待。
				selector.select(3);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Set <SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator <SelectionKey> keyIterator = selectedKeys.iterator();
			while(keyIterator.hasNext()) {
				SelectionKey key = keyIterator.next();
				if(key.isAcceptable()) {
				    System.out.println("isAcceptable");
					Socket s = null;
					try {
						s = socket.accept();
					} catch (IOException e) {
						e.printStackTrace();
					}
				    User newuser = new User(s);
				    usermap.put(s.getRemoteSocketAddress().toString(), newuser);
				    
				} else if (key.isReadable()) {
					System.out.println("isReadable");
					try {
						GameRead(FindUser(((SocketChannel) key.channel()).getRemoteAddress()));
					} catch (IOException e) {
						((SelectionKey) key).cancel();						
						GameClose(key.channel());
					}
					
				} else if (key.isWritable()) {
					System.out.println("isWritable");
					try {
						GameWrite(FindUser( ((SocketChannel)key.channel()).getRemoteAddress() ));
						key.channel().register(selector, SelectionKey.OP_READ);
						System.out.println("read registe ok!");
					} catch (IOException e) {
							((SelectionKey) key).cancel();
							GameClose(key.channel());
					}
				}else{
					((SelectionKey) key).cancel();
					GameClose(key.channel());
				}
				keyIterator.remove();
			}
		}
	}//End of main function.


	private static void ThreadPool() {
		// 开启10个ServerThread线程为该客户端服务。
        for(int i=0;i<10;i++){
    		Thread WorkThread = null;
			try {
				WorkThread = new Thread(new ServerThread());
			} catch (IOException e) {
				e.printStackTrace();
			}
    		WorkThread.start();
        }
	}


	//读取数据。
	static int GameRead(User user) throws IOException{
    	int bytesRead = 0;
    	
    	if(user.equals(null)){
    		System.out.println("Null user error!");
    		return -1;
    	}
		
		ByteBuffer buf = User.bufin;
		
		bytesRead = user.sc.read(buf);
		while (bytesRead > 0) {
			System.out.println("Read " + bytesRead);
			bytesRead = user.sc.read(buf);
		}
		//加入处理队列。
		synchronized(UserProcessQueue){
			UserProcessQueue.offer(user);
			UserProcessQueue.notify();
		}
		return 0;
    }
	
	//输出socket数据。
    static int GameWrite(User user) throws IOException{
    	int byteswrites = 0;
    	
    	if(user.equals(null)){
    		System.out.println("Null user error!");
    		return -1;
    	}
    	ByteBuffer buf = User.bufout;
    	buf.flip();
		byteswrites = user.sc.write(buf);
		System.out.println("Write " + byteswrites);
		buf.clear();
		return byteswrites;
    }
    
    private static void GameClose(SelectableChannel selectableChannel) {
		try {
			selectableChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    //通过SocketAddress找到对应的User全局表数据。
    static private User FindUser(SocketAddress sa){
    	return Server.usermap.get(sa.toString());
    }
}
