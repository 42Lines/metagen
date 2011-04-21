/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.ftlines.metagen.processor.tree.visitor;

import net.ftlines.metagen.processor.tree.AbstractBean;
import net.ftlines.metagen.processor.tree.BeanSpace;
import net.ftlines.metagen.processor.tree.NestedBean;
import net.ftlines.metagen.processor.tree.Property;
import net.ftlines.metagen.processor.tree.TopLevelBean;
import net.ftlines.metagen.processor.tree.Visitor;

public abstract class BeanVisitorAdapter implements Visitor {

	protected abstract void enterBean(AbstractBean bean);

	protected abstract void exitBean(AbstractBean bean);

	@Override
	public void enterBeanSpace(BeanSpace space) {
	}

	@Override
	public void exitBeanSpace(BeanSpace space) {
	}

	@Override
	public void enterTopLevelBean(TopLevelBean node) {
		enterBean(node);
	}

	@Override
	public void exitTopLevel(TopLevelBean node) {
		exitBean(node);
	}

	@Override
	public void enterNestedBean(NestedBean node) {
		enterBean(node);
	}

	@Override
	public void exitNestedBean(NestedBean node) {
		exitBean(node);
	}

	@Override
	public void enterProperty(Property node) {
	}

	@Override
	public void exitProperty(Property node) {
	}

}
