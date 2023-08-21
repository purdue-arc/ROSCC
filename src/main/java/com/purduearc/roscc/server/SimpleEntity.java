package com.purduearc.roscc.server;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

public class SimpleEntity {
	public final String type;
	public final double x;
	public final double y;
	public final double z;
	public final String nbt;
	public SimpleEntity(String type, double x, double y, double z, String nbt) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		this.nbt = nbt;
	}
	public SimpleEntity(Entity entity, boolean includeNBT) {
		this.type = entity.getType().toShortString();
		this.x = entity.getX();
		this.y = entity.getY();
		this.z = entity.getZ();
		CompoundTag fullNBT = entity.serializeNBT();
//		fullNBT.remove("Count");
//		fullNBT.remove("id");
		this.nbt = !includeNBT || fullNBT.toString().equals("{}") ? null : fullNBT.toString();
	}
}
