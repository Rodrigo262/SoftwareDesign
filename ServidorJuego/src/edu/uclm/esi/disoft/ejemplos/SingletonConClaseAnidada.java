package edu.uclm.esi.disoft.ejemplos;

public class SingletonConClaseAnidada {
	private static SingletonConClaseAnidada yo;
	
	private int numeroDeInstancias;
	
	private SingletonConClaseAnidada() {
		numeroDeInstancias++;
		System.out.println("Creado un Singleton");
	}
	
	private static class SingletonHolder {
		static SingletonConClaseAnidada singleton=new SingletonConClaseAnidada();
	}
	
	public static SingletonConClaseAnidada get() {
		return SingletonHolder.singleton;
	}
	
	public int getNumeroDeInstancias() {
		return numeroDeInstancias;
	}
}
