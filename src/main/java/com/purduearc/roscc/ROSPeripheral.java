package com.purduearc.roscc;

import com.purduearc.roscc.blocks.ROSPeripheralBlockEntity;

import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;

public class ROSPeripheral implements IPeripheral {
	
	private final ROSPeripheralBlockEntity blockEntity;
	
	public ROSPeripheral(ROSPeripheralBlockEntity rosPeripheralBlockEntity) {
		this.blockEntity = rosPeripheralBlockEntity;
	}

	@Override
	public String getType() {
		return "ros";
	}

	@Override
	public boolean equals(IPeripheral other) {
		return this == other || (other instanceof ROSPeripheral && ((ROSPeripheral) other).blockEntity == blockEntity);
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
