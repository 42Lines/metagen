package net.ftlines.metagen.processor.model;

import javax.lang.model.element.TypeElement;

public class QualifiedName {
	private String value;

	public QualifiedName(TypeElement e) {
		this.value = e.getQualifiedName().toString();
	}

	public QualifiedName(String value) {
		this.value = value;
	}

	public String getLocal() {
		int dot = value.lastIndexOf('.');
		return value.substring(dot + 1);
	}

	public String getNamespace() {
		int dot = value.lastIndexOf('.');
		return value.substring(0, dot);
	}

	public String getQualified() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QualifiedName other = (QualifiedName) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
