package net.ftlines.metagen.processor.inner;

import net.ftlines.metagen.annot.Bean;

public class BeanStartingWithGap {

	public class BeanWithNoProps {

		public class AnotherBeanWithNoProps {

			@Bean
			public class BeanWithProp {
				public int int2;
			}

		}

	}

}
