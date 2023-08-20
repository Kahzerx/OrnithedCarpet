package com.kahzerx.carpet.api.settings;

import com.kahzerx.carpet.utils.Messenger;
import net.minecraft.server.command.source.CommandSourceStack;

public abstract class Validator<T> {
	public abstract T validate(CommandSourceStack source, CarpetRule<T> changingRule, T newValue, String userInput);

	public String description() {
		return null;
	}

	public void notifyFailure(CommandSourceStack source, CarpetRule<T> currentRule, String providedValue)
	{
		Messenger.m(source, "r Wrong value for " + currentRule.name() + ": " + providedValue);
		if (description() != null)
			Messenger.m(source, "r " + description());
	}
}
