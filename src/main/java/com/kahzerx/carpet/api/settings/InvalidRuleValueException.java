package com.kahzerx.carpet.api.settings;

import com.kahzerx.carpet.utils.Messenger;
//#if MC>=11300
import net.minecraft.server.command.source.CommandSourceStack;
//#else
//$$ import net.minecraft.server.command.source.CommandSource;
//#endif

public class InvalidRuleValueException extends Exception {
	public InvalidRuleValueException(String cause) {
		super(cause);
	}

	public InvalidRuleValueException() {
		super();
	}

	//#if MC>=11300
	public void notifySource(String ruleName, CommandSourceStack source) {
	//#else
	//$$ public void notifySource(String ruleName, CommandSource source) {
	//#endif
		if (getMessage() != null) {
			Messenger.m(source, "r Couldn't set value for rule " + ruleName + ": "+ getMessage());
		}
	}
}
