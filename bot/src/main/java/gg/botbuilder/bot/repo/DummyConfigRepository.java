package gg.botbuilder.bot.repo;

import gg.botbuilder.bot.conf.YamlConfigInterpreter;
import gg.botbuilder.bot.conf.model.ConfigContainerModel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DummyConfigRepository implements IConfigRepository {
    @Override
    public Map<String, ConfigContainerModel> pullConfigs(List<String> serverIds) {
        var dummyConf =
"""
bot:
  version: "0.0.1"
  customisation:
    name: "My Bot"
    profilePictureUrl: "https://s3-link-goes-here"
    status: "watermark by default else custom"
  modules: # enabled modules
    list:
      - midjourney
      - crypto
      - chatgpt
      - minecraft-server
      - authentication
      - calculator
  config:
    variables:
      "MY_VARIABLE": "poop sock"
      "SOME_TOKEN": "aaaa"
    # declarations for code behaviour
    discord:
      events:
        - on: "message_read" # fixed choice of events
          where:
            # free for all inside this
            and:
              - eq:
                  - "$.message.server.id"
                  - "1081879642513866762"
              - eq:
                  - "$.message.channel.id"
                  - "1081879643168194562"
              - or:
                  - eq:
                      - "$.message.sender.rank"
                      - "1114649835484745749"
                  - eq:
                      - "$.message.sender.id"
                      - "192148309123530753"
          then:
            # semi-structured in the sense that you can define one or more actions
            # there will be variables corresponding to actions that you can use in the templates
            # maybe functions too?
            - reply:
                content: "hello, $.message.sender.at we are in $.message.channel.at!" # as opposed to 'send_message'
                when:
                  not:
                    eq:
                      - "$.message.sender.id"
                      - "192148309123530753"
            - send-message:
                server: "1081879642513866762" # must be available, of course
                channel: "1114618181621911703" # must be available, of course
                content: "I see that $.message.sender.at sent a message in $.message.channel.at in our server $.message.server.at"
            - log:
                content: "sent hello message to $.message.sender.id"
            - role-manage:
                action: "add"
                role: "122345"
            - webhook:
                # mostly structured, I guess this could be a list too...
                method: POST
                payload: json # let's only support json for now
                target: "http://127.0.0.1:8080/message"
                headers:
                  "X-Authorization": "$.ENV.MY_VARIABLE"
                  "Content-Type": "text/json"
                content: |-
                  {
                    "greeted": "$.message.sender.id"
                  }
                
""";

        var interpreter = new YamlConfigInterpreter();
        ConfigContainerModel conf = interpreter.interpret(dummyConf);

        Map<String, ConfigContainerModel> all = new HashMap<>();
        for (String guildId : serverIds) {
            all.put(guildId, conf);
        }

        return all;
    }

}
