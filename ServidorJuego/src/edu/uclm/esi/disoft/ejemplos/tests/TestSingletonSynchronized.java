package edu.uclm.esi.disoft.ejemplos.tests;

import static org.junit.Assert.*;
import java.util.Vector;
import org.junit.Test;
import edu.uclm.esi.disoft.ejemplos.SingletonSynchronized;

public class TestSingletonSynchronized {

	@Test
	public void testUnHilo() {
		int n=1000;
		Vector<SingletonSynchronized> singletones=new Vector<SingletonSynchronized>();
		for (int i=0; i<n; i++) {
			SingletonSynchronized s=SingletonSynchronized.get();
			singletones.add(s);
		}
		check(singletones);
	}

	@Test
	public void testHilos() {
		int n=100;
		final Vector<SingletonSynchronized> singletones=new Vector<SingletonSynchronized>();
		for (int i=0; i<n; i++) {
			Runnable r=new Runnable() {
				@Override
				public void run() {
					SingletonSynchronized s=SingletonSynchronized.get();
					singletones.add(s);
				}
			};
			new Thread(r).start();
			if (i==99)
				check(singletones);
		}
	}

	private void check(Vector objetos) {
		for (int i=0; i<objetos.size(); i++) {
			for (int j=i+1; j<objetos.size(); j++) {
				assertSame("No coinciden el " + i + " con el " + j, 
						objetos.get(i), objetos.get(j));
			}
		}
	}
}
