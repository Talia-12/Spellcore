package ram.talia.spellcore.client;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;
import ram.talia.spellcore.client.entity.SpellRenderer;
import ram.talia.spellcore.common.lib.SpellcoreEntities;
import ram.talia.spellcore.xplat.IClientXplatAbstractions;

public class RegisterClientStuff {
	public static void init () {
		var x = IClientXplatAbstractions.INSTANCE;

		x.registerEntityRenderer(SpellcoreEntities.SPELL_ENTITY, SpellRenderer::new);
	}

	public static void registerBlockEntityRenderers(@NotNull BlockEntityRendererRegisterer registerer) {

	}

	@FunctionalInterface
	public interface BlockEntityRendererRegisterer {
		<T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<T> type, BlockEntityRendererProvider<? super T> berp);
	}
}
