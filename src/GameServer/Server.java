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
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class Server {
	static Selector selector;
	static ArrayList <User> UserList 
		= new ArrayList<User>();
	
    public static void main(String[] args)
		throws IOException
    {
    	//�������п���һ��ѡ���������ڼ������¼���
    	Server.selector = Selector.open();
    	
        ServerSocketChannel server = ServerSocketChannel.open();
        server.configureBlocking(false);
        
		// ����10��ServerThread�߳�Ϊ�ÿͻ��˷���
        for(int i=0;i<10;i++){
    		//Thread NewThread = new Thread(new ServerThread());
    		//NewThread.start();
        }
  
        //��ͨ����ָ���˿�  
        ServerSocket socket = server.socket();  
        InetSocketAddress address = new InetSocketAddress(8088);
        socket.bind(address);
        
        //��Selector��ע������¼�  
        server.register(selector, SelectionKey.OP_ACCEPT);
        
		while(true){
			
			selector.select();	//�����ȴ���
			Set <SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator <SelectionKey> keyIterator = selectedKeys.iterator();
			while(keyIterator.hasNext()) {
				SelectionKey key = keyIterator.next();
				if(key.isAcceptable()) {
					Socket s = socket.accept();
				    User NewUser = new User(s);
				    UserList.add(NewUser);
				  //System.out.println("isAcceptable");

				} else if (key.isReadable()) {
					Gameread(FindUser((SocketChannel)key.channel()));
					//System.out.println("isReadable");
					
				} else if (key.isWritable()) {
					GameWrite(FindUser((SocketChannel)key.channel()));
					//System.out.println("isWritable");
					((SelectionKey) key).cancel();
				}
				keyIterator.remove();
			}
		}
	}//end main function.   
    
    
    //��ȡsocket���ݡ�
	static int Gameread(User user) throws IOException{
		ByteBuffer buf = User.bufin;
		
    	int bytesRead = user.sc.read(buf);
		while (bytesRead > 0) {
			System.out.println("Read " + bytesRead);
			
			//��buf��������Ļ�����
			buf.flip();
			while(buf.hasRemaining()){
				char ch = (char) buf.get();
				buf.put((byte) ch);
			}
			buf.clear();
			bytesRead = user.sc.read(buf);
		}
		//System.out.println("read over!");
	    user.sc.configureBlocking(false);
	    user.sc.register(Server.selector, SelectionKey.OP_WRITE);
		return 0;
    }
    
	//���socket���ݡ�
    static int GameWrite(User user) throws IOException{
    	ByteBuffer buf = User.bufout;
    	buf.flip();
    	int byteswrite = user.sc.write(buf);
		System.out.println("Write " + byteswrite);
		//System.out.println("Write over!");
		buf.clear();
		return byteswrite;
    }
    
    //ͨ��socketchannel�ҵ���Ӧ��User
    static User FindUser(SocketChannel sc){
    	for (Iterator<User> user = Server.UserList.iterator(); user.hasNext(); ){
    		if(user.hashCode() == sc.hashCode() )
    			return (User) user;
    	}
    	return null;
    }
}
