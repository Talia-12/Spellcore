package ram.talia.spellcore.forge.eventhandlers;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ram.talia.spellcore.api.softphysics.Physics;

public class PhysicsEventHandler {
    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent event) {
        Physics.runPhysicsTick();
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        Physics.runPhysicsTick();
    }
}
