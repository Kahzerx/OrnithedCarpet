package com.kahzerx.carpet;

import com.kahzerx.carpet.api.settings.CarpetRule;
import com.kahzerx.carpet.api.settings.Rule;
import com.kahzerx.carpet.api.settings.Validator;
import com.kahzerx.carpet.utils.Translations;
import net.minecraft.server.command.source.CommandSourceStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.kahzerx.carpet.api.settings.RuleCategory.FEATURE;

public class CarpetSettings {
	public static final String carpetVersion = "1.0.0";
	public static final String releaseTarget = "1.19.4";
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
}
