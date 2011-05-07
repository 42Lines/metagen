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

package net.ftlines.metagen.processor.model;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

public enum Visibility
{
	PUBLIC(40, "public"), PROTECTED(30, "protected"), DEFAULT(20, ""), PRIVATE(10, "private");

	private final int rank;
	private final String keyword;

	private Visibility(int rank, String keyword)
	{
		this.rank = rank;
		this.keyword = keyword;
	}

	public String getKeyword()
	{
		return keyword;
	}

	public static Visibility max(Visibility a, Visibility b)
	{
		return (a.rank > b.rank) ? a : b;
	}

	public static Visibility of(Element e)
	{
		if (e.getModifiers().contains(Modifier.PUBLIC))
		{
			return Visibility.PUBLIC;
		}
		else if (e.getModifiers().contains(Modifier.PROTECTED))
		{
			return Visibility.PROTECTED;
		}
		else if (e.getModifiers().contains(Modifier.PRIVATE))
		{
			return Visibility.PRIVATE;
		}
		else
		{
			return Visibility.DEFAULT;
		}
	}

	public boolean sameAs(Element e)
	{
		return equals(of(e));
	}
}
