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
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;

public class ActionHandler implements HttpHandler {
	
	private final Moshi moshi;
	private final JsonAdapter<TurtleStatus> turtleStatusAdapter;
	public ActionHandler() {
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
		TurtleAccessWrap turtle = ROSCCServer.turtles.get(id);
		if (turtle == null) {
			response += "400 Bad Request (Invalid ID)";
			exchange.sendResponseHeaders(400, response.length());
	        OutputStream os = exchange.getResponseBody();
	        os.write(response.getBytes());
	        os.close();
	        return;
		}
		response = "test";
		exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
	}

}
