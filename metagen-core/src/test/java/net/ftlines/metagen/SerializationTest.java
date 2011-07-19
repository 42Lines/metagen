package net.ftlines.metagen;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.Assert;

import org.junit.Test;

public class SerializationTest
{
	@Test
	public void serialization() throws Exception
	{
		SingularProperty<SomeBean, Integer> prop = new SingularProperty<SomeBean, Integer>("value", SomeBean.class,
			"value", "getValue", "setValue");

		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(bytesOut);
		out.writeObject(prop);
		out.close();
		ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytesOut.toByteArray());
		ObjectInputStream in = new ObjectInputStream(bytesIn);
		SingularProperty<SomeBean, Integer> prop2 = (SingularProperty<SomeBean, Integer>)in.readObject();

		Assert.assertEquals(prop.getName(), prop2.getName());
		Assert.assertEquals(prop.getField(), prop2.getField());
		Assert.assertEquals(prop.getGetter(), prop2.getGetter());
		Assert.assertEquals(prop.getSetter(), prop2.getSetter());
		
		
	}
	

}
