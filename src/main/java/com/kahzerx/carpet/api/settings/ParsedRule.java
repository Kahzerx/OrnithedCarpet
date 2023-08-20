package com.kahzerx.carpet.api.settings;

import com.kahzerx.carpet.utils.TranslationKeys;
import com.kahzerx.carpet.utils.Translations;
import net.minecraft.server.command.source.CommandSourceStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.apache.commons.lang3.ClassUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ParsedRule<T> implements CarpetRule<T>, Comparable<ParsedRule<?>> {
	private final String name;
	private final List<Text> extraInfo;
	private final List<String> categories;
	private final List<String> options;
	private boolean isStrict;
	private final List<Validator<T>> validators;
	private final SettingsManager settingsManager;
	private final Field field;
	private final boolean isClient;
	private final Class<T> type;
	private final T defaultValue;
	private final FromStringConverter<T> converter;

	private static final Map<Class<?>, FromStringConverter<?>> CONVERTER_MAP = initConverterMap();

	private static Map<Class<?>, FromStringConverter<?>> initConverterMap() {
		Map<Class<?>, FromStringConverter<?>> converter = new HashMap<>();
		Map.Entry<Class<String>, FromStringConverter<String>> stringConverter = new AbstractMap.SimpleEntry<>(String.class, str -> str);
 		Map.Entry<Class<Boolean>, FromStringConverter<Boolean>> boolConverter = simpleConverter(Boolean.class, Boolean::parseBoolean);
		Map.Entry<Class<Integer>, FromStringConverter<Integer>> intConverter = simpleConverter(Integer.class, Integer::parseInt);
		Map.Entry<Class<Double>, FromStringConverter<Double>> doubleConverter = simpleConverter(Double.class, Double::parseDouble);
		Map.Entry<Class<Long>, FromStringConverter<Long>> longConverter = simpleConverter(Long.class, Long::parseLong);
		Map.Entry<Class<Float>, FromStringConverter<Float>> floatConverter = simpleConverter(Float.class, Float::parseFloat);
		converter.put(stringConverter.getKey(), stringConverter.getValue());
		converter.put(boolConverter.getKey(), boolConverter.getValue());
		converter.put(intConverter.getKey(), boolConverter.getValue());
		converter.put(doubleConverter.getKey(), boolConverter.getValue());
		converter.put(longConverter.getKey(), boolConverter.getValue());
		converter.put(floatConverter.getKey(), boolConverter.getValue());
		return converter;
	}

	public ParsedRule(Field field, Rule rule, SettingsManager settingsManager) {
		this.name = rule.name();
		this.field = field;
		String extraPrefix = String.format(TranslationKeys.RULE_EXTRA_PREFIX_PATTERN, settingsManager.identifier(), name());
		this.extraInfo = getTranslationArray(extraPrefix);
		@SuppressWarnings("unchecked")
		Class<T> type = (Class<T>) ClassUtils.primitiveToWrapper(field.getType());
		this.type = type;
		this.categories = Arrays.asList(rule.categories());
		this.isStrict = rule.strict();
		FromStringConverter<T> converter0 = null;
		if (rule.options().length > 0) {
			this.options = Arrays.asList(rule.options());
		} else if (type == Boolean.class) {
			this.options = Arrays.asList("true", "false");
		} else if (type == String.class && this.categories.contains(RuleCategory.COMMAND)) {
			this.options = Validators.CommandLevel.OPTIONS;
		} else if (type.isEnum()) {
			this.options = Arrays.stream(type.getEnumConstants()).map(e -> ((Enum<?>) e).name().toLowerCase(Locale.ROOT)).collect(Collectors.toList());
			converter0 = str -> {
				try {
					@SuppressWarnings({"unchecked", "rawtypes"}) // Raw necessary because of signature. Unchecked because compiler doesn't know T extends Enum
					T ret = (T)Enum.valueOf((Class<? extends Enum>) type, str.toUpperCase(Locale.ROOT));
					return ret;
				} catch (IllegalArgumentException e) {
					throw new InvalidRuleValueException("Valid values for this rule are: " + this.options);
				}
			};
		} else {
			this.options = Collections.emptyList();
		}
		if (converter0 == null) {
			@SuppressWarnings("unchecked")
			FromStringConverter<T> converterFromMap = (FromStringConverter<T>)CONVERTER_MAP.get(type);
			if (converterFromMap == null) {
				throw new UnsupportedOperationException("Unsupported type for ParsedRule" + type);
			}
			converter0 = converterFromMap;
		}
		this.converter = converter0;
		this.settingsManager = settingsManager;
		List<Validator<T>> tempValidators = Stream.of(rule.validators()).map(this::instantiateValidator).collect(Collectors.toList());
		if (categories.contains(RuleCategory.COMMAND)) {
			tempValidators.add(new Validator._COMMAND<T>());
			if (type == String.class) {
				tempValidators.add(instantiateValidator(Validators.CommandLevel.class));
			}
		}
		this.isClient = this.categories.contains(RuleCategory.CLIENT);
		if (this.isClient) {
			tempValidators.add(new Validator._CLIENT<>());
		}
		if (isStrict && !this.options.isEmpty()) {
			tempValidators.add(0, new Validator.StrictValidator<>()); // at 0 prevents validators with side effects from running when invalid
		}
		this.validators = tempValidators.stream().filter(Objects::nonNull).collect(Collectors.toList());
		this.defaultValue = this.value();
	}

	@FunctionalInterface
	interface FromStringConverter<T> {
		T convert(String value) throws InvalidRuleValueException;
	}

	@SuppressWarnings({"unchecked", "rawtypes"}) // Needed because of the annotation
	private Validator<T> instantiateValidator(Class<? extends Validator> cls) {
		try {
			Constructor<? extends Validator> constr = cls.getDeclaredConstructor();
			constr.setAccessible(true);
			return constr.newInstance();
		} catch (ReflectiveOperationException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private static <T> Map.Entry<Class<T>, FromStringConverter<T>> simpleConverter(Class<T> outputClass, Function<String, T> converter) {
		return new AbstractMap.SimpleEntry<>(outputClass, str -> {
			try {
				return converter.apply(str);
			} catch (NumberFormatException e) {
				throw new InvalidRuleValueException("Invalid number for rule");
			}
		});
	}

	private List<Text> getTranslationArray(String prefix) {
		List<Text> ret = new ArrayList<>();
		for (int i = 0; Translations.hasTranslation(prefix + i); i++) {
			ret.add(new LiteralText(Translations.tr(prefix + i)));
		}
		return ret;
	}

	@Override
	public String name() {
		return this.name;
	}

	@Override
	public List<Text> extraInfo() {
		return this.extraInfo;
	}

	@Override
	public Collection<String> categories() {
		return this.categories;
	}

	@Override
	public Collection<String> suggestions() {
		return this.options;
	}

	@Override
	public SettingsManager settingsManager() {
		return this.settingsManager;
	}

	@Override
	public T value() {
		try {
			return (T) this.field.get(null);
		} catch (IllegalAccessException e) {
			// Can't happen at regular runtime because we'd have thrown it on construction
			throw new IllegalArgumentException("Couldn't access field for rule: " + name, e);
		}
	}

	@Override
	public boolean canBeToggledClientSide() {
		return this.isClient;
	}

	@Override
	public Class<T> type() {
		return this.type;
	}

	@Override
	public T defaultValue() {
		return this.defaultValue;
	}

	@Override
	public void set(CommandSourceStack source, String value) throws InvalidRuleValueException {
		set(source, converter.convert(value), value);
	}

	@Override
	public void set(CommandSourceStack source, T value) throws InvalidRuleValueException {
		set(source, value, RuleHelper.toRuleString(value));
	}

	@Override
	public boolean strict() {
		return !validators.isEmpty() && validators.get(0) instanceof Validator.StrictValidator;
	}

	private void set(CommandSourceStack source, T value, String userInput) throws InvalidRuleValueException {
		for (Validator<T> validator : this.validators) {
			value = validator.validate(source, this, value, userInput); // should this recalculate the string? Another validator may have changed value
			if (value == null) {
				if (source != null) {
					validator.notifyFailure(source, this, userInput);
				}
				throw new InvalidRuleValueException();
			}
		}
		if (!value.equals(value()) || source == null) {
			try {
				this.field.set(null, value);
			} catch (IllegalAccessException e) {
				throw new IllegalStateException("Couldn't access field for rule: " + name, e);
			}
			if (source != null) {
				settingsManager().notifyRuleChanged(source, this, userInput);
			}
		}
	}

	@Override
	public int compareTo(@NotNull ParsedRule<?> o) {
		return this.name().compareTo(o.name());
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof ParsedRule && ((ParsedRule<?>) obj).name.equals(this.name);
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

	@Override
	public String toString() {
		return this.name + ": " + RuleHelper.toRuleString(value());
	}
}
