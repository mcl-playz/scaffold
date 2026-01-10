package org.jasperdev.mcommandframework.commands;

import org.bukkit.entity.Player;
import org.jasperdev.mcommandframework.api.MCommand;
import org.jasperdev.mcommandframework.models.OptionData;
import org.jasperdev.mcommandframework.tree.MCmdNode;

import java.util.List;

public class MCFCommand implements MCommand {
	@Override
	public MCmdNode setup(){
		// Root Node
		MCmdNode root = new MCmdNode("mcf", "MCommandFramework management command");

		// Subcommand: options
		MCmdNode optionsSub = new MCmdNode("options", "Used to test options");

		// Options Subcommand: playerarg
		MCmdNode playerArgSub = new MCmdNode("playerarg", "Used to test player argument functionality");
		MCmdNode playerArg = new MCmdNode(new OptionData("player", "Test player", OptionData.OptionType.PLAYER));

		playerArg.setExecutor(ctx -> {
			Player player = ctx.getArg("player", Player.class);
			ctx.sender().sendMessage("Grabbed information on player " + player.getUniqueId() + ". They are at " + player.getLocation());
		});

		playerArgSub.addChild(playerArg);
		optionsSub.addChild(playerArgSub);

		// Options Subcommand: choicearg
		MCmdNode choiceArgSub = new MCmdNode("choicearg", "Used to test choice argument functionality");
		MCmdNode choiceArg = new MCmdNode(new OptionData("choice", "Test choice", List.of("gay", "pan", "bi", "straight")));

		choiceArg.setExecutor(ctx -> {
			String choice = ctx.getArg("choice", String.class);
			ctx.sender().sendMessage("You chose " + choice);
		});

		choiceArgSub.addChild(choiceArg);
		optionsSub.addChild(choiceArgSub);

		// Options Subcommand: intarg
		MCmdNode intArgSub = new MCmdNode("intarg", "Used to test integer argument functionality");
		MCmdNode intArg = new MCmdNode(new OptionData("int", "Test integer", OptionData.OptionType.INTEGER));

		intArg.setExecutor(ctx -> {
			int integer = ctx.getArg("int", Integer.class);
			ctx.sender().sendMessage("You typed " + integer);
		});

		intArgSub.addChild(intArg);
		optionsSub.addChild(intArgSub);

		root.addChild(optionsSub);

		return root;
	}
}
