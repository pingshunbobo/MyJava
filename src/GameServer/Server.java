/*
 * main thread.
 * 负责所有的 tcp 连接 和socket io
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
	
	//Hash表存储核心数据
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
        ServerSocket listensocket = server.socket();
        InetSocketAddress address = new InetSocketAddress(8088);
        try {
        	listensocket.bind(address);
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
			//阻塞等待。
			SocketSelect();
	        
			Set <SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator <SelectionKey> keyIterator = selectedKeys.iterator();
			
			while(keyIterator.hasNext()) {
				SelectionKey key = keyIterator.next();
				if(key.isAcceptable()) {
				    System.out.println("isAcceptable");
					Socket sock = SocketAccept(listensocket);
					String sockstr = sock.getRemoteSocketAddress().toString();
				    User newuser = new User(sock);
				    newuser.ReadRegister();
				    usermap.put(sockstr, newuser);
				    
				}  else if (key.isReadable()) {
					System.out.println("isReadable");
					
					User user = FindUser(key);
					if( SocketRead(user) > 0 ){
						System.out.println("Notice!");
						NoticeProcesser(user);
					} else{
						SocketClose(key.channel());
					}
					
				} else if (key.isWritable()) {
					System.out.println("isWritable");
					User user = FindUser(key);
					SocketWrite(user);
					user.ReadRegister();
					
				} else{
					((SelectionKey) key).cancel();
					SocketClose(key.channel());
				}
				keyIterator.remove();
			}
		}
	}//End of main function.
	
	//对select 函数的封装。
	static int SocketSelect(){
		try {
			selector.select(3);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
		
	}
	
	//对accept函数的封装。
	static Socket SocketAccept(ServerSocket listensocket){
		Socket sock = null;
		try {
			sock = listensocket.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sock;
	}
	
	//读取数据到用户空间。
	static int SocketRead(User user){
    	int ReadBytes = 0;
    	int ALLReads = 0;
		ByteBuffer buf = User.bufin;
		
		try {
			ReadBytes = user.sc.read(buf);
			while (ReadBytes > 0) {
				ALLReads += ReadBytes;
				ReadBytes = user.sc.read(buf);
			}
		} catch (IOException e) {
			SocketClose(user.sc);
			user.CancelRegister();
		}
		System.out.println("read " + ALLReads);
		System.out.println(buf.asCharBuffer().toString());
		return ALLReads;
    }
	
	//输出数据到socket。
    static int SocketWrite(User user){
    	int bytewrites = 0;
    	
    	ByteBuffer buf = User.bufout;
    	buf.flip();
		try {
			bytewrites = user.sc.write(buf);
		} catch (IOException e) {
			SocketClose(user.sc);
			user.CancelRegister();
		}
		System.out.println("Write " + bytewrites);
		buf.clear();
		return bytewrites;
    }
    
    //关闭Socket连接。
    static void SocketClose(SelectableChannel selectableChannel) {
		try {
			selectableChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    //ͨ通过SelectionKey找到对应的User全局表数据。
    static private User FindUser(SelectionKey key){
    	SocketAddress sa = null;
		try {
			sa = ((SocketChannel) key.channel()).getRemoteAddress();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return Server.usermap.get(sa.toString());
    }
    
    static private void NoticeProcesser(User user){
    	//加入处理队列,交由线程池处理。
		synchronized(UserProcessQueue){
			UserProcessQueue.offer(user);
			UserProcessQueue.notify();
		}
    }
    
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
}
