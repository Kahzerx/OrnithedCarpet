package com.kahzerx.carpet.api.settings;

import com.kahzerx.carpet.utils.CommandHelper;
import com.kahzerx.carpet.utils.Messenger;
//#if MC>=11200
import net.minecraft.server.command.source.CommandSourceStack;
//#else
//$$ import net.minecraft.server.command.source.CommandSource;
//#endif

public abstract class Validator<T> {
	//#if MC>=11200
	public abstract T validate(CommandSourceStack source, CarpetRule<T> changingRule, T newValue, String userInput);
	//#else
	//$$ public abstract T validate(CommandSource source, CarpetRule<T> changingRule, T newValue, String userInput);
	//#endif

	public String description() {
		return null;
	}

	//#if MC>=11200
	public void notifyFailure(CommandSourceStack source, CarpetRule<T> currentRule, String providedValue) {
	//#else
	//$$ public void notifyFailure(CommandSource source, CarpetRule<T> currentRule, String providedValue) {
	//#endif
		Messenger.m(source, "r Wrong value for " + currentRule.name() + ": " + providedValue);
		if (description() != null) {
			Messenger.m(source, "r " + description());
		}
	}

	static class _COMMAND<T> extends Validator<T> {
		@Override
		//#if MC>=11200
		public T validate(CommandSourceStack source, CarpetRule<T> currentRule, T newValue, String string) {
		//#else
		//$$ public T validate(CommandSource source, CarpetRule<T> currentRule, T newValue, String string) {
		//#endif
			if (source != null) {
				//#if MC>=11300
				CommandHelper.notifyPlayersCommandsChanged(source.getServer());  // TODO no need to notify with old command system? can't send command tree
				//#endif
			}
			return newValue;
		}
		@Override
		public String description() { return "It has an accompanying command";}
	}

	// maybe remove this one and make printRulesToLog check for canBeToggledClientSide instead
	static class _CLIENT<T> extends Validator<T> {
		@Override
		//#if MC>=11200
		public T validate(CommandSourceStack source, CarpetRule<T> currentRule, T newValue, String string) {
		//#else
		//$$ public T validate(CommandSource source, CarpetRule<T> currentRule, T newValue, String string) {
		//#endif
			return newValue;
		}
		@Override
		public String description() { return "Its a client command so can be issued and potentially be effective when connecting to non-carpet/vanilla servers. " +
				"In these situations (on vanilla servers) it will only affect the executing player, so each player needs to type it" +
				" separately for the desired effect";}
	}

	static class StrictValidator<T> extends Validator<T> {
		@Override
		//#if MC>=11200
		public T validate(CommandSourceStack source, CarpetRule<T> currentRule, T newValue, String string) {
		//#else
		//$$ public T validate(CommandSource source, CarpetRule<T> currentRule, T newValue, String string) {
		//#endif
			if (!currentRule.suggestions().contains(string)) {
				Messenger.m(source, "r Valid options: " + currentRule.suggestions().toString());
				return null;
			}
			return newValue;
		}
	}
}
