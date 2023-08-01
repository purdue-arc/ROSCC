package com.purduearc.roscc;

import com.purduearc.roscc.blocks.ROSPeripheralBlockEntity;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;

public class ROSPeripheral implements IPeripheral {
	
	private ROSPeripheralBlockEntity blockEntity = null;
	private ITurtleAccess turtle = null;
	private TurtleSide turtleSide = null;
	
	private boolean active = false;
	
	public ROSPeripheral(ROSPeripheralBlockEntity rosPeripheralBlockEntity) {
		this.blockEntity = rosPeripheralBlockEntity;
	}
	
	public ROSPeripheral(ITurtleAccess turtle, TurtleSide turtleSide) {
		this.turtle = turtle;
		this.turtleSide = turtleSide;
	}

	@Override
	public String getType() {
		return "ros";
	}

	@Override
	public boolean equals(IPeripheral other) {
		return this == other || (other instanceof ROSPeripheral && ((ROSPeripheral) other).blockEntity == blockEntity);
	}
	
	@LuaFunction
	public final void startNode(IComputerAccess computer) {
		this.active = true;
	}
	
	@LuaFunction
	public final void stopNode(IComputerAccess computer) {
		this.active = false;
	}
	
	@LuaFunction
	public final boolean nodeStatus(IComputerAccess computer) {
		return this.active;
	}
	
	@LuaFunction
	public final boolean turtleTest(IComputerAccess computer) {
		return turtle != null;
	}
	
	@Override
	public void attach(IComputerAccess computer) {
		blockEntity.attach(computer);
	}
	
	@Override
	public void detach(IComputerAccess computer) {
		blockEntity.detach(computer);
	}
	
	@Override
	public Object getTarget() {
		return blockEntity;
	}

}
