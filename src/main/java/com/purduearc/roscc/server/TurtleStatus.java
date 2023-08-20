package com.purduearc.roscc.server;

public class TurtleStatus {
	public final SimplePos pos;
	public final SimpleItem[] inv;
	public TurtleStatus(SimplePos pos, SimpleItem[] inv) {
		this.pos = pos;
		this.inv = inv;
	}
}
