MetaGen is an annotation processor that generates metadata about classes.

#### Features
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

#### Triggers
The processor supports the following triggers that will generate meta classes:
* A class is annotated with a JPA annotaiton, eg @Entity or @MappedSuperClass
* A class is annotated with @Bean annotation
* A field or getter is annotated with @Property annotation

The annotation processor is still in early stages of development

#### Building
Because this is an annotation processor there are some hoops to jump through compared to other projects. Namely, the project has to be installed like this:
    mvn install -Dmaven.test.skip=true
After the above command has been executed normal commands can be used, such as
    mvn eclipse:eclipse

#### Installation
Add the following dependencies into your pom.xml
		<dependency>
			<groupId>net.ftlines.metagen</groupId>
			<artifactId>metagen-core</artifactId>
			<version>${project.version}</version>
		</dependency>
		<dependency>
			<groupId>net.ftlines.metagen</groupId>
			<artifactId>metagen-processor</artifactId>
			<version>${project.version}</version>
		</dependency>

Add the following to your build plugins in your pom.xml
			<plugin>
				<groupId>org.bsc.maven</groupId>
				<artifactId>maven-processor-plugin</artifactId>
				<executions>
					<execution>
						<id>process</id>
						<goals>
							<goal>process</goal>
						</goals>
						<phase>generate-sources</phase>
						<configuration>
							<outputDirectory>target/metamodel</outputDirectory>
						</configuration>
					</execution>
				</executions>
			</plugin>
			<plugin>
				<groupId>org.codehaus.mojo</groupId>
				<artifactId>build-helper-maven-plugin</artifactId>
				<version>1.3</version>
				<executions>
					<execution>
						<id>add-source</id>
						<phase>generate-sources</phase>
						<goals>
							<goal>add-source</goal>
						</goals>
						<configuration>
							<sources>
								<source>target/metamodel</source>
							</sources>
						</configuration>
					</execution>
				</executions>
			</plugin>

#### TODO
*	The code is messy, especially with regard to ElementExt and friends. Clean it up.
*	Need unit tests for current feature set
*	Meta classes do not properly follow hierarchy (do not extend each other where appropriate)
*	Meta classes do not support inner classes

