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
    	//�������п���һ��ѡ���������ڼ������¼���
    	Selector selector = Selector.open();
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
				    	
				    //����socket Channel��
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
			
			//��buf��������Ļ�����
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
