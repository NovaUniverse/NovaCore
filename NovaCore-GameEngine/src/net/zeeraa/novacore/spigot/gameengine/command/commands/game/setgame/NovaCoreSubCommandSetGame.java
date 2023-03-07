package net.zeeraa.novacore.spigot.gameengine.command.commands.game.setgame;

import net.zeeraa.novacore.spigot.command.AllowedSenders;
import net.zeeraa.novacore.spigot.command.NovaSubCommand;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.Game;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.GameManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

public class NovaCoreSubCommandSetGame extends NovaSubCommand  {

    public NovaCoreSubCommandSetGame() {
        super("setgame");

        this.setDescription("Sets the current game");
        this.setPermission("novacore.command.game.setgame");
        this.setPermissionDefaultValue(PermissionDefault.OP);
        this.setPermissionDescription("Access to the /game setgame command");

        this.addHelpSubCommand();

        this.setAllowedSenders(AllowedSenders.ALL);

        this.setFilterAutocomplete(true);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (GameManager.getInstance().hasRegisteredGame()) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Please provide a name. You can use tab to autocomplete avaliable trigger names");
            } else {
                Game game = GameManager.getInstance().getRegisteredGame(args[0]);
                if (game != null) {
                    if (!game.getName().equals(GameManager.getInstance().getName())) {
                        if (GameManager.getInstance().setActiveGame(game)) {
                            sender.sendMessage(ChatColor.GREEN + "Game \"" + game.getDisplayName() + "\" has been enabled.");
                        } else {
                            sender.sendMessage(ChatColor.RED + "Couldn't enable game.");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "This game is already active");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Could not find a game with that name");
                }
            }
        }
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> result = new ArrayList<>();
        if (GameManager.getInstance().hasRegisteredGame()) {
            for (Game game : GameManager.getInstance().getRegisteredGames()) {
                if (!game.getName().equals(GameManager.getInstance().getActiveGame().getName())) {
                    result.add(game.getName());
                }

            }
        }
        return result;
    }
}
