package gg.botbuilder.func;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VariableUtilities {
    private static final Set<Class<?>> ALLOWED_NUMBER_PRIMITIVES = Set.of(int.class, long.class, byte.class);

    public static boolean isNumber(final Class<?> variableClass) {
        return Number.class.isAssignableFrom(variableClass) || (variableClass.isPrimitive() && ALLOWED_NUMBER_PRIMITIVES.contains(variableClass));
    }

    public static boolean isNumber(final Object variable) {
        final Class<?> variableClass = variable.getClass();
        return isNumber(variableClass);
    }

    public static boolean isString(final Class<?> variableClass) {
        return variableClass.equals(String.class);
    }

    public static boolean isString(final Object variable) {
        final Class<?> variableClass = variable.getClass();
        return isString(variableClass);
    }

    public static boolean isNumberOrString(final Class<?> variableClass) {
        return isString(variableClass) || isNumber(variableClass);
    }

    public static boolean isNumberOrString(final Object variable) {
        final Class<?> variableClass = variable.getClass();
        return isNumberOrString(variableClass);
    }
}
