
package net.owo.cac.item;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class CacLogoItem extends Item {
	public CacLogoItem() {
		super(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC));
	}

	@Override
	public boolean isCorrectToolForDrops(BlockState state) {
		return true;
	}
}
