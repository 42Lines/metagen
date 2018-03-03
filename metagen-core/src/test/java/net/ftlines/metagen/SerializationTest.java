/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package net.ftlines.metagen;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;

import junit.framework.Assert;

public class SerializationTest
{
	@Test
	public void serialization() throws Exception
	{
		SingularProperty<SomeBean, Integer> prop = new SingularProperty<SomeBean, Integer>("value", SomeBean.class,
			"value", true, "getValue", true, "setValue", true);

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
