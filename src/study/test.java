/*
 * »ù±¾IO·¶Àý
 * 
 * */

package study;

import java.io.*;
public class test {
	static BufferedReader br = new BufferedReader(new 
            InputStreamReader(System.in));
	public static void main(String [] args) throws IOException
	{
		String str;
		System.out.println("Input charact to UpperCase:");
		while(true){
			str = br.readLine();
			System.out.println (str.toUpperCase());
		}
	}
}
