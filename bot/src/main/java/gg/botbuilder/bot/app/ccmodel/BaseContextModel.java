package gg.botbuilder.bot.app.ccmodel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Map;

@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseContextModel {
    @Getter
    @NonNull
    private Map<String, String> ENV;
}
