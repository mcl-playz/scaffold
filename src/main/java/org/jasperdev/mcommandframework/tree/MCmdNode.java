package org.jasperdev.mcommandframework.tree;

import org.jasperdev.mcommandframework.models.MCommandContext;
import org.jasperdev.mcommandframework.models.OptionData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public final class MCmdNode {
	private final String name;
	private final String description;
	private final OptionData.OptionType type;
	private OptionData optionData;
	private MCmdExecutor executor;

	private final List<MCmdNode> children = new ArrayList<>();

	// Constructor for a Literal Subcommand
	public MCmdNode(String name, String description){
		this.name = name;
		this.description = description;
		this.type = null;
	}

	// Constructor for an argument
	public MCmdNode(@Nonnull OptionData data){
		this.name = data.getName();
		this.description = data.getDescription();
		this.type = data.getType();
		this.optionData = data;
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
	public OptionData.OptionType getType(){
		return type;
	}

	@Nullable
	public OptionData getOptionData(){
		return optionData;
	}

	public MCmdNode addChild(@Nonnull MCmdNode node){
		for(MCmdNode child : children){
			if(child.getType() != null){
				throw new IllegalStateException("Node already has an argument child. " +
						"Arguments cannot have siblings.");
			}

			if(node.getType() != null){
				throw new IllegalStateException("Cannot add an argument to a node " +
						"that already has subcommands.");
			}

			if(node.getType() == null && child.getName().equalsIgnoreCase(node.getName())){
				throw new IllegalArgumentException("Subcommand '" + node.getName() + "' already exists.");
			}
		}
		this.children.add(node);
		return this;
	}

	public @Nullable MCmdNode getChild(String name){
		return children.stream()
				.filter(child -> child.getName().equalsIgnoreCase(name))
				.findFirst()
				.orElse(null);
	}

	public List<MCmdNode> getChildren(){
		return children;
	}

	public MCmdNode setExecutor(@Nonnull MCmdExecutor executor){
		this.executor = executor;
		return this;
	}

	@Nullable
	public MCmdExecutor getExecutor(){
		return executor;
	}

	public boolean canExecute(){
		return this.executor != null;
	}

	public void run(@Nonnull MCommandContext context){
		if(executor != null){
			executor.execute(context);
		}
	}

	@FunctionalInterface
	public interface MCmdExecutor {
		void execute(MCommandContext context);
	}
}