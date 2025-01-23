
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.owo.cac.init;

import net.owo.cac.world.inventory.GuiBlankMenu;
import net.owo.cac.CacMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.common.extensions.IForgeMenuType;

import net.minecraft.world.inventory.MenuType;

public class CacModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, CacMod.MODID);
	public static final RegistryObject<MenuType<GuiBlankMenu>> GUI_BLANK = REGISTRY.register("gui_blank", () -> IForgeMenuType.create(GuiBlankMenu::new));
}
