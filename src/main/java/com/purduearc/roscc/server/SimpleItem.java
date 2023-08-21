package com.purduearc.roscc.server;

import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SimpleItem {
	public final int id;
	public final int count;
	public final String nbt;
	public SimpleItem(int id, int count, String nbt) {
		this.id = id;
		this.count = count;
		this.nbt = nbt;
	}
	public SimpleItem(ItemStack item) {
		this.id = Item.getId(item.getItem());
		this.count = item.getCount();
		String fullNBT = item.serializeNBT().toString();
		this.nbt = fullNBT.equals("{Count:1b,id:\"minecraft:air\"}") ? null : fullNBT;
	}
	public static SimpleItem[] toSimpleItems(Container container) {
		SimpleItem[] items = new SimpleItem[container.getContainerSize()];
		for (int i = 0; i < items.length; i++) {
			items[i] = new SimpleItem(container.getItem(i));
		}
		return items;
	}
}
