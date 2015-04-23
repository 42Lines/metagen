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

import java.util.Collections;
import java.util.Iterator;

/**
 * Represents an optional value.
 * <p>
 * This class aids in making handling of possible {@code null} values (whether return values or
 * parameter values) explicit and hopefully reduces the number of {@link NullPointerException}s that
 * will be thrown.
 * </p>
 * <p>
 * It also makes tracking down NPEs easier by not allowing {@code null} return values to be
 * propagated as parameters to further function calls, it does it by making {@link #get()} throw an
 * NPE if called on an {@link Optional} constructed with a {@code null} value.
 * </p>
 * <p>
 * By supporting {@link Iterable} this class allows the developer to rewrite code like this: <code>
 * if (optional.isSet()) {
 *    // do something
 * }
 * </code> With the following: <code>
 * for (optional) {
 *   // do something
 * }
 * </code> which some developers find easier to read.
 * </p>
 * 
 * @author igor
 * @param <T> type of optional value
 */
public final class Optional<T> implements Iterable<T>
{
	private static final Optional<Void> NULL = new Optional<Void>(null);
	private static final Iterator<?> EMPTY_ITERATOR = Collections.emptyList().iterator();

	private final T value;

	private Optional(T value)
	{
		this.value = value;
	}

	/**
	 * Gets the stored value or throws {@link NullPointerException} if the value is {@code null}
	 * 
	 * @throws NullPointerException
	 *             if the value is {@code null}
	 * 
	 * @return stored value
	 */
	public T get()
	{
		if (value == null)
		{
			throw new NullPointerException();
		}
		return value;
	}

	/**
	 * Gets the stored value or returns {@code defaultValue} if value is {@code null}
	 * 
	 * @return stored value or {@code defaultValue}
	 */
	public T get(T defaultValue)
	{
		if (value == null)
		{
			return defaultValue;
		}
		return value;
	}

	/**
	 * @return {code true} if the store value is {@code null}
	 */
	public boolean isNull()
	{
		return value == null;
	}

	/**
	 * @return {code true} if the store value is not {@code null}
	 */
	public boolean isNotNull()
	{
		return value != null;
	}

	/**
	 * @return {code true} if the store value is not {@code null}
	 */
	public boolean isSet()
	{
		return value != null;
	}

	/**
	 * @return {code true} if the store value is {@code null}
	 */
	public boolean isNotSet()
	{
		return value == null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Iterator<T> iterator()
	{
		if (value == null)
		{
			return (Iterator<T>)EMPTY_ITERATOR;
		}
		else
		{
			return Collections.singleton(value).iterator();
		}
	}

	/**
	 * Factory method for creating {@link Optional} values
	 * 
	 * @param <T> type of optional value
	 * @param value value to store inside optional
	 * @return optional that represents the specified value
	 */
	@SuppressWarnings("unchecked")
	public static <T> Optional<T> of(T value)
	{
		if (value == null)
		{
			return (Optional<T>)NULL;
		}
		else
		{
			return new Optional<T>(value);
		}
	}

	/**
	 * Factory method for creating an {@link Optional} value that represents a {@code null}
	 * 
	 * @param <Z> type of optional value
	 * @return optional that represents a {@code null}
	 */
	@SuppressWarnings("unchecked")
	public static <Z> Optional<Z> ofNull()
	{
		return (Optional<Z>)NULL;
	}
}