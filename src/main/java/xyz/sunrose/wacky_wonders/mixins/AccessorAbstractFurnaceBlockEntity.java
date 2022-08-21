package xyz.sunrose.wacky_wonders.mixins;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractFurnaceBlockEntity.class)
public interface AccessorAbstractFurnaceBlockEntity {
	@Accessor
	int getBurnTime();
	@Accessor
	void setBurnTime(int burnTime);

	@Accessor
	int getCookTime();
	@Accessor
	void setCookTime(int cookTime);

	@Accessor
	int getCookTimeTotal();

}
