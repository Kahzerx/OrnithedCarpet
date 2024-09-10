package com.kahzerx.carpet;

import com.kahzerx.carpet.api.settings.CarpetRule;
import com.kahzerx.carpet.api.settings.Rule;
import com.kahzerx.carpet.api.settings.Validator;
import com.kahzerx.carpet.fakes.ChunkMapAccess;
import com.kahzerx.carpet.utils.Translations;
import net.minecraft.entity.living.player.PlayerEntity;
//#if MC>=11300
import net.minecraft.server.command.source.CommandSourceStack;
//#else
//$$ import net.minecraft.server.command.source.CommandSource;
//#endif
//#if MC<=10809
//$$ import net.minecraft.server.MinecraftServer;
//#endif
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.kahzerx.carpet.api.settings.RuleCategory.*;

public class CarpetSettings {
	public static final String carpetVersion = "0.3.0";
	public static final Logger LOG = LogManager.getLogger("carpet");

	private static class LanguageValidator extends Validator<String> {
		@Override
		//#if MC>=11300
		public String validate(CommandSourceStack source, CarpetRule<String> currentRule, String newValue, String string) {
		//#else
		//$$ public String validate(CommandSource source, CarpetRule<String> currentRule, String newValue, String string) {
		//#endif
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
		//#if MC>=11300
		public String validate(CommandSourceStack source, CarpetRule<String> currentRule, String newValue, String string) {
		//#else
		//$$ public String validate(CommandSource source, CarpetRule<String> currentRule, String newValue, String string) {
		//#endif
			//#if MC>=11300
			if (source == null || source.hasPermissions(4)) {
			//#else
			//$$ if (source == null || source.canUseCommand(4, source.getName())) {
			//#endif
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
		//#if MC>=11300
		public Integer validate(CommandSourceStack source, CarpetRule<Integer> currentRule, Integer newValue, String string) {
		//#else
		//$$ public Integer validate(CommandSource source, CarpetRule<Integer> currentRule, Integer newValue, String string) {
		//#endif
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

	//#if MC>=11200
	@Rule(desc = "Parrots don't get of your shoulders until you receive proper damage", categories = {SURVIVAL, FEATURE})
	public static boolean persistentParrots = false;
	//#endif

	@Rule(desc = "Players absorb XP instantly, without delay", categories = CREATIVE)
	public static boolean xpNoCooldown = false;

	private static class PushLimitLimits extends Validator<Integer> {
		@Override
		//#if MC>=11300
		public Integer validate(CommandSourceStack source, CarpetRule<Integer> currentRule, Integer newValue, String userInput) {
		//#else
		//$$ public Integer validate(CommandSource source, CarpetRule<Integer> currentRule, Integer newValue, String userInput) {
		//#endif
			return (newValue > 0 && newValue <= 1024) ? newValue : null;
		}

		@Override
		public String description() { return "You must choose a value from 1 to 1024";}
	}
	@Rule(
		desc = "Customizable piston push limit",
		options = {"10", "12", "14", "100"},
		categories = CREATIVE,
		strict = false,
		validators = PushLimitLimits.class
	)
	public static int pushLimit = 12;

	@Rule(
		desc = "Customizable powered rail power range",
		options = {"9", "15", "30"},
		categories = CREATIVE,
		strict = false,
		validators = PushLimitLimits.class
	)
	public static int railPowerLimit = 9;

	@Rule(
		desc = "Creative No Clip",
		extra = {
			"On servers it needs to be set on both ",
			"client and server to function properly.",
			"Has no effect when set on the server only",
			"Can allow to phase through walls",
			"if only set on the carpet client side",
			"but requires some trapdoor magic to",
			"allow the player to enter blocks"
		},
		categories = {CREATIVE, CLIENT}
	)
	public static boolean creativeNoClip = false;

	@Rule(desc = "Explosions won't destroy blocks", categories = {CREATIVE, TNT})
	public static boolean explosionNoBlockDamage = false;

	@Rule(desc = "TNT doesn't update when placed against a power source", categories = {CREATIVE, TNT})
	public static boolean tntDoNotUpdate = false;

	@Rule(desc = "Enables /tick command to control game clocks", categories = COMMAND, options = {"ops", "2", "4"})
	public static String commandTick = "ops";

	@Rule(desc = "Gbhs sgnf sadsgras fhskdpri!!!", categories = EXPERIMENTAL)
	public static boolean superSecretSetting = false;

	private static class QuasiConnectivityValidator extends Validator<Integer> {
		@Override
		//#if MC>=11300
		public Integer validate(CommandSourceStack source, CarpetRule<Integer> changingRule, Integer newValue, String userInput) {
		//#else
		//$$ public Integer validate(CommandSource source, CarpetRule<Integer> changingRule, Integer newValue, String userInput) {
		//#endif
			int minRange = 0;
			int maxRange = 256-1;
			return (newValue >= minRange && newValue <= maxRange) ? newValue : null;
		}
	}

	@Rule(
		desc = "Pistons, droppers, and dispensers check for power to the block(s) above them.",
		extra = {
			"Defines the range at which pistons, droppers, and dispensers check for 'quasi power'."
		},
		categories = CREATIVE,
		options = {"0", "1", "2", "3"},
		strict = false,
		validators = QuasiConnectivityValidator.class
	)
	public static int quasiConnectivity = 1;

	@Rule(
		desc = "hoppers pointing to wool will count items passing through them",
		extra = {
			"Enables /counter command, and actions while placing red and green carpets on wool blocks",
			"Use /counter <color?> reset to reset the counter, and /counter <color?> to query",
			"In survival, place green carpet on same color wool to query, red to reset the counters",
			"Counters are global and shared between players, 16 channels available",
			"Items counted are destroyed, count up to one stack per tick per hopper"
		},
		categories = { COMMAND, CREATIVE, FEATURE }
	)
	public static boolean hopperCounters = false;

	//#if MC<10809
	//$$	@Rule(desc = "Enables /fill command", categories = COMMAND, options = {"ops", "2", "4"})
	//$$	public static String commandFill = "ops";
	//#endif

	private static class FillLimitLimits extends Validator<Integer> {
		@Override
		//#if MC>=11300
		public Integer validate(CommandSourceStack source, CarpetRule<Integer> changingRule, Integer newValue, String userInput) {
		//#else
		//$$ public Integer validate(CommandSource source, CarpetRule<Integer> changingRule, Integer newValue, String userInput) {
		//#endif
			return (newValue>0 && newValue < 20000000) ? newValue : null;
		}
		@Override
		public String description() {
			return "You must choose a value from 1 to 20M";
		}
	}
	@Rule(
		desc = "Customizable fill volume limit",
		options = {"32768", "250000", "1000000"},
		categories = CREATIVE,
		strict = false,
		validators = FillLimitLimits.class
	)
	public static int fillLimit = 32768;

	@Rule(
		desc = "Makes all emerald ore blocks act as an update suppressor",
		categories = CREATIVE,
		strict = false
	)
	public static boolean emeraldOreUpdateSuppressor = false;

	@Rule(
		desc = "smooth client animations with low tps settings",
		extra = "works only in SP, and will slow down players",
		categories = { CREATIVE, SURVIVAL, CLIENT }
	)
	public static boolean smoothClientAnimations = false;

	//#if MC>=10900
	private static class CreativePlayersLoadChunksValidator extends Validator<Boolean> {
		@Override
		//#if MC>=11300
		public Boolean validate(CommandSourceStack source, CarpetRule<Boolean> currentRule, Boolean newValue, String userInput) {
			//#else
			//$$ public Boolean validate(CommandSource source, CarpetRule<Boolean> currentRule, Boolean newValue, String userInput) {
			//#endif

			//#if MC>=11300
			for(ServerWorld world : source.getServer().getWorlds()) {
			//#else
			//$$ for(ServerWorld world : source.getServer().worlds) {
			//#endif
				ChunkMapAccess access = (ChunkMapAccess) world.getChunkMap();
				for(PlayerEntity player : world.players) {
					if(newValue) {
						access.syncChunks((ServerPlayerEntity) player);
					}else {
						access.unloadNearestChunks((ServerPlayerEntity) player);
					}

				}
			}
			return newValue;
		}
	}

	@Rule(
		desc = "Creative players won't load any chunks, except the one they are in",
		categories = {CREATIVE, FEATURE},
		validators = CreativePlayersLoadChunksValidator.class
	)
	public static boolean creativePlayersLoadChunks = true;
	//#endif
}
