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

import java.util.Stack;

import net.ftlines.metagen.processor.tree.AbstractBean;
import net.ftlines.metagen.processor.tree.BeanSpace;

public class TrimmingVisitor extends BeanVisitorAdapter {

	private BeanSpace space;
	private Stack<AbstractBean> beans = new Stack<AbstractBean>();

	@Override
	public void enterBeanSpace(BeanSpace space) {
		this.space = space;
	}

	@Override
	protected void enterBean(AbstractBean bean) {
		beans.push(bean);
	}

	@Override
	protected void exitBean(AbstractBean bean) {
		beans.pop();

		if (bean.getProperties().isEmpty() == false) {
			return;
		}
		if (bean.getNestedBeans().isEmpty() == false) {
			return;
		}

		// remove this bean because its empty

		if (beans.isEmpty()) {
			// top level bean
			space.remove(bean.getElement());
		} else {
			// nested bean
			beans.peek().getNestedBeans().remove(bean.getElement());
		}
	}

}
