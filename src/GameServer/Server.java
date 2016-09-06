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
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server {
    public static void main(String[] args)
		throws IOException
    {
    	//�������п���һ��ѡ���������ڼ������¼���
    	Selector selector = Selector.open();
        ServerSocketChannel server = ServerSocketChannel.open();  
        server.configureBlocking(false);
  
        //��ͨ����ָ���˿�  
        ServerSocket socket = server.socket();  
        InetSocketAddress address = new InetSocketAddress(8088);
        socket.bind(address);
        
        //��Selector��ע������¼�  
        server.register(selector, SelectionKey.OP_ACCEPT);
        
		while(true){
			int readyChannels = selector.select();
			if(0 == readyChannels)
				continue;
			Set <SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator <SelectionKey> keyIterator = selectedKeys.iterator();
			while(keyIterator.hasNext()) {
				SelectionKey key = keyIterator.next();
				if(key.isAcceptable()) {
					System.out.println("isAcceptable"); 

					Socket s = socket.accept();
					// ÿ���ͻ������Ӻ�����һ��ServerThread�߳�Ϊ�ÿͻ��˷���
					Thread th1 = new Thread(new ServerThread(s));
					th1.start();
				    	
				    //����socket Channel��
				    SocketChannel socketch = SocketChannel.open(); 
				    socketch = s.getChannel();
				    socketch.configureBlocking(false);
				    socketch.register(selector, SelectionKey.OP_READ);

				} else if (key.isConnectable()) {
					System.out.println("�����һ�㲻���õ�");
					// a connection was established with a remote server.
				} else if (key.isReadable()) {
					//System.out.println("isReadable");
				    	
					// a channel is ready for reading
				} else if (key.isWritable()) {
					System.out.println("isWritable");
				        // a channel is ready for writing
				}
				keyIterator.remove();
			}
		}
	}//end main function.   
    
    static void Gameread(Socket s){
    	s.getChannel();
    }
    
    static void GameWrite(Socket s){
    	s.getChannel();
    }
}
