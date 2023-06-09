package gg.botbuilder.func;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface AppFunctionArgument {
    String description();
    APP_VARIABLE_TYPE type() default APP_VARIABLE_TYPE.IMPLICIT;
}
