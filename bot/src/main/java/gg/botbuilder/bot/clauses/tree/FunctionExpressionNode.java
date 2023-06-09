package gg.botbuilder.bot.clauses.tree;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class FunctionExpressionNode implements IConditionNode {
    @Getter
    private final CLAUSE_FUNCTION operator;
    @Getter
    private final String subjectJsonQuery;
    @Getter
    private final String functionInput;

    @Override
    @NonNull
    public List<IConditionNode> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public boolean accept(ICoditionTreeEvaluatorVisitor visitor) {
        return visitor.visitExpression(this);
    }

    public static FunctionExpressionNode eq(String jsonQuery, String equalToWhat) {
        return new FunctionExpressionNode(CLAUSE_FUNCTION.EQ, jsonQuery, equalToWhat);
    }

    public static FunctionExpressionNode contains(String jsonQuery, String equalToWhat) {
        return new FunctionExpressionNode(CLAUSE_FUNCTION.CONTAINS, jsonQuery, equalToWhat);
    }

    public static FunctionExpressionNode startsWith(String jsonQuery, String equalToWhat) {
        return new FunctionExpressionNode(CLAUSE_FUNCTION.STARTS_WITH, jsonQuery, equalToWhat);
    }

    public static FunctionExpressionNode endsWith(String jsonQuery, String equalToWhat) {
        return new FunctionExpressionNode(CLAUSE_FUNCTION.ENDS_WITH, jsonQuery, equalToWhat);
    }

    public static FunctionExpressionNode regex(String jsonQuery, String equalToWhat) {
        return new FunctionExpressionNode(CLAUSE_FUNCTION.REGEX, jsonQuery, equalToWhat);
    }

    @Override
    public String toString() {
        return operator.toLowerCase() + "(" +
                subjectJsonQuery + ","  +
                functionInput +
                ')';
    }
}
