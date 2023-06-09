package gg.botbuilder.func;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AppFunctionEntryPoint {
    String returnsDescription();
    APP_VARIABLE_TYPE type() default APP_VARIABLE_TYPE.IMPLICIT;
}
