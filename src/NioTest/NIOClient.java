/*
 * https://github.com/wojiushimogui/Selector
 * */

package NioTest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOClient {
		
	private Selector selector;
	
	public NIOClient() throws IOException {
		this.selector =Selector.open();
	}

	private void init(String address,int port) throws IOException{
		//�ͻ��ˣ�������һ��SocketChannel
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);//����ͨ������Ϊ������ģʽ
		//����
		socketChannel.connect(new InetSocketAddress(address,port));
		
		//��SocketChannelע�ᵽselector�У���Ϊ��ͨ��ע��SelectionKey.OP_CONNECT
		socketChannel.register(selector, SelectionKey.OP_CONNECT);
		
		
	}

	public static void main(String[] args) throws IOException {
		NIOClient client = new NIOClient();
		client.init("localhost",9999);
		client.connect();

	}

	private void connect() throws IOException {
		int data = 1;
		while(true){
			selector.select();//
			Set<SelectionKey> set = selector.selectedKeys();
			Iterator<SelectionKey> ite = set.iterator();
			
			while(ite.hasNext()){
				SelectionKey selectionKey = (SelectionKey) ite.next();
				ite.remove(); //ɾ����ѡ��key,�Է��ظ�����
				if(selectionKey.isConnectable()){//���Ƿ������ӷ���
					SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
					//����������ӣ����������
					if(socketChannel.isConnectionPending()){
						socketChannel.finishConnect();
					}
					socketChannel.configureBlocking(false);//����Ϊ������ģʽ
					//���������˷�������
					System.out.println("�ͻ����������˷������ˡ�������");

					socketChannel.write(ByteBuffer.wrap(new String("hello server!").getBytes()));
					//Ϊ�˽������Է������˵����ݣ�����ͨ��ע�ᵽѡ������
					socketChannel.register(selector, SelectionKey.OP_READ);
				}
				else if(selectionKey.isReadable()){
					SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
					//���������ڷ������˷��͹���������
					ByteBuffer buf = ByteBuffer.allocate(128);
					socketChannel.read(buf);
					byte[] receData = buf.array();
            		String msg = new String(receData).trim();
            		System.out.println("接收来自服务器端的数据为："+msg);
            		buf.clear();
                	socketChannel.write(ByteBuffer.wrap(new String(data+"").getBytes()));
                	try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					data++;
				}						
			}		
		}	
	}

}
