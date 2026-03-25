package org.jasperdev.mcommandframework.tree;

import org.jasperdev.mcommandframework.models.OptionData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class HelpGenerator {

	public static String[] generate(MCmdNode root) {
		List<String[]> entries = new ArrayList<>();
		walkTree(root, root.getName(), root.getDescription(), entries);
		entries.sort(Comparator.comparing(e -> e[0]));

		List<String> lines = new ArrayList<>();
		lines.add("§6--- Help ---");
		for (String[] entry : entries) {
			lines.add("§e/§r" + entry[0]);
			lines.add("§8  » §7" + entry[1]);
		}
		return lines.toArray(new String[0]);
	}

	private static void walkTree(MCmdNode node, String path, String lastDescription, List<String[]> output) {
		String description = node.getType() == null ? node.getDescription() : lastDescription;
		boolean hasOnlyOptionalChildren = !node.getChildren().isEmpty()
				&& node.getChildren().stream().allMatch(c -> c.getOptionData() != null && c.getOptionData().isOptional());
		if (node.getExecutor() != null && !hasOnlyOptionalChildren) {
			output.add(new String[]{path, description});
		}
		for (MCmdNode child : node.getChildren()) {
			walkTree(child, formatChildPath(child, path), description, output);
		}
	}

	private static String formatChildPath(MCmdNode child, String path) {
		if (child.getType() == null) {
			return path + " " + child.getName();
		}
		String argDisplay = child.getType() != OptionData.OptionType.CHOICE
				? child.getType().toString().toLowerCase()
				: child.getName();
		boolean optional = child.getOptionData() != null && child.getOptionData().isOptional();
		return optional
				? path + " §6[<§r" + argDisplay + "§6>]§r"
				: path + " §e<§r" + argDisplay + "§e>§r";
	}
}
