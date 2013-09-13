package org.ftlines.metagen.eclipse.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BeanContainer {
	private List<Bean> beans;

	public void add(Bean bean) {
		if (bean == null) {
			throw new IllegalArgumentException();
		}
		if (beans == null) {
			beans = new ArrayList<Bean>(4);
		}
		beans.add(bean);
	}

	public List<Bean> getBeans() {
		if (beans == null) {
			return Collections.emptyList();
		}
		return beans;
	}
}
