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

import net.ftlines.metagen.Path;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * A column that displays the property pointed to by {@link Path} in a label
 * 
 * @author igor
 * 
 * @param <R>
 */
public class MetaTextColumn<R, S> extends AbstractMetaColumn<R, Object, S>
{

	public MetaTextColumn(IModel<String> displayModel, Path<R, ? extends Object> path, S sortProperty)
	{
		super(displayModel, path, sortProperty);
	}

	public MetaTextColumn(IModel<String> displayModel, Path<R, ? extends Object> path)
	{
		super(displayModel, path);
	}

	public MetaTextColumn(String header, Path<R, ? extends Object> path, S sortProperty)
	{
		super(header, path, sortProperty);
	}

	public MetaTextColumn(String header, Path<R, ? extends Object> path)
	{
		super(header, path);
	}

	@Override
	public final void internalPopulateItem(Item<ICellPopulator<R>> cellItem, String componentId,
		IModel<Object> cellModel)
	{
		cellItem.add(new Label(componentId, cellModel));
	}

	/**
	 * Factory method
	 * 
	 * @param label
	 * @param path
	 * @param sortData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <R,S> MetaTextColumn<R,S> of(IModel<String> label, Path<R, ?> path, S sortData)
	{
		return new MetaTextColumn<R,S>(label, (Path<R, Object>)path, sortData);
	}

	/**
	 * Factory method
	 * 
	 * @param label
	 * @param path
	 * @param sortData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <R,S> MetaTextColumn<R,S> of(IModel<String> label, Path<R, ?> path)
	{
		return new MetaTextColumn<R,S>(label, (Path<R, Object>)path);
	}

	/**
	 * Factory method
	 * 
	 * @param label
	 * @param path
	 * @param sortData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <R,S> MetaTextColumn<R,S> of(String label, Path<R, ?> path, S sortData)
	{
		return new MetaTextColumn<R,S>(label, (Path<R, Object>)path, sortData);
	}

	/**
	 * Factory method
	 * 
	 * @param label
	 * @param path
	 * @param sortData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <R,S> MetaTextColumn<R,S> of(String label, Path<R, ?> path)
	{
		return new MetaTextColumn<R,S>(label, (Path<R, Object>)path);
	}

}
