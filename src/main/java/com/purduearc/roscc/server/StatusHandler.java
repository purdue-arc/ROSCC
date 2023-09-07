package com.purduearc.roscc.server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;

public class StatusHandler implements HttpHandler {
	
	private final Moshi moshi;
	private final JsonAdapter<TurtleStatus> turtleStatusAdapter;
	public StatusHandler() {
		this.moshi = new Moshi.Builder().build();
		this.turtleStatusAdapter = this.moshi.adapter(TurtleStatus.class);
	}
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		String response = "";
		
		HashMap<String, String> query = QueryUtils.parseQuery(exchange.getRequestURI().getQuery());
		if (query.size() == 0 || !query.containsKey("id")) {
			response += "400 Bad Request";
			exchange.sendResponseHeaders(400, response.length());
	        OutputStream os = exchange.getResponseBody();
	        os.write(response.getBytes());
	        os.close();
	        return;
		}
		int id = Integer.parseInt(query.getOrDefault("id", "0"));
		int requestedBlocksRadius = Integer.parseInt(query.getOrDefault("blocks", "0"));
		int requestedEntitiesRadius = Integer.parseInt(query.getOrDefault("entities", "0"));
		boolean requestedInventory = query.containsKey("inv");
		boolean requestedPosition = query.containsKey("pos");
		boolean requestedBlockNBT = query.containsKey("blocknbt");
		boolean requestedItemNBT = query.containsKey("itemnbt");
		boolean requestedEntityNBT = query.containsKey("entitynbt");
		boolean requestedDirection = query.containsKey("dir");
		TurtleAccessWrap turtle = ROSCCServer.turtles.get(id);
		SimpleItem[] inv = null;
		SimplePos pos = null;
		SimpleBlock[] blocks = null;
		SimpleEntity[] entities = null;
		Direction dir = null;
		if (turtle == null) {
			response += "400 Bad Request (Invalid ID)";
			exchange.sendResponseHeaders(400, response.length());
	        OutputStream os = exchange.getResponseBody();
	        os.write(response.getBytes());
	        os.close();
	        return;
		}
		if (requestedInventory) {
			inv = new SimpleItem[turtle.turtle.getInventory().getContainerSize()];
			for (int i = 0; i < inv.length; i++) {
				inv[i] = new SimpleItem(turtle.turtle.getInventory().getItem(i), requestedItemNBT);
			}
		}
		if (requestedPosition) {
			pos = new SimplePos(turtle.turtle.getPosition());
		}
		if (requestedBlocksRadius > 0) {
			Vec3i cubeCorner = new Vec3i(requestedBlocksRadius, requestedBlocksRadius, requestedBlocksRadius);
			ArrayList<SimpleBlock> blocksList = new ArrayList<SimpleBlock>();
			for (BlockPos bp : BlockPos.betweenClosed(turtle.turtle.getPosition().subtract(cubeCorner), turtle.turtle.getPosition().offset(cubeCorner))) {
				String nbtString = null;
				if (requestedBlockNBT) {
					CompoundTag nbt = NbtUtils.writeBlockState(turtle.turtle.getLevel().getBlockState(bp));
					nbt.remove("Name");
					nbtString = nbt.toString().equals("{}") ? null : nbt.toString();
				}
				blocksList.add(new SimpleBlock(bp.getX(), bp.getY(), bp.getZ(), ForgeRegistries.BLOCKS.getKey(turtle.turtle.getLevel().getBlockState(bp).getBlock()).toString(), nbtString));
			}
			blocks = blocksList.toArray(new SimpleBlock[blocksList.size()]);
		}
		if (requestedEntitiesRadius > 0) {
			Vec3i cubeCorner = new Vec3i(requestedEntitiesRadius, requestedEntitiesRadius, requestedEntitiesRadius);
			List<Entity> entitiesList = turtle.turtle.getLevel().getEntities(null, new AABB(turtle.turtle.getPosition().subtract(cubeCorner).offset(0,1,0), turtle.turtle.getPosition().offset(cubeCorner).offset(1,1,1)));
			entities = new SimpleEntity[entitiesList.size()];
			for (int i = 0; i < entities.length; i++) {
				entities[i] = new SimpleEntity(entitiesList.get(i), requestedEntityNBT);
			}
		}
		if (requestedDirection) {
			dir = turtle.turtle.getDirection();
		}
		
		response = turtleStatusAdapter.toJson(new TurtleStatus(pos, inv, blocks, entities, dir));
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
	}

}
