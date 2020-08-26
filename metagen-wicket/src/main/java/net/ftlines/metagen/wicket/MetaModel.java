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

package net.ftlines.metagen.wicket;

import net.ftlines.metagen.SingularProperty;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;


/**
 * Uses binder's metamodel to build refactor-safe property expressions.
 * 
 * Here is an example of retrieving user's first name from the user object:
 * 
 * <pre>
 * IModel&lt;User&gt; user;
 * IModel&lt;String&gt; name = new MetaModel&lt;User&gt;(user).get(User_.profile).get(Profile_.firstName);
 * </pre>
 * 
 * @author igor
 * 
 * @param <T>
 */
public class MetaModel<T> extends PropertyModel<T>
{

	/**
	 * Constructor
	 * 
	 * @param root
	 *            root model object
	 */

	public MetaModel(IModel root)
	{
		super(root, null);
	}

	public MetaModel(MetaModelWrappable root)
	{
		super(root, null);
	}

	public MetaModel(org.apache.wicket.Component root)
	{
		super(root, null);
	}


	private MetaModel(IModel root, String path)
	{
		super(root, path);
	}

	private MetaModel(MetaModelWrappable root, String path)
	{
		super(root, path);
	}

	private MetaModel(org.apache.wicket.Component root, String path)
	{
		super(root, path);
	}
	/**
	 * Assigns a default value to this model that will be returned instead of {@code null}
	 * 
	 * @param defaultValue
	 * @return
	 */
	public MetaModel<T> withDefault(T defaultValue)
	{
		// Args.notNull(defaultValue, "defaultValue");
		return new DefaultModel<T>(this, new ValueModel<T>(defaultValue));
	}

	/**
	 * Assigns a default value to this model that will be returned instead of {@code null}
	 * 
	 * @param defaultValue
	 * @return
	 */
	public MetaModel<T> withDefault(IModel<T> defaultValue)
	{
		// Args.notNull(defaultValue, "defaultValue");
		return new DefaultModel<T>(this, defaultValue);
	}

	/**
	 * Creates a new instance of {@link MetaModel} that points to the specified attribute of the
	 * model it was called on
	 * 
	 * @param <V>
	 * @param attribute
	 * @return
	 */
	public <V> MetaModel<V> get(SingularProperty<? super T, V> attribute)
	{
		return new MetaModel<V>(this, attribute.getName());
	}

	/**
	 * Convenience factory to cut down on generics noise
	 * 
	 * @param <T>
	 * @param source
	 * @return
	 */
	public static <T> MetaModel<T> of(IModel<T> source)
	{
		return new MetaModel<T>(source);
	}

	public static <T extends MetaModelWrappable> MetaModel<T> of(T source)
	{
		return new MetaModel<T>(source);
	}

	public static <T extends org.apache.wicket.Component> MetaModel<T> of(T source)
	{
		return new MetaModel<T>(source);
	}

	/**
	 * Path model with a default value
	 * 
	 * @author igor
	 * 
	 * @param <T>
	 */
	public static class DefaultModel<T> extends MetaModel<T>
	{
		private final IModel<T> defaultValue;

		public DefaultModel(IModel<T> model, IModel<T> defaultValue)
		{
			super(model);
			this.defaultValue = defaultValue;
		}

		@Override
		public T getObject()
		{
			T value = super.getObject();
			return (value != null) ? value : defaultValue.getObject();
		}

		@Override
		public void detach()
		{
			super.detach();
			defaultValue.detach();
		}
	}

	/**
	 * Doesnt have the serializable type restrictions of {@link Model}
	 * 
	 * @author igor
	 * 
	 */
	private static class ValueModel<V> implements IModel<V>
	{
		private V value;

		public ValueModel(V value)
		{
			this.value = value;
		}

		@Override
		public void detach()
		{
		}

		@Override
		public V getObject()
		{
			return value;
		}

		@Override
		public void setObject(V object)
		{
			value = object;
		}

	}
}
