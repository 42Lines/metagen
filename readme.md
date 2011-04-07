MetaGen is an annotation processor that generates metadata about classes.

Currently it only generates property meta information that can be used to construct
refactor safe property expressions.

For example, using metagen-wicket module the following code that depends on strings:

    IModel<Person> person=getPersonModel();
    IModel<String> street=new PropertyModel<Street>(person, "address.street");

can be replaced with the following:

    IModel<Person> person=getPersonModel();
    IModel<String> street=MetaModel.of(person).get(PersonMeta.address).get(AddressMeta.street);

although the code is a more verbose then its string alternative it will generate
a compile time error should any properties change instead of failing at runtime
like its more concise string alternative.

The processor supports the following triggers that will generate meta classes:
* A class is annotated with a JPA annotaiton, eg @Entity or @MappedSuperClass
* A class is annotated with @Bean annotation
* A field or getter is annotated with @Property annotation

The annotation processor is still in early stages of development

TODO
*	The code is messy, especially with regard to ElementExt and friends. Clean it up.
*	Need unit tests for current feature set
*	Meta classes do not properly follow hierarchy (do not extend each other where appropriate)
*	Meta classes do not support inner classes

