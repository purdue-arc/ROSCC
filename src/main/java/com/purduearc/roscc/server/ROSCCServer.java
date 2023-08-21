package com.purduearc.roscc.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

import com.sun.net.httpserver.HttpServer;

public class ROSCCServer implements Runnable {
	private InetAddress address;
	private final int port;
	public static final HashMap<Integer, TurtleAccessWrap> turtles = new HashMap<Integer, TurtleAccessWrap>();
	private HttpServer server = null;
	private boolean running = false;
	public ROSCCServer(String address, int port) {
		try {
			this.address = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			this.address = null;
			e.printStackTrace();
		}
		this.port = port;
	}
	@Override
	public void run() {
		try {
			server = HttpServer.create(new InetSocketAddress(address, port), 0);
	        server.createContext("/status", new CHandlerStatus());
	        server.setExecutor(null);
	        server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		running = true;
	}
	public void stop() {
        server.stop(1);
	}
	public boolean isRunning() {
		return running;
	}

}
