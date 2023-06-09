package gg.botbuilder.bot.func.base;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class VariableFunctionTreeNode implements IFunctionTreeNode {
    @Getter
    @NonNull
    private final String variable;

    public static VariableFunctionTreeNode var(String variablePath) {
        return VariableFunctionTreeNode.builder().variable(variablePath).build();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VariableFunctionTreeNode varr) {
            return varr.getVariable().equals(this.getVariable());
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "var{" + this.getVariable() + "}";
    }

    @Override
    public Object accept(IFunctionTreeNodeVisitor visitor) {
        return visitor.visit(this);
    }
}
