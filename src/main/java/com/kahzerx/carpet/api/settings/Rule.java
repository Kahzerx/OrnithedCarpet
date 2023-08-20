package com.kahzerx.carpet.api.settings;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Rule {
	String name() default "";
	String desc();
	String[] extra() default {};
	String[] categories();
	String[] options() default {};
	boolean strict() default true;
	String appSource() default "";
	Class<? extends Validator>[] validators() default {};
	Class<? extends Condition>[] conditions() default {};

	interface Condition {
		boolean shouldRegister();
	}
}
