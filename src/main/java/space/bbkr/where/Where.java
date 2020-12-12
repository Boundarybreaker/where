package space.bbkr.where;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;

public class Where implements ModInitializer {

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> dispatcher.register(
				CommandManager.literal("where")
						.then(CommandManager.argument("where", EntityArgumentType.player())
								.executes(context -> {
									PlayerEntity target = EntityArgumentType.getPlayer(context, "where");

									if (!context.getSource().hasPermissionLevel(4)) {
										PlayerEntity self = context.getSource().getPlayer();
										if (self.getScoreboardTeam() != null && target.getScoreboardTeam() != null) {
											if (!self.getScoreboardTeam().equals(target.getScoreboardTeam())) {
												context.getSource().sendError(new LiteralText(
														"Cannot get the location of a player on another team!"
														));
												return 0;
											}
										}
									}

									context.getSource().sendFeedback(new LiteralText(target.getName().asString())
											.append(" is at " + target.getBlockPos().getX() + ", "
													+ target.getBlockPos().getY() + ", " + target.getBlockPos().getZ()
                                                                                                        + " in " 
												        + target.getEntityWorld().getRegistryKey().getValue().toString()),
											true);
									return 1;
								})
						)
		));
	}
}
