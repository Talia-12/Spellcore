package ram.talia.spellcore.forge.xplat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import ram.talia.spellcore.xplat.IClientXplatAbstractions;

import java.util.function.Function;

public class ForgeClientXplatImpl implements IClientXplatAbstractions {
//	@Override
//	public void sendPacketToServer (IMessage packet) {
//		ForgePacketHandler.getNetwork().sendToServer(packet);
//	}
	
	@Override
	public void initPlatformSpecific () {
		// NO-OP
	}
	
	@Override
	public <T extends Entity> void registerEntityRenderer (EntityType<? extends T> type, EntityRendererProvider<T> renderer) {
		EntityRenderers.register(type, renderer);
	}
	
	@Override
	public <T extends ParticleOptions> void registerParticleType (ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> factory) {
		Minecraft.getInstance().particleEngine.register(type, factory::apply);
	}
	
	@Override
	public void registerItemProperty (Item item, ResourceLocation id, ItemPropertyFunction func) {
		ItemProperties.register(item, id, func);
	}
	
//	@Override
//	public void setFilterSave (AbstractTexture texture, boolean filter, boolean mipmap) {
//		texture.setBlurMipmap(filter, mipmap);
//	}
//
//	@Override
//	public void restoreLastFilter (AbstractTexture texture) {
//		texture.restoreLastBlurMipmap();
//	}
}
