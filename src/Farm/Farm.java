/*
 * ¼Ì³Ð·¶Àý
 * 
 * */

package Farm;

public class Farm {
	public static void main(String [] args){
	
		System.out.println("There is a animal:");
		Aanimal();
		
		System.out.println();
		
		System.out.println("There is a dog:");
		Adog();
	}
	
	public static void Aanimal(){
		Animal an1 = new Animal("xiaoming");
		an1.Grew(3);
		an1.SayName();
		an1.SayAge();
	}
	
	public static void Adog(){
		Dog dg1 = new Dog("huang");
		dg1.Grew(2);
		dg1.SayName();
		dg1.SayAge();
		dg1.test();
		dg1.test("hello!");
	}
}

class Animal {
	public static int age;
	public static String name;
	
	public Animal(String Name){
		name = Name;
		age = 1;
	}
	
	public void SayAge(){
		System.out.println("I have "+age+" years old!");
	}
	
	public void SayName(){
		System.out.println("My name is "+name);
	}
	
	public void Grew(int num){
		age += num;
	}
	
	public void test(){
		System.out.println("args: null test.");
	}
	
	public void test(String str1){
		System.out.printf("args: %s test.",str1);
	}
	
}


class Dog extends Animal{
	public Dog(String Name) {
		super(Name);
		// TODO Auto-generated constructor stub
	}
	
	public void SayName(){
		System.out.print("I am a dog,");
		super.SayName();
	}
}