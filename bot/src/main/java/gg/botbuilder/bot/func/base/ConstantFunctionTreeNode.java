package gg.botbuilder.bot.func.base;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.util.Locale;

@Builder
@RequiredArgsConstructor
public class ConstantFunctionTreeNode implements IFunctionTreeNode {
    @Getter
    @NonNull
    private final String value;

    @Getter
    private final CONSTANT_TYPE type;

    public static ConstantFunctionTreeNode string(String stringContent) {
        return ConstantFunctionTreeNode.builder()
                .type(CONSTANT_TYPE.STRING)
                .value(stringContent)
                .build();
    }

    public static ConstantFunctionTreeNode number(String number) {
        try {
            new BigInteger(number);
        } catch (NumberFormatException e) {
            throw e;
        }
        return ConstantFunctionTreeNode.builder()
                .type(CONSTANT_TYPE.NUMBER)
                .value(number)
                .build();
    }

    public static ConstantFunctionTreeNode number(int number) {
        return ConstantFunctionTreeNode.builder()
                .type(CONSTANT_TYPE.NUMBER)
                .value(String.valueOf(number))
                .build();
    }

    public static ConstantFunctionTreeNode number(long number) {
        return ConstantFunctionTreeNode.builder()
                .type(CONSTANT_TYPE.NUMBER)
                .value(String.valueOf(number))
                .build();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ConstantFunctionTreeNode constant) {
            return constant.getType() == this.getType() && constant.getValue().equals(this.getValue());
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "const{" + this.getType().toString().toLowerCase(Locale.ROOT) + "}{" + this.getValue() + "}";
    }

    @Override
    public Object accept(IFunctionTreeNodeVisitor visitor) {
        return visitor.visit(this);
    }
}
