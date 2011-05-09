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


/**
 * Builds a compile-time-safe property path expression using metagen's metadata. This object is
 * immutable, therefore any operations such as {@link #get(SingularProperty)} return a new instance.
 * 
 * @author igor
 * 
 * @param R
 *            type of root object
 * @param T
 *            type of property this path is pointing to, eg evaluation this path against an instance
 *            of {@code R} yields an instance of {@code T}
 */
public class Path<R, T>
{
	private static final String SEPARATOR = ".";
	private final String path;

	private Path()
	{
		this("");
	}

	private Path(String path)
	{
		this.path = path;
	}


	/**
	 * Retrieves the property expression of this path
	 * 
	 * @return expression, empty string if path points to the root object
	 */
	public String expression()
	{
		return path;
	}

	/**
	 * Extends the path to point to the specified {@code property}
	 * 
	 * @param property
	 * @param <V>
	 *            type of property
	 * @return
	 */
	public <V> Path<R, V> get(SingularProperty<? super T, V> property)
	{
		return new Path<R, V>(path + ((path.length() > 0) ? SEPARATOR : "") + property.getName());
	}

	/**
	 * Factory method for the root path instance.
	 * 
	 * Notice the path does not keep a reference to the {@code clazz} parameter, it is only here to
	 * help with type inference.
	 * 
	 * @param <V>
	 * @param clazz
	 * @return instance of path that points to the root object
	 */
	public static <V> Path<V, V> of(Class<? extends V> clazz)
	{
		return new Path<V, V>();
	}

	/**
	 * Factory method for the root path instance.
	 * 
	 * Notice the path does not keep a reference to the {@code object} parameter, it is only here to
	 * help with type inference.
	 * 
	 * @param <V>
	 * @param object
	 * @return instance of path that points to the root object
	 */
	public static <V> Path<V, V> of(V object)
	{
		return new Path<V, V>();
	}
}
