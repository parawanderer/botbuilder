package gg.botbuilder.func;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AppFunction {
    enum TYPE {
        DEFAULT,
        MODULE;
    }
    String name();
    String description();
    TYPE type() default TYPE.DEFAULT;
    String moduleName() default "";
}
