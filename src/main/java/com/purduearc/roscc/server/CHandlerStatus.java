package com.purduearc.roscc.server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class CHandlerStatus implements HttpHandler {
	
	private final Moshi moshi;
	private final JsonAdapter<TurtleStatus> turtleStatusAdapter;
	public CHandlerStatus() {
		this.moshi = new Moshi.Builder().build();
		this.turtleStatusAdapter = this.moshi.adapter(TurtleStatus.class);
	}
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		String response = "";
		
		HashMap<String, String> query = parseQuery(exchange.getRequestURI().getQuery());
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
		TurtleAccessWrap turtle = ROSCCServer.turtles.get(id);
		SimpleItem[] inv = null;
		SimplePos pos = null;
		if (turtle == null) {
			response += "400 Bad Request (Invalid ID)";
			exchange.sendResponseHeaders(400, response.length());
	        OutputStream os = exchange.getResponseBody();
	        os.write(response.getBytes());
	        os.close();
	        return;
		}
		if (requestedInventory) {
			inv = SimpleItem.toSimpleItems(turtle.turtle.getInventory());
		}
		if (requestedPosition) {
			pos = new SimplePos(turtle.turtle.getPosition());
		}
		response = turtleStatusAdapter.toJson(new TurtleStatus(pos, inv));
		
		
		// localhost:8080/status?id=1
		
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
	}
	private static HashMap<String, String> parseQuery(String query) {
		HashMap<String, String> map = new HashMap<String, String>();
		for (String keyval : query.split("\\&")) {
			String parts[] = keyval.split("\\=");
			if (parts.length == 2) map.put(parts[0], parts[1]);
			else map.put(parts[0], "");
		}
		return map;
	}

}
