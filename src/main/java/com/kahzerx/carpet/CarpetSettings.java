package com.kahzerx.carpet;

import com.kahzerx.carpet.api.settings.CarpetRule;
import com.kahzerx.carpet.api.settings.Rule;
import com.kahzerx.carpet.api.settings.Validator;
import com.kahzerx.carpet.utils.Translations;
import net.minecraft.server.command.source.CommandSourceStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.kahzerx.carpet.api.settings.RuleCategory.CREATIVE;
import static com.kahzerx.carpet.api.settings.RuleCategory.FEATURE;

public class CarpetSettings {
	public static final String carpetVersion = "1.0.0";
	public static final String releaseTarget = "1.13.2";
	public static final Logger LOG = LogManager.getLogger("carpet");

	private static class LanguageValidator extends Validator<String> {
		@Override public String validate(CommandSourceStack source, CarpetRule<String> currentRule, String newValue, String string) {
			CarpetSettings.language = newValue;
			Translations.updateLanguage();
			return newValue;
		}
	}
	@Rule(
			desc = "Sets the language for Carpet",
			categories = FEATURE,
			options = {"en_us"},
			validators = LanguageValidator.class
	)
	public static String language = "en_us";

	private static class CarpetPermissionLevel extends Validator<String> {
		@Override
		public String validate(CommandSourceStack source, CarpetRule<String> currentRule, String newValue, String string) {
			if (source == null || source.hasPermissions(4)) {
				return newValue;
			}
			return null;
		}

		@Override
		public String description() {
			return "This setting can only be set by admins with op level 4";
		}
	}
	@Rule(
			desc = "Carpet command permission level",
			categories = CREATIVE,
			validators = CarpetPermissionLevel.class,
			options = {"ops", "2", "4"}
	)
	public static String carpetCommandPermissionLevel = "ops";
}
