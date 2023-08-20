package com.purduearc.roscc.server;

import net.minecraft.core.Vec3i;

public class SimplePos {
	public final int x;
	public final int y;
	public final int z;
	public SimplePos(int z, int y, int x) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public SimplePos(Vec3i pos) {
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
	}
}
