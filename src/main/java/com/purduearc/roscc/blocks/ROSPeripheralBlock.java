package com.purduearc.roscc.blocks;

import com.purduearc.roscc.ROSCC;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ROSPeripheralBlock extends Block implements EntityBlock {
	private static final VoxelShape SHAPE = Shapes.join(Block.box(1.0d, 0.0d, 1.0d, 15.0d, 2.0d, 15.0d), Block.box(2.0d, 0.0d, 2.0d, 14.0d, 3.0d, 14.0d), BooleanOp.OR);

	public ROSPeripheralBlock() {
		super(BlockBehaviour.Properties.of(Material.STONE));
	}
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext collisionCtx) {
		return SHAPE;
	}
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return ROSCC.PERIPHERAL_BLOCK_ENTITY.get().create(pos, state);
	}
}
