package com.purduearc.roscc.server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dan200.computercraft.api.lua.ILuaCallback;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleVerb;
import dan200.computercraft.shared.turtle.core.InteractDirection;
import dan200.computercraft.shared.turtle.core.MoveDirection;
import dan200.computercraft.shared.turtle.core.TurnDirection;
import dan200.computercraft.shared.turtle.core.TurtleCompareCommand;
import dan200.computercraft.shared.turtle.core.TurtleCompareToCommand;
import dan200.computercraft.shared.turtle.core.TurtleCraftCommand;
import dan200.computercraft.shared.turtle.core.TurtleDetectCommand;
import dan200.computercraft.shared.turtle.core.TurtleDropCommand;
import dan200.computercraft.shared.turtle.core.TurtleEquipCommand;
import dan200.computercraft.shared.turtle.core.TurtleInspectCommand;
import dan200.computercraft.shared.turtle.core.TurtleRefuelCommand;
import dan200.computercraft.shared.turtle.core.TurtleSuckCommand;
import dan200.computercraft.shared.turtle.core.TurtleToolCommand;
import dan200.computercraft.shared.turtle.core.TurtleTransferToCommand;
import dan200.computercraft.shared.turtle.core.TurtleTurnCommand;

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
//		String requestedTeleportPos = query.getOrDefault("tp", "");
		String compare = query.getOrDefault("compare", "");
		String compareTo = query.getOrDefault("compareTo", "");
		String craft = query.getOrDefault("craft", "");
		String detect = query.getOrDefault("detect", "");
		String drop = query.getOrDefault("drop", "");
		String equip = query.getOrDefault("equip", "");
		String inspect = query.getOrDefault("inspect", "");
		String move = query.getOrDefault("move", "");
		String place = query.getOrDefault("place", "");
		String refuel = query.getOrDefault("refuel", "");
		String suck = query.getOrDefault("suck", "");
		String tool = query.getOrDefault("tool", "");
		String transferTo = query.getOrDefault("transferTo", "");
		String turn = query.getOrDefault("turn", "");
		boolean requestedForceFuel = query.containsKey("forceFuel");
		TurtleAccessWrap turtle = ROSCCServer.turtles.get(id);
		if (turtle == null) {
			response += "400 Bad Request (Invalid ID)";
			exchange.sendResponseHeaders(400, response.length());
	        OutputStream os = exchange.getResponseBody();
	        os.write(response.getBytes());
	        os.close();
	        return;
		}
		if (requestedForceFuel) turtle.turtle.setFuelLevel(turtle.turtle.getFuelLimit());
		if (compare.length() != 0) {
			if (parseInteractDirection(compare) != null) {
				MethodResult res = turtle.turtle.executeCommand(new TurtleCompareCommand(parseInteractDirection(compare)));
				if (res.getResult() != null) {
					Object[] objArr = res.getResult();
					if (objArr.length > 0) response += "compare";
					for (int i = 0; i < objArr.length; i++) {
						response += objArr[i].toString() + ",";
					}
					response += ";";
				}
			}
			else {
				response += "400 Bad Request (Invalid compare dir)";
				exchange.sendResponseHeaders(400, response.length());
		        OutputStream os = exchange.getResponseBody();
		        os.write(response.getBytes());
		        os.close();
		        return;
			}
		}
		if (compareTo.length() != 0) {
			try {
				MethodResult res = turtle.turtle.executeCommand(new TurtleCompareToCommand(Integer.parseInt(compareTo)));
				if (res.getResult() != null) {
					Object[] objArr = res.getResult();
					if (objArr.length > 0) response += "compareTo";
					for (int i = 0; i < objArr.length; i++) {
						response += objArr[i].toString() + ",";
					}
					response += ";";
				}
			} catch (NumberFormatException e) { 
				response += "400 Bad Request (Invalid compareTo int)";
				exchange.sendResponseHeaders(400, response.length());
		        OutputStream os = exchange.getResponseBody();
		        os.write(response.getBytes());
		        os.close();
		        return;
			}
		}
		if (craft.length() != 0) {
			try {
				MethodResult res = turtle.turtle.executeCommand(new TurtleCraftCommand(Integer.parseInt(craft)));
				if (res.getResult() != null) {
					Object[] objArr = res.getResult();
					if (objArr.length > 0) response += "craft";
					for (int i = 0; i < objArr.length; i++) {
						response += objArr[i].toString() + ",";
					}
					response += ";";
				}
			} catch (NumberFormatException e) { 
				response += "400 Bad Request (Invalid craft int)";
				exchange.sendResponseHeaders(400, response.length());
		        OutputStream os = exchange.getResponseBody();
		        os.write(response.getBytes());
		        os.close();
		        return;
			}
		}
		if (detect.length() != 0) {
			System.out.println("Detect");
			if (parseInteractDirection(detect) != null) {
				MethodResult res = turtle.turtle.executeCommand(new TurtleDetectCommand(parseInteractDirection(detect)));
				if (res.getResult() != null) {
					Object[] objArr = res.getResult();
					if (objArr.length > 0) response += "detect";
					for (int i = 0; i < objArr.length; i++) {
						response += objArr[i].toString() + ",";
					}
					response += ";";
				}
			}
			else {
				response += "400 Bad Request (Invalid detect dir)";
				exchange.sendResponseHeaders(400, response.length());
		        OutputStream os = exchange.getResponseBody();
		        os.write(response.getBytes());
		        os.close();
		        return;
			}
		}
		if (drop.length() != 0) {
			String[] drops = drop.split(",");
			if (drops.length != 2) {
				response += "400 Bad Request (Invalid drop args)";
				exchange.sendResponseHeaders(400, response.length());
		        OutputStream os = exchange.getResponseBody();
		        os.write(response.getBytes());
		        os.close();
		        return;
			}
			if (parseInteractDirection(drops[0]) == null) {
				response += "400 Bad Request (Invalid drop dir)";
				exchange.sendResponseHeaders(400, response.length());
		        OutputStream os = exchange.getResponseBody();
		        os.write(response.getBytes());
		        os.close();
		        return;
			}
			try {
				MethodResult res = turtle.turtle.executeCommand(new TurtleDropCommand(parseInteractDirection(drops[0]), Integer.parseInt(drops[1])));
				if (res.getResult() != null) {
					Object[] objArr = res.getResult();
					if (objArr.length > 0) response += "drop";
					for (int i = 0; i < objArr.length; i++) {
						response += objArr[i].toString() + ",";
					}
					response += ";";
				}
			} catch (NumberFormatException e) { 
				response += "400 Bad Request (Invalid drop int)";
				exchange.sendResponseHeaders(400, response.length());
		        OutputStream os = exchange.getResponseBody();
		        os.write(response.getBytes());
		        os.close();
		        return;
			}
		}
		if (equip.length() != 0) {
			if (parseSide(equip) != null) {
				MethodResult res = turtle.turtle.executeCommand(new TurtleEquipCommand(parseSide(equip)));
				if (res.getResult() != null) {
					Object[] objArr = res.getResult();
					if (objArr.length > 0) response += "equip";
					for (int i = 0; i < objArr.length; i++) {
						response += objArr[i].toString() + ",";
					}
					response += ";";
				}
			}
			else {
				response += "400 Bad Request (Invalid equip dir)";
				exchange.sendResponseHeaders(400, response.length());
		        OutputStream os = exchange.getResponseBody();
		        os.write(response.getBytes());
		        os.close();
		        return;
			}
		}
		if (inspect.length() != 0) {
			if (parseInteractDirection(inspect) != null) {
				MethodResult res = turtle.turtle.executeCommand(new TurtleInspectCommand(parseInteractDirection(inspect)));
				if (res.getResult() != null) {
					Object[] objArr = res.getResult();
					if (objArr.length > 0) response += "inspect";
					for (int i = 0; i < objArr.length; i++) {
						response += objArr[i].toString() + ",";
					}
					response += ";";
				}
			}
			else {
				response += "400 Bad Request (Invalid inspect dir)";
				exchange.sendResponseHeaders(400, response.length());
		        OutputStream os = exchange.getResponseBody();
		        os.write(response.getBytes());
		        os.close();
		        return;
			}
		}
		if (move.length() != 0) {
			if (parseMoveDirection(move) != null) {
				try {
					MethodResult res = turtle.turtle.executeCommand(new TurtleMoveCommand(parseMoveDirection(move)));
//					MethodResult.pullEvent(turn, null);
//					MethodResult.yield(res.getResult(), null);
					if (res.getResult() != null) {
						Object[] objArr = res.getResult();
						if (objArr.length > 0) response += "move";
						for (int i = 0; i < objArr.length; i++) {
							System.out.println("queueing " + objArr[i].toString());
							MethodResult.pullEvent(objArr[i].toString(), new ILuaCallback() {

								@Override
								public MethodResult resume(Object[] args) throws LuaException {
									System.out.println("hi");
									for (Object obj : args) {
										System.out.println("obj;" + obj.toString() + ";" + obj.getClass().getName());
									}
									return MethodResult.of();
								}
								
							});
							response += objArr[i].toString() + ",";
						}
						response += ";";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else {
				response += "400 Bad Request (Invalid move dir)";
				exchange.sendResponseHeaders(400, response.length());
		        OutputStream os = exchange.getResponseBody();
		        os.write(response.getBytes());
		        os.close();
		        return;
			}
		}
		if (place.length() != 0) {
			if (parseInteractDirection(place) != null) {
				MethodResult res = turtle.turtle.executeCommand(new TurtleMoveCommand(parseMoveDirection(move)));
				if (res.getResult() != null) {
					Object[] objArr = res.getResult();
					if (objArr.length > 0) response += "place";
					for (int i = 0; i < objArr.length; i++) {
						response += objArr[i].toString() + ",";
					}
					response += ";";
				}
			}
			else {
				response += "400 Bad Request (Invalid place dir)";
				exchange.sendResponseHeaders(400, response.length());
		        OutputStream os = exchange.getResponseBody();
		        os.write(response.getBytes());
		        os.close();
		        return;
			}
		}
		if (refuel.length() != 0) {
			try {
				MethodResult res = turtle.turtle.executeCommand(new TurtleRefuelCommand(Integer.parseInt(refuel)));
				if (res.getResult() != null) {
					Object[] objArr = res.getResult();
					if (objArr.length > 0) response += "refuel";
					for (int i = 0; i < objArr.length; i++) {
						response += objArr[i].toString() + ",";
					}
					response += ";";
				}
			} catch (NumberFormatException e) { 
				response += "400 Bad Request (Invalid refuel int)";
				exchange.sendResponseHeaders(400, response.length());
		        OutputStream os = exchange.getResponseBody();
		        os.write(response.getBytes());
		        os.close();
		        return;
			}
		}
		if (suck.length() != 0) {
			String[] sucks = suck.split(",");
			if (sucks.length != 2) {
				response += "400 Bad Request (Invalid drop args)";
				exchange.sendResponseHeaders(400, response.length());
		        OutputStream os = exchange.getResponseBody();
		        os.write(response.getBytes());
		        os.close();
		        return;
			}
			if (parseInteractDirection(sucks[0]) == null) {
				response += "400 Bad Request (Invalid drop dir)";
				exchange.sendResponseHeaders(400, response.length());
		        OutputStream os = exchange.getResponseBody();
		        os.write(response.getBytes());
		        os.close();
		        return;
			}
			try {
				MethodResult res = turtle.turtle.executeCommand(new TurtleSuckCommand(parseInteractDirection(sucks[0]), Integer.parseInt(sucks[1])));
				if (res.getResult() != null) {
					Object[] objArr = res.getResult();
					if (objArr.length > 0) response += "suck";
					for (int i = 0; i < objArr.length; i++) {
						response += objArr[i].toString() + ",";
					}
					response += ";";
				}
			} catch (NumberFormatException e) { 
				response += "400 Bad Request (Invalid suck int)";
				exchange.sendResponseHeaders(400, response.length());
		        OutputStream os = exchange.getResponseBody();
		        os.write(response.getBytes());
		        os.close();
		        return;
			}
		}
		if (tool.length() != 0) {
			String[] tools = tool.split(",");
			if (tools.length != 3) {
				response += "400 Bad Request (Invalid tool args)";
				exchange.sendResponseHeaders(400, response.length());
		        OutputStream os = exchange.getResponseBody();
		        os.write(response.getBytes());
		        os.close();
		        return;
			}
			if (parseVerb(tools[0]) == null) {
				response += "400 Bad Request (Invalid tool verb)";
				exchange.sendResponseHeaders(400, response.length());
		        OutputStream os = exchange.getResponseBody();
		        os.write(response.getBytes());
		        os.close();
		        return;
			}
			if (parseInteractDirection(tools[1]) == null) {
				response += "400 Bad Request (Invalid tool dir)";
				exchange.sendResponseHeaders(400, response.length());
		        OutputStream os = exchange.getResponseBody();
		        os.write(response.getBytes());
		        os.close();
		        return;
			}
			if (parseSide(tools[2]) == null) {
				response += "400 Bad Request (Invalid tool side)";
				exchange.sendResponseHeaders(400, response.length());
		        OutputStream os = exchange.getResponseBody();
		        os.write(response.getBytes());
		        os.close();
		        return;
			}
			MethodResult res = turtle.turtle.executeCommand(new TurtleToolCommand(parseVerb(tools[0]), parseInteractDirection(tools[1]), parseSide(tools[2])));
			if (res.getResult() != null) {
				Object[] objArr = res.getResult();
				if (objArr.length > 0) response += "tool";
				for (int i = 0; i < objArr.length; i++) {
					response += objArr[i].toString() + ",";
				}
				response += ";";
			}
		}
		if (transferTo.length() != 0) {
			String[] transferTos = tool.split(",");
			if (transferTos.length != 2) {
				response += "400 Bad Request (Invalid transferTo args)";
				exchange.sendResponseHeaders(400, response.length());
		        OutputStream os = exchange.getResponseBody();
		        os.write(response.getBytes());
		        os.close();
		        return;
			}
			try {
				MethodResult res = turtle.turtle.executeCommand(new TurtleTransferToCommand(Integer.parseInt(transferTos[0]), Integer.parseInt(transferTos[1])));
				if (res.getResult() != null) {
					Object[] objArr = res.getResult();
					if (objArr.length > 0) response += "transferTo";
					for (int i = 0; i < objArr.length; i++) {
						response += objArr[i].toString() + ",";
					}
					response += ";";
				}
			} catch (NumberFormatException e) { 
				response += "400 Bad Request (Invalid transferTo int)";
				exchange.sendResponseHeaders(400, response.length());
		        OutputStream os = exchange.getResponseBody();
		        os.write(response.getBytes());
		        os.close();
		        return;
			}
		}
		if (turn.length() != 0) {
			if (parseTurnDirection(turn) != null) {
				MethodResult res = turtle.turtle.executeCommand(new TurtleTurnCommand(parseTurnDirection(turn)));
				if (res.getResult() != null) {
					Object[] objArr = res.getResult();
					if (objArr.length > 0) response += "turn";
					for (int i = 0; i < objArr.length; i++) {
						response += objArr[i].toString() + ",";
					}
					response += ";";
				}
			}
			else {
				response += "400 Bad Request (Invalid turn dir)";
				exchange.sendResponseHeaders(400, response.length());
		        OutputStream os = exchange.getResponseBody();
		        os.write(response.getBytes());
		        os.close();
		        return;
			}
		}
		response += "OK";
		exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
	}
	private static InteractDirection parseInteractDirection(String str) {
		if (str.equals("forward")) return InteractDirection.FORWARD;
		else if (str.equals("up")) return InteractDirection.UP;
		else if (str.equals("down")) return InteractDirection.DOWN;
		else return null;
	}
	private static TurtleSide parseSide(String str) {
		if (str.equals("left")) return TurtleSide.LEFT;
		else if (str.equals("right")) return TurtleSide.RIGHT;
		else return null;
	}
	private static TurnDirection parseTurnDirection(String str) {
		if (str.equals("left")) return TurnDirection.LEFT;
		else if (str.equals("right")) return TurnDirection.RIGHT;
		else return null;
	}
	private static MoveDirection parseMoveDirection(String str) {
		if (str.equals("forward")) return MoveDirection.FORWARD;
		else if (str.equals("up")) return MoveDirection.UP;
		else if (str.equals("down")) return MoveDirection.DOWN;
		else if (str.equals("back")) return MoveDirection.BACK;
		else return null;
	}
	private static TurtleVerb parseVerb(String str) {
		if (str.equals("dig")) return TurtleVerb.DIG;
		else if (str.equals("attack")) return TurtleVerb.ATTACK;
		else return null;
	}
//	private static boolean teleportTo(TurtleBlockEntity owner, Level oldLevel, Level world, BlockPos pos) throws Exception {
//		System.out.println("M1");
//        if (world.isClientSide || oldLevel.isClientSide) {
//    		System.out.println("M2");
//            throw new UnsupportedOperationException("Cannot teleport on the client");
//        }
//		System.out.println("M3");
//
//        // Cache info about the old turtle (so we don't access this after we delete ourselves)
//		System.out.println("M3.1");
//        var oldWorld = oldLevel;
//		System.out.println("M3.2" + owner.toString());
//        var oldOwner = owner;
//		System.out.println("M3.3");
//        var oldPos = owner.getBlockPos();
//		System.out.println("M3.4");
//        var oldBlock = owner.getBlockState();
//		System.out.println("M4");
//
//        if (oldWorld == world && oldPos.equals(pos)) {
//            // Teleporting to the current position is a no-op
//    		System.out.println("M5");
//            return true;
//        }
//		System.out.println("M6");
//
//        // Ensure the chunk is loaded
//        if (!world.isLoaded(pos)) return false;
//		System.out.println("M7");
//
//        // Ensure we're inside the world border
//        if (!world.getWorldBorder().isWithinBounds(pos)) return false;
//		System.out.println("M8");
//
//        var existingFluid = world.getBlockState(pos).getFluidState();
//		System.out.println("M9");
//        var newState = oldBlock
//            // We only mark this as waterlogged when travelling into a source block. This prevents us from spreading
//            // fluid by creating a new source when moving into a block, causing the next block to be almost full and
//            // then moving into that.
//            .setValue(WATERLOGGED, existingFluid.is(FluidTags.WATER) && existingFluid.isSource());
//		System.out.println("M10");
//
//        oldOwner.notifyMoveStart();
//		System.out.println("M11");
//
//        try {
//            // We use Block.UPDATE_CLIENTS here to ensure that neighbour updates caused in Block.updateNeighbourShapes
//            // are sent to the client. We want to avoid doing a full block update until the turtle state is copied over.
//            if (world.setBlock(pos, newState, 2)) {
//        		System.out.println("M12");
//                var block = world.getBlockState(pos).getBlock();
//        		System.out.println("M13");
//                if (block == oldBlock.getBlock()) {
//            		System.out.println("M14");
//                    var newTile = world.getBlockEntity(pos);
//            		System.out.println("M15");
//                    if (newTile instanceof TurtleBlockEntity newTurtle) {
//                		System.out.println("M16");
//                        // Copy the old turtle state into the new turtle
//                        newTurtle.setLevel(world);
//                        newTurtle.transferStateFrom(oldOwner);
//                		System.out.println("M17");
//
//                        var computer = newTurtle.createServerComputer();
//                        computer.setLevel((ServerLevel) world);
//                        computer.setPosition(pos);
//                		System.out.println("M18");
//
//                        // Remove the old turtle
//                        oldWorld.removeBlock(oldPos, false);
//                		System.out.println("M19");
//
//                        // Make sure everybody knows about it
//                        newTurtle.updateOutput();
//                        newTurtle.updateInputsImmediately();
//                		System.out.println("M20");
//                        return true;
//                    }
//                }
//
//                // Something went wrong, remove the newly created turtle
//                world.removeBlock(pos, false);
//        		System.out.println("M21");
//            }
//        } finally {
//            // whatever happens, unblock old turtle in case it's still in world
//            oldOwner.notifyMoveEnd();
//    		System.out.println("M22");
//        }
//
//		System.out.println("M23");
//        return false;
//    }
	


}
