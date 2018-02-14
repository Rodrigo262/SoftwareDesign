package edu.uclm.esi.disoft.ejemplos;

public class SingletonSynchronized {
	private static SingletonSynchronized yo;
	
	private int numeroDeInstancias;
	
	private SingletonSynchronized() {
		numeroDeInstancias++;
		System.out.println("Creado un Singleton");
	}
	
	public static SingletonSynchronized get() {
		if (yo==null) {
			synchronized (SingletonSynchronized.class) {
				if (yo==null)
					yo=new SingletonSynchronized();				
			}
		}
		return yo;
	}
	
	public int getNumeroDeInstancias() {
		return numeroDeInstancias;
	}
}
