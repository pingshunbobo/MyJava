/*
 * ����IO����
 * 
 * */

package study;

import java.io.*;
public class test {
	static BufferedReader br = new BufferedReader(new 
            InputStreamReader(System.in));
	public static void main(String [] args) throws IOException
	{
		System.out.println(args[0] + args.length);
		System.out.println("Input charact to UpperCase:");
		while(true){
			String str = br.readLine();
			System.out.println (str.toUpperCase());
			System.out.println (String.valueOf(10086));
		}
	}
}
