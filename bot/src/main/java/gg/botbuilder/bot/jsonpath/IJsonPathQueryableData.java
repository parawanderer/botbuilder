package gg.botbuilder.bot.jsonpath;

import lombok.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IJsonPathQueryableData {
     @NonNull
     Map<String, Object> getData();
     @NonNull
     List<String> query(String jsonPath);
     @NonNull
     Optional<String> queryOne(String jsonPath);
}
