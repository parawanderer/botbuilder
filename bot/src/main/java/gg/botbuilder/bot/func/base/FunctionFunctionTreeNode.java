package gg.botbuilder.bot.func.base;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Builder
@RequiredArgsConstructor
public class FunctionFunctionTreeNode implements IFunctionTreeNode {
    @Getter
    @NonNull
    private final List<IFunctionTreeNode> args;

    @Getter
    @NonNull
    private final String name;

    public static FunctionFunctionTreeNode fn(String name, IFunctionTreeNode... args) {
        List<IFunctionTreeNode> arguments = new ArrayList<>(args.length);
        var fn = FunctionFunctionTreeNode.builder()
                .name(name)
                .args(arguments);

        arguments.addAll(Arrays.asList(args));

        return fn.build();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FunctionFunctionTreeNode fn) {
            return fn.getName().equals(this.getName()) &&
                    fn.getArgs().equals(this.getArgs());
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "fn{" + this.getName() + "}(" + (!this.getArgs().isEmpty() ? this.getArgs() : "" ) + ")";
    }

    @Override
    public Object accept(IFunctionTreeNodeVisitor visitor) {
        return visitor.visit(this);
    }
}
