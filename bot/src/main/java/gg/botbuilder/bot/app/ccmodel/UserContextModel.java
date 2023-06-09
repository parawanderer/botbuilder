package gg.botbuilder.bot.app.ccmodel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.time.OffsetDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserContextModel extends GenericNamedEntityContextModel {
    @Getter
    private String avatar;

    @Getter
    private List<String> roles;

    @Getter
    @NonNull
    private OffsetDateTime discordJoinDate;

    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OffsetDateTime serverJoinDate;

    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String serverNickname;

    @Jacksonized
    @Builder
    public UserContextModel(@NonNull String name, @NonNull String id, String avatar, List<String> roles, @NonNull OffsetDateTime discordJoinDate, OffsetDateTime serverJoinDate, String serverNickname) {
        super(name, id);
        this.avatar = avatar;
        this.roles = roles;
        this.discordJoinDate = discordJoinDate;
        this.serverJoinDate = serverJoinDate;
        this.serverNickname = serverNickname;
    }
}
