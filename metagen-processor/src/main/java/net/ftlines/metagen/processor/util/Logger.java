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
package net.ftlines.metagen.processor.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger
{
	private static final boolean ENABLED = true;


	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private final String name;
	
	public Logger(Class<?> name)
	{
		this.name = name.getSimpleName();
	}

	public void log(String message, Object... params)
	{
		if (!ENABLED)
			return;

		try
		{
			File temp=File.createTempFile("metagen", "log");
			FileOutputStream out=new FileOutputStream(temp, true);
			PrintWriter log = new PrintWriter(out);
			log.print(dateFormat.format(new Date()));
			log.print(" ");
			log.print(name);
			log.print(" ");
			log.println(String.format(message, params));
			log.flush();
			log.close();
			out.flush();
			out.close();
		}
		catch (IOException e)
		{
			throw new RuntimeException("Logger error", e);
		}
	}

}
