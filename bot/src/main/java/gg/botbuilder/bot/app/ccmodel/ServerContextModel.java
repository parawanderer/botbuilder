package gg.botbuilder.bot.app.ccmodel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServerContextModel extends GenericNamedEntityContextModel {
    @Getter
    private String link;

    @Getter
    private String description;

    @Getter
    private String image;

    @Getter
    private String banner;

    @Jacksonized
    @Builder
    public ServerContextModel(@NonNull String name, @NonNull String id, String link, String description, String image, String banner) {
        super(name, id);
        this.link = link;
        this.description = description;
        this.image = image;
        this.banner = banner;
    }
}
