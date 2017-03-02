/*
 * use hashmap storage user RemoteAddress -> data.
 * 
 * */

package GameServer;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class User {
	static SocketChannel sc = null;
	static SocketAddress sa = null;
	static ByteBuffer bufin = null;
	static ByteBuffer bufout = null;
	UserStatus status = UserStatus.READING;

	public User(Socket s) {
		sa = s.getRemoteSocketAddress();
    	bufin = ByteBuffer.allocate(1024);
    	bufout = ByteBuffer.allocate(1024);
    	
    	status = UserStatus.READING;
    	
    	SocketChannelOpen(s);
    	System.out.print("New User @");
	    System.out.println(s.getRemoteSocketAddress().toString());
	}
	//读取数据到用户空间。
	int SocketRead(){
    	int ReadBytes = 0;
		ByteBuffer buf = User.bufin;
		
		try {
			ReadBytes = sc.read(buf);
			System.out.println("read " + ReadBytes); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		//判断返回值，注册事件
		if( ReadBytes >= 0 ){
			System.out.println("Notice!");
			Server.NoticeProcesser(this);
		} else{
			CancelRegister();
			SocketClose();
		}
		return ReadBytes;
    }
	
	//输出数据到socket。
    int SocketWrite(){
    	int bytewrites = 0;
    	
    	ByteBuffer buf = User.bufout;
    	buf.flip();
		try {
			bytewrites = sc.write(buf);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Write " + bytewrites);
		buf.clear();
		ReadRegister();
		return bytewrites;
    }
    
    //关闭Socket连接。
    void SocketClose() {
		try {
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void DataProcess(){
		//如果数据未满，返回继续读！
		if(bufin.position() < 6){
			return;
		} else if(bufin.position() >= 6){
			DataEcho();
		} else{
			DataError();
		}
		WriteRegister();
		System.out.println("Write Register!");
	}
	
	//简单讲输入复制到输出。
	private static void DataEcho(){
		ByteBuffer buf = bufin;
		buf.flip();				//将buf内容做屏幕输出。
		while(buf.hasRemaining()){
			User.bufout.put(buf.get());
		}
		buf.clear();
	}
	
	private static void DataError(){
		bufin.clear();
		bufout.put("Error\r\n".getBytes());
	}
	
	private void SocketChannelOpen(Socket s){
	    SocketChannel socketch;
	    
		try {
			socketch = SocketChannel.open();
		    socketch = s.getChannel();
		    socketch.configureBlocking(false);
		    sc = socketch;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void ReadRegister(){
		try{
		    sc.register(Server.selector, SelectionKey.OP_READ);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void WriteRegister(){
		try{
		    sc.register(Server.selector, SelectionKey.OP_WRITE);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void CancelRegister(){
		sc.keyFor(Server.selector).cancel();
	}
	
	private enum UserStatus{
		READING,PROCESSING,WRITEING,ERROR
	}
}
