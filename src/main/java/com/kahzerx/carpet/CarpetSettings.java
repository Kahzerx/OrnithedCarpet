package com.kahzerx.carpet;

import com.kahzerx.carpet.api.settings.CarpetRule;
import com.kahzerx.carpet.api.settings.Rule;
import com.kahzerx.carpet.api.settings.Validator;
import com.kahzerx.carpet.utils.Translations;
import net.minecraft.server.command.source.CommandSourceStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.kahzerx.carpet.api.settings.RuleCategory.*;

public class CarpetSettings {
	public static final String carpetVersion = "1.0.0";
	public static final String releaseTarget = "1.13.2";
	public static final Logger LOG = LogManager.getLogger("carpet");

	private static class LanguageValidator extends Validator<String> {
		@Override
		public String validate(CommandSourceStack source, CarpetRule<String> currentRule, String newValue, String string) {
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

	private static class OneHourMaxDelayLimit extends Validator<Integer> {
		@Override
		public Integer validate(CommandSourceStack source, CarpetRule<Integer> currentRule, Integer newValue, String string) {
			System.out.println(newValue > 0 && newValue <= 72000);
			return (newValue > 0 && newValue <= 72000) ? newValue : null;
		}

		@Override
		public String description() {
			return "You must choose a value from 1 to 72000";
		}
	}
	@Rule(
			desc = "Amount of delay ticks to use a nether portal in creative",
			options = {"1", "40", "80", "72000"},
			categories = CREATIVE,
			strict = false,
			validators = OneHourMaxDelayLimit.class
	)
	public static int portalCreativeDelay = 1;
	@Rule(
			desc = "Amount of delay ticks to use a nether portal in survival",
			options = {"1", "40", "80", "72000"},
			categories = SURVIVAL,
			strict = false,
			validators = OneHourMaxDelayLimit.class
	)
	public static int portalSurvivalDelay = 80;

	@Rule(desc = "Parrots don't get of your shoulders until you receive proper damage", categories = {SURVIVAL, FEATURE})
	public static boolean persistentParrots = false;
}
