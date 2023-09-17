package com.purduearc.roscc.server;

public class TurtleStatus {
	public final SimplePos pos;
	public final SimpleItem[] inv;
	public final SimpleBlock[] blocks;
	public final SimpleEntity[] entities;
	public final String dir;
	public TurtleStatus(SimplePos pos, SimpleItem[] inv, SimpleBlock[] blocks, SimpleEntity[] entities, String dir) {
		this.pos = pos;
		this.inv = inv;
		this.blocks = blocks;
		this.entities = entities;
		this.dir = dir;
	}
}
