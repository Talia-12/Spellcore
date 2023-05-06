package ram.talia.spellcore.fabric;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import ram.talia.spellcore.api.SpellcoreAPI;
import ram.talia.spellcore.api.config.SpellcoreConfig;
import ram.talia.spellcore.xplat.IXplatAbstractions;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
@Config(name = SpellcoreAPI.MOD_ID)
@Config.Gui.Background("minecraft:textures/block/calcite.png")
public class FabricSpellcoreConfig extends PartitioningSerializer.GlobalData {
    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.TransitiveObject
    public final Common common = new Common();
    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.TransitiveObject
    public final Client client = new Client();
    @ConfigEntry.Category("server")
    @ConfigEntry.Gui.TransitiveObject
    public final Server server = new Server();

    public static FabricSpellcoreConfig setup() {
        AutoConfig.register(FabricSpellcoreConfig.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
        var instance = AutoConfig.getConfigHolder(FabricSpellcoreConfig.class).getConfig();

        SpellcoreConfig.setCommon(instance.common);
        // We care about the client only on the *physical* client ...
        if (IXplatAbstractions.INSTANCE.isPhysicalClient()) {
            SpellcoreConfig.setClient(instance.client);
        }
        // but we care about the server on the *logical* server
        // i believe this should Just Work without a guard? assuming we don't access it from the client ever
        SpellcoreConfig.setServer(instance.server);

        return instance;
    }


    @Config(name = "common")
    private static class Common implements ConfigData, SpellcoreConfig.CommonConfigAccess { }

    @Config(name = "client")
    private static class Client implements ConfigData, SpellcoreConfig.ClientConfigAccess { }


    @Config(name = "server")
    private static class Server implements ConfigData, SpellcoreConfig.ServerConfigAccess {

        @Override
        public void validatePostLoad() throws ValidationException {
        }

        private int bound(int toBind, int lower, int upper) {
            return Math.min(Math.max(toBind, lower), upper);
        }
        private double bound(double toBind, double lower, double upper) {
            return Math.min(Math.max(toBind, lower), upper);
        }


        //region getters



        //endregion
    }
}
