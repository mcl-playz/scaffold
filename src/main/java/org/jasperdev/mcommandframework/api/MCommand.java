package org.jasperdev.mcommandframework.api;

import org.jasperdev.mcommandframework.tree.MCmdNode;

public interface MCommand {
	/**
	 * Defines the structure and logic of the command.
	 *
	 * @return The root CmdNode for this command.
	 */
	MCmdNode setup();
}