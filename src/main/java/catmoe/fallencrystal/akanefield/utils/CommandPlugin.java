package catmoe.fallencrystal.akanefield.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public final class CommandPlugin extends Plugin {
    private final CommandManager commandManager;
    private Configuration config;

    public CommandPlugin() {
        this.commandManager = new CommandManager(this);
    }

    @Override
    public void onEnable() {
        loadConfig();

        CommandManager commandManager = getCommandManager();
        commandManager.loadProxyJoinCommands();

        registerChannels();
    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public Configuration getConfig() {
        if (this.config == null) {
            loadConfig();
        }

        return this.config;
    }

    public void saveConfig() {
        try {
            File pluginFolder = getDataFolder();
            if (!pluginFolder.exists()) {
                boolean makeFolder = pluginFolder.mkdirs();
                if (!makeFolder) {
                    throw new IOException("Failed to create plugin folder for JoinCommands.");
                }
            }

            ConfigurationProvider provider = ConfigurationProvider.getProvider(YamlConfiguration.class);
            File file = new File(pluginFolder, "config.yml");
            provider.save(this.config, file);
        } catch (IOException ex) {
            Logger logger = getLogger();
            logger.log(Level.WARNING, "An error occurred while saving the 'config.yml' file:", ex);
        }
    }

    private void registerChannels() {
        ProxyServer proxy = getProxy();
        proxy.registerChannel("jc:player");
        proxy.registerChannel("jc:console");
    }

    private void loadConfig() {
        try {
            File pluginFolder = getDataFolder();
            if (!pluginFolder.exists()) {
                boolean makeFolder = pluginFolder.mkdirs();
                if (!makeFolder) {
                    throw new IOException("Failed to create plugin folder.");
                }
            }

            File file = new File(pluginFolder, "config.yml");
            if (!file.exists()) {
                saveDefaultConfig();
            }

            ConfigurationProvider provider = ConfigurationProvider.getProvider(YamlConfiguration.class);
            this.config = provider.load(file);
        } catch (IOException ex) {
            Logger logger = getLogger();
            logger.log(Level.WARNING, "An error occurred while loading the 'config.yml' file:", ex);
            this.config = null;
        }
    }

    private void saveDefaultConfig() {
        try {
            File pluginFolder = getDataFolder();
            if (!pluginFolder.exists()) {
                boolean makeFolder = pluginFolder.mkdirs();
                if (!makeFolder) {
                    throw new IOException("Failed to create plugin folder.");
                }
            }

            File file = new File(pluginFolder, "config.yml");
            if (!file.exists()) {
                boolean createFile = file.createNewFile();
                if (!createFile) {
                    throw new IOException("Create file returned false.");
                }
            }

            Path path = file.toPath();
            InputStream configStream = getResourceAsStream("config-bungee.yml");
            Files.copy(configStream, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger logger = getLogger();
            logger.log(Level.WARNING, "An error occurred while creating the default 'config.yml' file:", ex);
        }
    }
}