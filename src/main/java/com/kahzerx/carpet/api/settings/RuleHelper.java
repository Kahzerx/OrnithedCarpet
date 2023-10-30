package com.kahzerx.carpet.api.settings;

import com.kahzerx.carpet.utils.TranslationKeys;
import com.kahzerx.carpet.utils.Translations;
//#if MC>=11300
import net.minecraft.server.command.source.CommandSourceStack;
//#else
//$$ import net.minecraft.server.command.source.CommandSource;
//#endif

import java.util.Locale;

public final class RuleHelper {
	private RuleHelper() {}

	public static boolean getBooleanValue(CarpetRule<?> rule) {
		if (rule.type() == Boolean.class) return (boolean) rule.value();
		if (Number.class.isAssignableFrom(rule.type())) return ((Number) rule.value()).doubleValue() > 0;
		return false;
	}

	public static String toRuleString(Object value) {
		if (value instanceof Enum) return ((Enum<?>) value).name().toLowerCase(Locale.ROOT);
		return value.toString();
	}

	public static boolean isInDefaultValue(CarpetRule<?> rule) {
		return rule.defaultValue().equals(rule.value());
	}

	//#if MC>=11300
	public static <T> void resetToDefault(CarpetRule<T> rule, CommandSourceStack source) {
	//#else
	//$$ public static <T> void resetToDefault(CarpetRule<T> rule, CommandSource source) {
	//#endif
		try {
			rule.set(source, rule.defaultValue());
		} catch (InvalidRuleValueException e) {
			throw new IllegalStateException("Rule couldn't be set to default value!", e);
		}
	}

	public static String translatedName(CarpetRule<?> rule) {
		String key = String.format(TranslationKeys.RULE_NAME_PATTERN, rule.settingsManager().identifier(), rule.name());
		return Translations.hasTranslation(key) ? Translations.tr(key) + String.format(" (%s)", rule.name()): rule.name();
	}

	public static String translatedDescription(CarpetRule<?> rule) {
		return Translations.tr(String.format(TranslationKeys.RULE_DESC_PATTERN, rule.settingsManager().identifier(), rule.name()), rule.desc());
	}

	public static String translatedCategory(String manager, String category) {
		return Translations.tr(String.format(TranslationKeys.CATEGORY_PATTERN, manager, category), category);
	}
}
