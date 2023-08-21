package com.purduearc.roscc.server;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class SimpleItem {
	public final String id;
	public final int count;
	public final String nbt;
	public SimpleItem(String id, int count, String nbt) {
		this.id = id;
		this.count = count;
		this.nbt = nbt;
	}
	public SimpleItem(ItemStack item, boolean includeNBT) {
		this.id = ForgeRegistries.ITEMS.getKey(item.getItem()).toString();
		this.count = item.getCount();
		CompoundTag fullNBT = item.serializeNBT();
		fullNBT.remove("Count");
		fullNBT.remove("id");
		this.nbt = !includeNBT || fullNBT.toString().equals("{}") ? null : fullNBT.toString();
	}
}
