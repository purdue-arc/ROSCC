package com.purduearc.roscc;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.purduearc.roscc.blocks.ROSPeripheralBlockEntity;
import com.purduearc.roscc.server.ROSCCServer;
import com.purduearc.roscc.server.TurtleAccessWrap;

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
	
	private static ROSCCServer server = new ROSCCServer("127.0.0.1", 8080);
	private static Thread serverThread = new Thread(server);
	
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
	public final int nodeStatus(IComputerAccess computer) {
		return this.active ? computer.getID() : -1;
	}
	
	@LuaFunction
	public final boolean setNodeAddress(String address, int port) {
		server.stop();
		serverThread = new Thread(server);
		try {
			server.setAddress(InetAddress.getByName(address));
		} catch (UnknownHostException e) {
			return false;
		}
		server.setPort(port);
		serverThread.start();
		return true;
	}
	
	@LuaFunction
	public final boolean isTurtle(IComputerAccess computer) {
		return turtle != null;
	}
	
	@Override
	public void attach(IComputerAccess computer) {
		if (turtle != null) {
			ROSCCServer.turtles.put(computer.getID(), new TurtleAccessWrap(turtle, computer));
			if (!serverThread.isAlive() && !server.isRunning()) serverThread.start();
			this.active = true;
		}
		if (blockEntity != null) blockEntity.attach(computer);
	}
	
	@Override
	public void detach(IComputerAccess computer) {
		if (turtle != null) {
			ROSCCServer.turtles.remove(computer.getID());
			if (ROSCCServer.turtles.size() == 0 && server.isRunning()) server.stop();
			this.active = false;
		}
		if (blockEntity != null) blockEntity.detach(computer);
	}
	
	@Override
	public Object getTarget() {
		return blockEntity;
	}

}
