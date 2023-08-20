package com.kahzerx.carpet.api.settings;

import net.minecraft.server.command.source.CommandSourceStack;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.List;

public interface CarpetRule<T> {
	String name();
	List<Text> extraInfo();
	Collection<String> categories();
	Collection<String> suggestions();
	SettingsManager settingsManager();
	T value();
	boolean canBeToggledClientSide();
	Class<T> type();
	T defaultValue();
	default boolean strict() {
		return false;
	}
	void set(CommandSourceStack source, String value) throws InvalidRuleValueException;
	void set(CommandSourceStack source, T value) throws InvalidRuleValueException;
}
