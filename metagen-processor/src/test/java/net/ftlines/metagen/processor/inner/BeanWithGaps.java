package net.ftlines.metagen.processor.inner;

import net.ftlines.metagen.annot.Bean;

@net.ftlines.metagen.annot.Bean
public class BeanWithGaps {
	public int int1;

	public class BeanWithNoProps {

		public class AnotherBeanWithNoProps {

			@Bean
			public class BeanWithProp {
				public int int2;
			}

		}

	}

}
