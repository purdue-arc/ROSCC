package com.purduearc.roscc;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleUpgradeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ROSTurtleUpgrade implements ITurtleUpgrade {
	
	private static final ItemStack CRAFTING_ITEM = new ItemStack(ROSCC.PERIPHERAL_BLOCK_ITEM.get(), 1);
	private static final String UNLOCALIZED_ADJECTIVE = "ROSified";
	private final ResourceLocation upgradeID;
	
	public ROSTurtleUpgrade(ResourceLocation upgradeID) {
		this.upgradeID = upgradeID;
	}

	@Override
	public ResourceLocation getUpgradeID() {
		return upgradeID;
	}

	@Override
	public String getUnlocalisedAdjective() {
		return UNLOCALIZED_ADJECTIVE;
	}

	@Override
	public ItemStack getCraftingItem() {
		return CRAFTING_ITEM;
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.PERIPHERAL;
	}
	
	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return new ROSPeripheral(turtle, side);
	}

}
