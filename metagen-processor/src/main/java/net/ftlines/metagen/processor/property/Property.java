package net.ftlines.metagen.processor.property;

import java.io.IOException;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import net.ftlines.metagen.processor.Constants;
import net.ftlines.metagen.processor.model.QualifiedName;
import net.ftlines.metagen.processor.model.TypeResolver;
import net.ftlines.metagen.processor.model.Visibility;
import net.ftlines.metagen.processor.util.SourceWriter;


public class Property {
	private final TypeElement container;
	private final String name;
	private final TypeMirror type;
	private Visibility visibility;

	public Property(TypeElement container, Visibility visibility,
			TypeMirror type, String name) {
		this.name = name;
		this.type = type;
		this.container = container;
		this.visibility = visibility;
	}

	public void generateSource(SourceWriter writer) throws IOException {
		String type = getTypeDeclaration();
		Visibility visibility = Visibility.max(this.visibility,
				Visibility.DEFAULT);

		writer.line("%s static final %s<%s,%s> %s = new %s(\"%s\");",
				visibility.getKeyword(), Constants.SINGULAR, getContainerName()
						.getQualified(), type, name, Constants.SINGULAR, name);
	}

	private String getTypeDeclaration() {
		return type.accept(new TypeResolver(), null);
	}

	public TypeElement getContainer() {
		return container;
	}

	private QualifiedName getContainerName() {
		return new QualifiedName(getContainer());
	}

	public String getName() {
		return name;
	}

	public Visibility getVisibility() {
		return visibility;
	}

	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}

	public TypeMirror getType() {
		return type;
	}

}
