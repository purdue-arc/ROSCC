package com.purduearc.roscc;

import com.mojang.logging.LogUtils;
import com.purduearc.roscc.blocks.ROSPeripheralBlock;
import com.purduearc.roscc.blocks.ROSPeripheralBlockEntity;
import dan200.computercraft.api.client.ComputerCraftAPIClient;
import dan200.computercraft.api.client.turtle.TurtleUpgradeModeller;
import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ROSCC.MODID)
public class ROSCC
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "roscc";
    // Directly reference a slf4j logger
    protected static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "roscc" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "roscc" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    // Create a Deferred Register to hold BlockEntities which will all be registered under the "roscc" namespace
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);
    // Create a Deferred Register to hold TurtleUpgradeSerialisers which will all be registered under the "roscc" namespace
    public static final DeferredRegister<TurtleUpgradeSerialiser<?>> TURTLE_UPGRADE_SERIALIZERS = DeferredRegister.create(TurtleUpgradeSerialiser.registryId(), MODID);
    
    // Creates a new Block with the id "roscc:ros_peripheral"
    public static final RegistryObject<Block> PERIPHERAL_BLOCK = BLOCKS.register("ros_peripheral", () -> new ROSPeripheralBlock());
    // Creates a new BlockItem with the id "roscc:ros_peripheral"
    public static final RegistryObject<Item> PERIPHERAL_BLOCK_ITEM = ITEMS.register("ros_peripheral", () -> new BlockItem(PERIPHERAL_BLOCK.get(), new Item.Properties()));
    // Creates a new BlockEntity with the id "roscc:ros_peripheral"
    public static final RegistryObject<BlockEntityType<ROSPeripheralBlockEntity>> PERIPHERAL_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("ros_peripheral", () -> BlockEntityType.Builder.of((pos, state) -> new ROSPeripheralBlockEntity(ROSCC.PERIPHERAL_BLOCK_ENTITY.get(), pos, state), PERIPHERAL_BLOCK.get()).build(null));
    // Creates a new TurtleUpgradeSerialiser with the id "roscc:ros_peripheral"
    public static final RegistryObject<TurtleUpgradeSerialiser<ROSTurtleUpgrade>> PERIPHERAL_TURTLE_UPGRADE = TURTLE_UPGRADE_SERIALIZERS.register("ros_upgrade", () -> TurtleUpgradeSerialiser.simple(ROSTurtleUpgrade::new));
    
    public ROSCC()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
//        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so block entities get registered
        BLOCK_ENTITY_TYPES.register(modEventBus);
        // Register the Deferred Register to the mod event bus so turtle upgrade serializers get registered
        TURTLE_UPGRADE_SERIALIZERS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        
        // Register the creative tab
//        modEventBus.addListener(this::addCreativeTab);

        // Register the item to the creative tab
//        modEventBus.addListener(this::addCreativeContents);
    }

//    private void commonSetup(final FMLCommonSetupEvent event)
//    {
        // Some common setup code
//        LOGGER.info("HELLO FROM COMMON SETUP");
//        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
//    }
    
//    private void addCreativeTab(CreativeModeTabEvent.Register event) {
//    	event.regi
//    }
//
//    private void addCreativeContents(CreativeModeTabEvent.BuildContents event)
//    {
//        if (event.getTab() == null) event.accept(PERIPHERAL_BLOCK_ITEM);
//    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
//    @SubscribeEvent
//    public void onServerStarting(ServerStartingEvent event)
//    {
//        // Do something when the server starts
//        LOGGER.info("HELLO from server starting");
//    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
    	// Taken from https://gist.github.com/ChampionAsh5357/163a75e87599d19ee6b4b879821953e8
    	// Available under CC BY 4.0
    	@SubscribeEvent
    	public static void buildContents(CreativeModeTabEvent.Register event) {
    		// Thanks to https://gist.github.com/ChampionAsh5357/163a75e87599d19ee6b4b879821953e8 for 
    		event.registerCreativeModeTab(new ResourceLocation(MODID, "tab"), builder ->
	    		builder.title(Component.translatable("item_group." + MODID + ".tab"))
	    	    .icon(() -> new ItemStack(PERIPHERAL_BLOCK_ITEM.get()))
	    	    .displayItems((params, output) -> {
	    	    	ItemStack item = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("computercraft", "turtle_advanced")));
	    	    	item.addTagElement("RightUpgrade", StringTag.valueOf("roscc:ros_upgrade"));
		    	    output.accept(PERIPHERAL_BLOCK_ITEM.get());
		    	    output.accept(item);
    	    })
    	  );
    	}
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
//            LOGGER.info("HELLO FROM CLIENT SETUP");
//            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
            ComputerCraftAPIClient.registerTurtleUpgradeModeller(PERIPHERAL_TURTLE_UPGRADE.get(), TurtleUpgradeModeller.sided(new ResourceLocation(MODID, "block/ros_upgrade_left"), new ResourceLocation(MODID, "block/ros_upgrade_right")));
        }
    }
}
