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
	static Selector selector;
	static HashMap< String , User> UserMap = 
		      new HashMap< String, User>();
	
	static Queue <User> UserProcessQueue 
		= new LinkedList<User>();
	
    public static void main(String[] args)
		throws IOException
    {
    	//主函数中开启一个选择器，用于监听多事件。
    	Server.selector = Selector.open();
    	
        ServerSocketChannel server = ServerSocketChannel.open();
        server.configureBlocking(false);
        
		// 开启10个ServerThread线程为该客户端服务。
        for(int i=0;i<10;i++){
    		Thread NewThread = new Thread(new ServerThread());
    		NewThread.start();
        }
  
        //绑定通道到指定端口  
        ServerSocket socket = server.socket();  
        InetSocketAddress address = new InetSocketAddress(8088);
        socket.bind(address);
        
        //向Selector中注册监听事件  
        server.register(selector, SelectionKey.OP_ACCEPT);
        
		while(true){
			selector.select(3);	//阻塞等待。
			Set <SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator <SelectionKey> keyIterator = selectedKeys.iterator();
			while(keyIterator.hasNext()) {
				SelectionKey key = keyIterator.next();
				if(key.isAcceptable()) {
				    //System.out.println("isAcceptable");
					Socket s = socket.accept();
				    User NewUser = new User(s);
				    UserMap.put(s.getRemoteSocketAddress().toString(),NewUser);

				} else if (key.isReadable()) {
					//System.out.println("isReadable");
					GameRead(FindUser(((SocketChannel) key.channel()).getRemoteAddress()));
					
				} else if (key.isWritable()) {
					//System.out.println("isWritable");
					GameWrite(FindUser(((SocketChannel) key.channel()).getRemoteAddress()));
					((SelectionKey) key).cancel();
				}
				keyIterator.remove();
			}
		}
	}//end main function.   
    
    
    //读取数据。
	static int GameRead(User user) throws IOException{
    	if(user.equals(null)){
    		System.out.println("Null user error!");
    		return -1;
    	}
		
		ByteBuffer buf = User.bufin;
		
    	int bytesRead = user.sc.read(buf);
		while (bytesRead > 0) {
			System.out.println("Read " + bytesRead);
			
			//将buf内容做屏幕输出。
			buf.flip();
			while(buf.hasRemaining()){
				char ch = (char) buf.get();
				User.bufout.put((byte) ch);
			}
			buf.clear();
			bytesRead = user.sc.read(buf);
		}
		//System.out.println("read over!");

		//加入处理队列。
		synchronized(UserProcessQueue){
			UserProcessQueue.offer(user);
			UserProcessQueue.notify();
		}
		return 0;
    }
	
	//输出socket数据。
    static int GameWrite(User user) throws IOException{
    	if(user.equals(null)){
    		System.out.println("Null user error!");
    		return -1;
    	}
    	ByteBuffer buf = User.bufout;
    	buf.flip();
    	int byteswrite = user.sc.write(buf);
		System.out.println("Write " + byteswrite);
		//System.out.println("Write over!");
		buf.clear();
		return byteswrite;
    }
    
    //通过SocketAddress找到对应的User
    static User FindUser(SocketAddress sa){
    	return Server.UserMap.get(sa.toString());
    }
}
