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
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Base class for columns where cell model is based on a {@link Path} expression relative to the row
 * model.
 * 
 * @author igor
 * 
 * @param <R>
 * @param <T>
 */
public abstract class AbstractMetaColumn<R, T> extends AbstractColumn<R>
{
	private final Path<R, ? extends T> path;

	/**
	 * Constructor
	 * 
	 * @param displayModel
	 * @param path
	 * @param sortProperty
	 */
	public AbstractMetaColumn(IModel<String> displayModel, Path<R, ? extends T> path, String sortProperty)
	{
		super(displayModel, sortProperty);
		this.path = path;
	}

	/**
	 * Constructor
	 * 
	 * @param displayModel
	 * @param path
	 */
	public AbstractMetaColumn(IModel<String> displayModel, Path<R, ? extends T> path)
	{
		super(displayModel);
		this.path = path;
	}

	@Override
	public final void populateItem(Item<ICellPopulator<R>> cellItem, String componentId, IModel<R> rowModel)
	{
		internalPopulateItem(cellItem, componentId, new PropertyModel<T>(rowModel, path.expression()));
	}

	/**
	 * Same as {@link #populateItem(Item, String, IModel)} but with the model parameter pointing to
	 * the property of the root object specified by the {@link Path} instead of the root object
	 * itself.
	 * 
	 * @param cellItem
	 * @param componentId
	 * @param cellModel
	 */
	public abstract void internalPopulateItem(Item<ICellPopulator<R>> cellItem, String componentId, IModel<T> cellModel);

	/**
	 * Gets the property path this column is bound to
	 * 
	 * @return
	 */
	public final Path<R, ? extends T> getPath()
	{
		return path;
	}


}
