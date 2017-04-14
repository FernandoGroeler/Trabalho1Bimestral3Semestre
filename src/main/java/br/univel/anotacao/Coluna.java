package br.univel.anotacao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Coluna {
	String nome() default "";
	boolean pk() default false;
	String label() default "";
	int max() default 10;
	boolean obrigatorio() default false;	
}
