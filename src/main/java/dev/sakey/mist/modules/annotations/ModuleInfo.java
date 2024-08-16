package dev.sakey.mist.modules.annotations;

import dev.sakey.mist.modules.Category;
import org.lwjgl.input.Keyboard;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
public @interface ModuleInfo {
	String name();

	String description() default "";

	int key() default Keyboard.KEY_NONE;

	Category category();

	boolean enabledByDefault() default false;

	boolean hiddenInArrayList() default false;
}
