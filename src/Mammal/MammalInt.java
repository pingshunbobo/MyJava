/*
 * ½Ó¿Úimplements·¶Àý¡£
 * 
 * */
package Mammal;

public class MammalInt implements Dog{

	   public void eat(){
	      System.out.println("Mammal eats");
	   }
	   
	   public void travel(){
	      System.out.println("Mammal travels");
	   }

	   public int noOfLegs(){
	      return 0;
	   }
	   
	   public void say(){
		   System.out.println("Say!");
	   }

	   public static void main(String args[]){
	      MammalInt m = new MammalInt();
	      m.eat();
	      m.travel();
	   }
	}
interface Animal {

	   public void eat();
	   public void travel();
}

interface Dog extends Animal{
	public void say();
}