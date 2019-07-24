/*
 * Copyright 2018 Bradley Steele
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.darkboy.profile.infinityprofile.api.register.command;

import com.google.common.collect.Lists;
import me.darkboy.profile.infinityprofile.api.register.Registrable;
import me.darkboy.profile.infinityprofile.api.util.Players;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Bradley Steele
 */
public abstract class InfinityCommand implements Registrable, InfinityCommandExecutor, InfinityCommandTabCompleter {

    protected me.darkboy.profile.infinityprofile.api.InfinityPlugin plugin;

    private String name;
    private String description;
    private String usage;
    private String permission;
    private List<String> permissionDenyMessage;
    private boolean allowConsole;
    private List<String> aliases = Lists.newArrayList();
    private List<InfinityCommand> children = Lists.newArrayList();

    private boolean sync = true;
    private InfinityCommandExecutor executor = this;
    private InfinityCommandTabCompleter tabCompleter = this;
    private InfinityCommand parent;

    @Override
    public final void register() {
        // Update children which were set in the constructor, as
        // the plugin is injected after the constructor.
        children.forEach(child -> {
            child.plugin = plugin;
            child.parent = this;

            child.register();
            child.onRegister();
        });

        if (isRoot()) {
            if (InfinityCommandBukkit.getCommandMap().register(plugin.getName(), new InfinityCommandBukkit(this))) {
                plugin.getConsole().info("&bRegistered command: &a" + getName() + " &bwith &e" + children.size() + " &bchildren.");
            } else {
                plugin.getConsole().error("&cFailed to register command: &c" + getName() + "&r.");
            }
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        return null;
    }

    final void executeCalled(CommandSender sender, String[] args) {
        if (!children.isEmpty() && args.length > 0) {
            for (InfinityCommand child : children) {
                if (child.getAliases().stream().anyMatch(alias -> alias.equalsIgnoreCase(args[0]))) {
                    List<String> list = Lists.newArrayList(args);
                    list.remove(0);

                    child.executeCalled(sender, list.toArray(new String[0]));
                    return;
                }
            }
        }

        if (sender instanceof ConsoleCommandSender && !isAllowConsole()) {
            plugin.getConsole().error("&c" + getName() + " &rdoes not have console support.");
            return;
        }

        if (permission != null && !sender.hasPermission(permission)) {
            if (permissionDenyMessage != null && !permissionDenyMessage.isEmpty() && (sender instanceof Player)) {
                Players.sendMessage(Players.getPlayer(sender), permissionDenyMessage);
            }

            return;
        }

        // All tests past, execute.
        if (isSync()) {
            executor.execute(sender, args);
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> executor.execute(sender, args));
        }
    }

    final List<String> tabCalled(CommandSender sender, String alias, String[] args) {
        if (permission != null && !sender.hasPermission(permission)) {
            return null;
        }

        if (!children.isEmpty() && args.length > 0) {
            for (InfinityCommand child : children) {
                if (child.getAliases().stream().anyMatch(a -> a.equalsIgnoreCase(args[0]))) {
                    List<String> list = Lists.newArrayList(args);
                    list.remove(0);

                    return child.tabCalled(sender, alias, list.toArray(new String[0]));
                }
            }
        }

        // No children, invoke this class' tab completer
        return tabComplete(sender, alias, args);
    }

    /**
     * If the {@link InfinityCommand#name} is not present, the first alias will be returned, or
     * {@code null} if no aliases are set.
     *
     * @return command's name.
     */
    public String getName() {
        return name != null ? name : aliases.size() > 0 ? aliases.get(0) : null;
    }

    /**
     * The commands description describes what the command does, not how to use it.
     *
     * @return command's description.
     */
    public String getDescription() {
        return description != null ? description : "";
    }

    /**
     * @return command's usage.
     */
    public String getUsage() {
        return usage != null ? usage : "/" + name;
    }

    /**
     * @return command's permission or {@code null} if none.
     */
    public String getPermission() {
        return permission;
    }

    /**
     * @return message to send to the sender if they do not have permission to
     *         execute the command.
     */
    public List<String> getPermissionDenyMessage() {
        return permissionDenyMessage;
    }

    /**
     * @return if the console is allowed to execeute the command.
     */
    public boolean isAllowConsole() {
        return allowConsole;
    }

    /**
     * @return a list of aliases, including the command's "main" executor.
     */
    public List<String> getAliases() {
        return Collections.unmodifiableList(aliases);
    }

    /**
     * @return list of child commands, can be empty.
     */
    public List<InfinityCommand> getChildren() {
        return Collections.unmodifiableList(children);
    }

    /**
     * @return {@code true} if the command is ran on the main thread.
     */
    public boolean isSync() {
        return sync;
    }

    /**
     * @return the command's executor.
     */
    public InfinityCommandExecutor getExecutor() {
        return executor;
    }

    /**
     * @return the command's tab completer.
     */
    public InfinityCommandTabCompleter getTabCompleter() {
        return tabCompleter;
    }

    /**
     * @return the command's parent or {@code null} if the object is a
     *         root command.
     */
    public InfinityCommand getParent() {
        return parent;
    }

    /**
     * @return the command's root.
     */
    public InfinityCommand getRoot() {
        InfinityCommand command = this;

        while (!command.isRoot()) {
            command = command.getParent();
        }

        return command;
    }

    /**
     * @return {@code true} if the command has no parent.
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * @param parent the command's parent.
     */
    public void setParent(InfinityCommand parent) {
        this.parent = parent;
    }

    /**
     * @param name command's name, usually main or common executor.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The description should not contain how to use the command.
     *
     * @param description command's description, describing what the command does.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * The usage should not contain what the command does.
     *
     * @param usage command's usage, how to use it.
     */
    public void setUsage(String usage) {
        this.usage = usage;
    }

    /**
     * @param permission the permission required to execute the command, or null
     *                   if no permission is required.
     */
    public void setPermission(String permission) {
        this.permission = permission;
    }

    /**
     * @param permissionDenyMessage the messages to send to the sender if they do not have permission.
     */
    public void setPermissionDenyMessage(List<String> permissionDenyMessage) {
        this.permissionDenyMessage = permissionDenyMessage;
    }

    /**
     * @param allowConsole if the console is allowed to execute the command.
     */
    public void setAllowConsole(boolean allowConsole) {
        this.allowConsole = allowConsole;
    }

    /**
     * @param aliases new list of aliases.
     */
    public void setAliases(List<String> aliases) {
        this.aliases.clear();
        aliases.forEach(this::addAlias);
    }

    /**
     * @param aliases new list of aliases.
     */
    public void setAliases(String... aliases) {
        setAliases(Arrays.asList(aliases));
    }

    /**
     * Checks to see whether the provided alias is already present in the list,
     * if so it will not add it - this is to avoid duplicate and redundant aliases.
     *
     * @param alias the alias to add.
     */
    public void addAlias(String alias) {
        if (!aliases.contains(alias)) {
            aliases.add(alias);
        }
    }

    /**
     * @param children new list of children.
     */
    public void setChildren(List<InfinityCommand> children) {
        this.children.clear();
        children.forEach(this::addChild);
    }

    /**
     * @param children new list of children.
     */
    public void setChildren(InfinityCommand... children) {
        setChildren(Arrays.asList(children));
    }

    /**
     * @param child the child command to add.
     */
    public void addChild(InfinityCommand child) {
        if (!children.contains(child)) {
            child.plugin = plugin;
            child.parent = parent;
            children.add(child);
        }
    }

    /**
     * @param sync if the command is to be run on the main thread.
     */
    public void setSync(boolean sync) {
        this.sync = sync;
    }

    /**
     * @param executor the command's executor.
     */
    public void setExecutor(InfinityCommandExecutor executor) {
        this.executor = executor;
    }

    /**
     * @param tabCompleter the command's tab completer.
     */
    public void setTabCompleter(InfinityCommandTabCompleter tabCompleter) {
        this.tabCompleter = tabCompleter;
    }
}