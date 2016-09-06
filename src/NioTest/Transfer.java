package NioTest;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class Transfer {
	public static void main(String [] args) throws IOException{
		transferFrom("data/file1","data/file2");
		//transferTo("file1","file2");
	}
	
	static void transferFrom(String fromfile,String tofile) throws IOException{
		RandomAccessFile fromFile = new RandomAccessFile(fromfile, "rw");
		FileChannel      fromChannel = fromFile.getChannel();

		RandomAccessFile toFile = new RandomAccessFile(tofile, "rw");
		FileChannel      toChannel = toFile.getChannel();

		long position = 0;
		long count = fromChannel.size();

		toChannel.transferFrom(fromChannel, count, position);
	}
	static void transferTo(String fromfile,String tofile) throws IOException{
		RandomAccessFile fromFile = new RandomAccessFile(fromfile, "rw");
		FileChannel      fromChannel = fromFile.getChannel();

		RandomAccessFile toFile = new RandomAccessFile(tofile, "rw");
		FileChannel      toChannel = toFile.getChannel();

		long position = 0;
		long count = fromChannel.size();

		fromChannel.transferTo(position, count, toChannel);
	}
}
