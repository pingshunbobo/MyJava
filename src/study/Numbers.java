package study;

public class Numbers {
	public static void main(String [] args){
		int a = 1234567890;
		System.out.println(Integer.toString(a));

		char[] helloArray = { 'h', 'e', 'l', 'l', 'o', '.'};
		String helloString = new String(helloArray);
		System.out.println( helloString );
		
		String StrNum1 = "1234";
		String StrNum2 = "6789";
		int sum = Integer.parseInt(StrNum1) + Integer.parseInt(StrNum2);
		System.out.print("num1" + "  +  " + "num2 = ");
		System.out.println(sum);
	}
}
