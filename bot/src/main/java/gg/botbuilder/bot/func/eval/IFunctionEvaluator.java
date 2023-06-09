package gg.botbuilder.bot.func.eval;

import gg.botbuilder.bot.func.base.IFunctionTreeNode;
import gg.botbuilder.bot.jsonpath.IJsonPathQueryableData;
import lombok.NonNull;

public interface IFunctionEvaluator {
    Object evaluate(@NonNull IFunctionTreeNode node, @NonNull IJsonPathQueryableData ctx);
}
