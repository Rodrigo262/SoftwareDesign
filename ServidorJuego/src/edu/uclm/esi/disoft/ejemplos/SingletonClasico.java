package edu.uclm.esi.disoft.ejemplos;

public class SingletonClasico {
	private static SingletonClasico yo;
	
	private int numeroDeInstancias;
	
	private SingletonClasico() {
		numeroDeInstancias++;
		System.out.println("Creado un Singleton");
	}
	
	public static SingletonClasico get() {
		if (yo==null)
			yo=new SingletonClasico();
		return yo;
	}
	
	public int getNumeroDeInstancias() {
		return numeroDeInstancias;
	}
}
