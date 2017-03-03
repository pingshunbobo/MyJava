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

public class Conn {
	SocketChannel sc = null;
	SocketAddress sa = null;
	ByteBuffer bufin = null;
	ByteBuffer bufout = null;
	ConnStatus status = null;

	//初始化一个连接结构。
	public Conn(Socket sock) {
		sa = sock.getRemoteSocketAddress();
    	bufin = ByteBuffer.allocate(1024);
    	bufout = ByteBuffer.allocate(1024);
    	
    	status = ConnStatus.READING;
    	
    	try {
        	this.sc = sock.getChannel();
			this.sc.configureBlocking(false);
		    this.ReadRegister();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
    	System.out.print("New User @");
	    System.out.println(sock.getRemoteSocketAddress().toString());
	}
	
	//读取数据到用户空间。
	int ConnRead(){
    	int ReadBytes = 0;
		ByteBuffer buf = this.bufin;
		
		try {
			ReadBytes = sc.read(buf);
			System.out.println("read " + ReadBytes); 
		} catch (IOException e) {
			//e.printStackTrace();
		}
		//判断返回值，注册事件
		if( ReadBytes > 0 ){
			System.out.println(ReadBytes + "Notice!");
			Server.NoticeProcesser(this);
		} else if(ReadBytes < 0){
			this.ConnClose();
		}else{
			//Do nothing!
		}
		return ReadBytes;
    }
	
	//输出数据到socket。
    int ConnWrite(){
    	int bytewrites = 0;
    	
    	ByteBuffer buf = this.bufout;
    	buf.flip();
		try {
			bytewrites = this.sc.write(buf);
			ReadRegister();
		} catch (IOException e) {
			this.ConnClose();
			e.printStackTrace();
		}
		System.out.println("Write " + bytewrites);
		buf.clear();
		return bytewrites;
    }
    
    //关闭Socket连接。
    void ConnClose() {
		try {
			this.CancelRegister();
			this.sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void DataProcess(){
		//如果数据未满，返回继续读！
		if(bufin.position() < 6){
			return;
		} else if(bufin.position() >= 6){
			this.DataEcho();
		} else{
			this.DataError();
		}
		this.WriteRegister();
		System.out.println("Write Register!");
	}
	
	//简单把输入复制到输出。
	private void DataEcho(){
		ByteBuffer buf = bufin;
		buf.flip();				//将buf内容做屏幕输出。
		while(buf.hasRemaining()){
			this.bufout.put(buf.get());
		}
		buf.clear();
	}
	
	private void DataError(){
		this.bufin.clear();
		this.bufout.put("Error\r\n".getBytes());
	}
	
	public void ReadRegister(){
		try{
		    this.sc.register(Server.selector, SelectionKey.OP_READ);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void WriteRegister(){
		try{
		    this.sc.register(Server.selector, SelectionKey.OP_WRITE);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void CancelRegister(){
		this.sc.keyFor(Server.selector).cancel();
	}
	
	private enum ConnStatus{
		READING,PROCESSING,WRITEING,ERROR
	}
}
