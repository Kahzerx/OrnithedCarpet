package com.kahzerx.carpet.api.settings;

import com.kahzerx.carpet.utils.CommandHelper;
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
		if (description() != null) {
			Messenger.m(source, "r " + description());
		}
	}

	static class _COMMAND<T> extends Validator<T> {
		@Override
		public T validate(CommandSourceStack source, CarpetRule<T> currentRule, T newValue, String string) {
			if (source != null) {
				CommandHelper.notifyPlayersCommandsChanged(source.getServer());
			}
			return newValue;
		}
		@Override
		public String description() { return "It has an accompanying command";}
	}

	// maybe remove this one and make printRulesToLog check for canBeToggledClientSide instead
	static class _CLIENT<T> extends Validator<T> {
		@Override
		public T validate(CommandSourceStack source, CarpetRule<T> currentRule, T newValue, String string) {
			return newValue;
		}
		@Override
		public String description() { return "Its a client command so can be issued and potentially be effective when connecting to non-carpet/vanilla servers. " +
				"In these situations (on vanilla servers) it will only affect the executing player, so each player needs to type it" +
				" separately for the desired effect";}
	}

	static class StrictValidator<T> extends Validator<T> {
		@Override
		public T validate(CommandSourceStack source, CarpetRule<T> currentRule, T newValue, String string) {
			if (!currentRule.suggestions().contains(string)) {
				Messenger.m(source, "r Valid options: " + currentRule.suggestions().toString());
				return null;
			}
			return newValue;
		}
	}
}
