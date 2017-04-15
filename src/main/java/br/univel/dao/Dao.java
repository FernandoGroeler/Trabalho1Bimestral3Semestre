package br.univel.dao;

public class Dao {
	private Class<? extends Object> cl;
	
	public Dao(Object o) {
		cl = o.getClass();
	}
	

}
