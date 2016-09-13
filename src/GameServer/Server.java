/*
 * main thread.
 * �����tcp���Ӻ�socket io��
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
    	//�������п���һ��ѡ���������ڼ������¼���
    	Server.selector = Selector.open();
    	
        ServerSocketChannel server = ServerSocketChannel.open();
        server.configureBlocking(false);
        
		// ����10��ServerThread�߳�Ϊ�ÿͻ��˷���
        for(int i=0;i<10;i++){
    		Thread NewThread = new Thread(new ServerThread());
    		NewThread.start();
        }
  
        //��ͨ����ָ���˿�  
        ServerSocket socket = server.socket();  
        InetSocketAddress address = new InetSocketAddress(8088);
        socket.bind(address);
        
        //��Selector��ע������¼�  
        server.register(selector, SelectionKey.OP_ACCEPT);
        
		while(true){
			selector.select(3);	//�����ȴ���
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
    
    
    //��ȡ���ݡ�
	static int GameRead(User user) throws IOException{
    	if(user.equals(null)){
    		System.out.println("Null user error!");
    		return -1;
    	}
		
		ByteBuffer buf = User.bufin;
		
    	int bytesRead = user.sc.read(buf);
		while (bytesRead > 0) {
			System.out.println("Read " + bytesRead);
			
			//��buf��������Ļ�����
			buf.flip();
			while(buf.hasRemaining()){
				char ch = (char) buf.get();
				User.bufout.put((byte) ch);
			}
			buf.clear();
			bytesRead = user.sc.read(buf);
		}
		//System.out.println("read over!");

		//���봦����С�
		synchronized(UserProcessQueue){
			UserProcessQueue.offer(user);
			UserProcessQueue.notify();
		}
		return 0;
    }
	
	//���socket���ݡ�
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
    
    //ͨ��SocketAddress�ҵ���Ӧ��User
    static User FindUser(SocketAddress sa){
    	return Server.UserMap.get(sa.toString());
    }
}
