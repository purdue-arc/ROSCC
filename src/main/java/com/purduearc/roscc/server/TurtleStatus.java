package com.purduearc.roscc.server;

public class TurtleStatus {
	public final SimplePos pos;
	public final SimpleItem[] inv;
	public final SimpleBlock[] blocks;
	public TurtleStatus(SimplePos pos, SimpleItem[] inv, SimpleBlock[] blocks) {
		this.pos = pos;
		this.inv = inv;
		this.blocks = blocks;
	}
}
