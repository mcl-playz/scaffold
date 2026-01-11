package org.jasperdev.mcommandframework;

import org.jasperdev.mcommandframework.models.OptionData;
import org.jasperdev.mcommandframework.tree.MCmdNode;

public class Utils {
	public static MCmdNode node(String name, String desc) {
		return new MCmdNode(name, desc);
	}

	public static MCmdNode arg(String name, String desc, OptionData.OptionType type) {
		return new MCmdNode(new OptionData(name, desc, type));
	}
}
