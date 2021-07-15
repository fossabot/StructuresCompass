package com.github.samarium150.structurescompass.command;

import com.github.samarium150.structurescompass.util.StructureSearchUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import com.github.samarium150.structurescompass.util.GeneralUtils;

public final class StructureCompassCommand extends CommandTreeBase {

    public StructureCompassCommand() {
        this.addSubcommand(
            new CommandBase() {
                @Override
                public String getName() {
                    return "greet";
                }

                @Override
                public String getUsage(ICommandSender sender) {
                    return "";
                }

                @Override
                public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
                    if (sender instanceof EntityPlayer) {
                        sender.sendMessage(new TextComponentString("Hello " + sender.getName() +
                            " from StructureCompass!"));
                    } else {
                        sender.sendMessage(new TextComponentString("Hello from StructureCompass!"));
                    }
                }
            }
        );

        this.addSubcommand(
            new CommandBase() {
                @Override
                public String getName() {
                    return "search";
                }

                @Override
                public String getUsage(ICommandSender sender) {
                    return "";
                }

                @Override
                public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
                    EntityPlayerMP player = getCommandSenderAsPlayer(sender);
                    if (args.length < 1) {
                        throw new CommandException("Invalid syntax!!!");
                    }
                    StringBuilder structureNameBuilder = new StringBuilder();
                    for (int i = 0; i < args.length; ++i) {
                        structureNameBuilder.append(args[i]);
                        if (i < args.length - 1) {
                            structureNameBuilder.append(' ');
                        }
                    }
                    String structureName = structureNameBuilder.toString();
                    player.sendMessage(new TextComponentString("Starting searching for " + structureName));
                    BlockPos targetPos = StructureSearchUtils.search(
                        structureName,
                        player.getPosition(),
                        player.world,
                        true
                    );
                    String posMsg;
                    if (targetPos == null) {
                        posMsg = "Search failed. Try to move into certain dimension/biome for searching.";
                    } else {
                        posMsg = "Found " + structureName + "!\n" + targetPos.toString();
                    }
                    player.sendMessage(new TextComponentString(posMsg));
                }
            }
        );
    }

    @Override
    public String getName() {
        return "spass";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        // TODO: Internalization
        return "/spass greet";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0; // 0 代表任何人都能用
    }
}
