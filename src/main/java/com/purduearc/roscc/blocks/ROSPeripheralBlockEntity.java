package com.purduearc.roscc.blocks;

import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;

import java.util.HashSet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.purduearc.roscc.Capabilities;
import com.purduearc.roscc.ROSPeripheral;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class ROSPeripheralBlockEntity extends BlockEntity {
	
	private static final Capability<IPeripheral> CAPABILITY_PERIPHERAL = Capabilities.CAPABILITY_PERIPHERAL;
	private LazyOptional<IPeripheral> peripheralLazyOptional = LazyOptional.of(() -> new ROSPeripheral(this));
	private final HashSet<IComputerAccess> computers = new HashSet<IComputerAccess>();

	public ROSPeripheralBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		return CAPABILITY_PERIPHERAL.orEmpty(cap, peripheralLazyOptional);
	}
	
	@Override
	public void invalidateCaps() {
		peripheralLazyOptional.invalidate();
		super.invalidateCaps();
	}
	
	public void attach(IComputerAccess computer) {
		computers.add(computer);
	}
	
	public void detach(IComputerAccess computer) {
		computers.remove(computer);
	}
	
}
