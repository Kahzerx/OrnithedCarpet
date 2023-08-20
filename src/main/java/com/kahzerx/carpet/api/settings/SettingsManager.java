package com.kahzerx.carpet.api.settings;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.source.CommandSourceStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsManager {
	private final Map<String, CarpetRule<?>> rules = new HashMap<>();
	private final String version;
	private final String identifier;
	private final String fancyName;
	private boolean locked;
	private MinecraftServer server;
	private final List<RuleObserver> observers = new ArrayList<>();
	private static final List<RuleObserver> staticObservers = new ArrayList<>();

	public SettingsManager(String version, String identifier, String fancyName) {
		this.version = version;
		this.identifier = identifier;
		this.fancyName = fancyName;
	}

	@FunctionalInterface
	public interface RuleObserver {
		void ruleChanged(CommandSourceStack source, CarpetRule<?> changedRule, String userInput);
	}

	public void registerRuleObserver(RuleObserver observer) {
		this.observers.add(observer);
	}

	public static void registerGlobalRuleObserver(RuleObserver observer) {
		staticObservers.add(observer);
	}

	public String identifier() {
		return this.identifier;
	}

	public boolean locked() {
		return this.locked;
	}

	static class ConfigReadResult {
		private final Map<String, String> ruleMap;
		private final boolean locked;
		public ConfigReadResult(Map<String, String> ruleMap, boolean locked) {
			this.ruleMap = ruleMap;
			this.locked = locked;
		}

		public Map<String, String> getRuleMap() {
			return ruleMap;
		}

		public boolean isLocked() {
			return locked;
		}
	}
}
