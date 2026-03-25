package org.jasperdev.scaffold.tree;

import org.jasperdev.scaffold.models.ArgumentData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class HelpGenerator {

	public static String[] generate(CommandNode root){
		List<String[]> entries = new ArrayList<>();
		walkTree(root, root.getName(), root.getDescription(), entries);
		entries.sort(Comparator.comparing(e -> e[0]));

		List<String> lines = new ArrayList<>();
		lines.add("§6--- Help ---");
		for(String[] entry : entries){
			lines.add("§e/§r" + entry[0]);
			lines.add("§8  » §7" + entry[1]);
		}
		return lines.toArray(new String[0]);
	}

	private static void walkTree(CommandNode node, String path, String lastDescription, List<String[]> output){
		String description = node.getType() == null ? node.getDescription() : lastDescription;
		boolean hasOnlyOptionalChildren = !node.getChildren().isEmpty()
				&& node.getChildren().stream().allMatch(c -> c.getArgumentData() != null && c.getArgumentData().isOptional());
		if(node.getExecutor() != null && !hasOnlyOptionalChildren){
			output.add(new String[]{path, description});
		}
		for(CommandNode child : node.getChildren()){
			walkTree(child, formatChildPath(child, path), description, output);
		}
	}

	private static String formatChildPath(CommandNode child, String path){
		if(child.getType() == null){
			return path + " " + child.getName();
		}
		String argDisplay = child.getType() != ArgumentData.ArgumentType.CHOICE
				? child.getType().toString().toLowerCase()
				: child.getName();
		boolean optional = child.getArgumentData() != null && child.getArgumentData().isOptional();
		return optional
				? path + " §6[<§r" + argDisplay + "§6>]§r"
				: path + " §e<§r" + argDisplay + "§e>§r";
	}
}
