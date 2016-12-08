package tests;

import static org.junit.Assert.assertEquals;
import static utils.FileHandler.isNull;

import java.io.File;

import org.junit.Test;

public class isNull {
	
	@Test
	public void isNullTest(){
		assertEquals(isNull(null,null,null),true);
	}
	@Test
	public void nullStrings(){
		String a=null,b=null;
		assertEquals(isNull(a,b),true);
	}
	@Test
	public void fileTest(){
		File    f1=null,
				f2=null,
				f3=null,
				f4=null;
		boolean test = isNull(f1,f2,f3,f4);
		assertEquals("This is null",test,true);
	}
}
