package org.jasperdev.mcommandframework;

import java.util.List;

public interface MCommand {
    String getName();
    String getDescription();
    String getPermission();
    List<SubcommandData> getSubcommands();
    void execute(SubcommandContext context);
}
