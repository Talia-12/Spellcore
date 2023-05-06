package ram.talia.spellcore.forge;


import net.minecraftforge.common.ForgeConfigSpec;
import ram.talia.spellcore.api.config.SpellcoreConfig;

public class ForgeSpellcoreConfig implements SpellcoreConfig.CommonConfigAccess {
    public ForgeSpellcoreConfig(ForgeConfigSpec.Builder builder) {

    }

    public static class Client implements SpellcoreConfig.ClientConfigAccess {
        public Client(ForgeConfigSpec.Builder builder) {

        }
    }

    public static class Server implements SpellcoreConfig.ServerConfigAccess {

        public Server(ForgeConfigSpec.Builder builder) {
            builder.translation("text.autoconfig.spellcore.option.server.exampleSpells").push("exampleSpells");

            builder.pop();
        }

        //region getters

        //endregion
    }
}
