package org.jasperdev.scaffold.tree;

import org.jasperdev.scaffold.models.ArgumentData;
import org.jasperdev.scaffold.models.CommandContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public final class CommandNode {
	private final String name;
	private final String description;
	private final ArgumentData.ArgumentType type;
	private ArgumentData argumentData;
	private CommandExecutor executor;

	private final List<CommandNode> children = new ArrayList<>();

	// Constructor for a Literal Subcommand
	public CommandNode(String name, String description){
		this.name = name;
		this.description = description;
		this.type = null;
	}

	// Constructor for an argument
	public CommandNode(@Nonnull ArgumentData data){
		this.name = data.getName();
		this.description = data.getDescription();
		this.type = data.getType();
		this.argumentData = data;
	}

	@Nonnull
	public String getName(){
		return name;
	}

	@Nonnull
	public String getDescription(){
		return description;
	}

	@Nullable
	public ArgumentData.ArgumentType getType(){
		return type;
	}

	@Nullable
	public ArgumentData getArgumentData(){
		return argumentData;
	}

	public CommandNode addChild(@Nonnull CommandNode node){
		for(CommandNode child : children){
			if(child.getType() != null){
				throw new IllegalStateException("Node already has an argument child. " +
						"Arguments cannot have siblings.");
			}

			if(node.getType() != null){
				throw new IllegalStateException("Cannot add an argument to a node " +
						"that already has subcommands.");
			}

			if(child.getName().equalsIgnoreCase(node.getName())){
				throw new IllegalArgumentException("Subcommand '" + node.getName() + "' already exists.");
			}
		}
		this.children.add(node);
		return this;
	}

	public @Nullable CommandNode getChild(String name){
		return children.stream()
				.filter(child -> child.getName().equalsIgnoreCase(name))
				.findFirst()
				.orElse(null);
	}

	public List<CommandNode> getChildren(){
		return children;
	}

	public CommandNode setExecutor(@Nonnull CommandExecutor executor){
		this.executor = executor;
		return this;
	}

	@Nullable
	public CommandExecutor getExecutor(){
		return executor;
	}

	public boolean canExecute(){
		return this.executor != null;
	}

	public void run(@Nonnull CommandContext context){
		if(executor != null){
			executor.execute(context);
		}
	}

	@FunctionalInterface
	public interface CommandExecutor {
		void execute(CommandContext context);
	}
}