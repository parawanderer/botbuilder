package gg.botbuilder.bot.app.handler;

import gg.botbuilder.bot.app.action.ActionFactory;
import gg.botbuilder.bot.app.action.ActionHandlerContext;
import gg.botbuilder.bot.app.action.IAction;
import gg.botbuilder.bot.clauses.eval.ClauseTreeEvaluatorVisitor;
import gg.botbuilder.bot.conf.model.action.IThenActionArguments;
import gg.botbuilder.bot.jsonpath.IJsonPathQueryableData;
import lombok.NonNull;
import net.dv8tion.jda.api.events.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public abstract class GenericEventSubscriber<T extends Event> implements IRegisteredEventSubscriber {
    private static final Logger LOGGER = LogManager.getLogger();

    @NonNull
    private final Factory<GenericEventHandlerContext> contextFactory;

    protected GenericEventSubscriber(@NonNull Factory<GenericEventHandlerContext> contextFactory) {
        this.contextFactory = contextFactory;
    }

    @Override
    public void call(@NonNull Event event) {
        // 1. should I be allowed to handle this?
        GenericEventHandlerContext context = this.contextFactory.call();
        final String name = context.getThenActionDefinition().getActionType().toPublicFacing();
        final IThenActionArguments config = context.getThenActionDefinition();

        // 2. Build the json context that user-provided logic will have access to... (will be case-by-case)
        final IJsonPathQueryableData userQueryableContext = this.buildContext(context, (T) event);

        // 3. evaluate the user-provided clause (if we had one) or just apply it anyway
        if (this.isWhereClauseMet(context, userQueryableContext)) {
            LOGGER.debug("Executing then event '" + name + "'");

            ActionHandlerContext actionContext = ActionHandlerContext.builder()
                    .userQueryableContext(userQueryableContext)
                    .environmentVariables(context.getEnvironmentVariables())
                    .thenActionDefinition(context.getThenActionDefinition())
                    .build();

            IAction action = ActionFactory.getAction(
                    context.getThenActionDefinition().getActionType(),
                    actionContext,
                    event.getJDA(),
                    event
            );
            action.execute(config);

        } else {
            LOGGER.debug("Where clause was not met for '" + name + "'");
        }
    }

    private boolean isWhereClauseMet(@NonNull GenericEventHandlerContext context, final IJsonPathQueryableData userQueryableContext) {
        return context.getThenActionDefinition()
                .getWhere()
                .map(clause -> new ClauseTreeEvaluatorVisitor(userQueryableContext).evaluate(clause))
                .orElse(true);
    }

    @NonNull
    protected abstract IJsonPathQueryableData buildContext(@NonNull GenericEventHandlerContext context, @NonNull T event); // implement this
}
