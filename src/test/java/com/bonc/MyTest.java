package com.bonc;

import org.junit.Test;

public class MyTest {
	
	@Test
  public void name() {
	Integer a = 5;
	Integer b = 5;
	Integer c = 300;
	Integer d = 300;
	Integer e = new Integer(5);
	Integer f = new Integer(5);
	System.out.println(a==b);
	System.out.println(c==d);
	System.out.println(e==f);
	System.out.println(e==a);
}
}
