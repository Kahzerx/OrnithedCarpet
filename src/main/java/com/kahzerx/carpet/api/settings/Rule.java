package com.kahzerx.carpet.api.settings;

public @interface Rule {
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
