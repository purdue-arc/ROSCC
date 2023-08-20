package com.purduearc.roscc.server;

import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.turtle.ITurtleAccess;

public class TurtleAccessWrap {
	public final ITurtleAccess turtle;
	public final IComputerAccess computer;
	public TurtleAccessWrap(ITurtleAccess turtle, IComputerAccess computer) {
		this.turtle = turtle;
		this.computer = computer;
	}
}
