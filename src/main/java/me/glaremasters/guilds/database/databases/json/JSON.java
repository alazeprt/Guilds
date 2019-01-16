package me.glaremasters.guilds.database.databases.json;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import me.glaremasters.guilds.Guilds;
import me.glaremasters.guilds.database.Callback;
import me.glaremasters.guilds.database.DatabaseProvider;
import me.glaremasters.guilds.guild.Guild;
import me.glaremasters.guilds.guild.GuildHandler;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by GlareMasters
 * Date: 7/18/2018
 * Time: 11:38 AM
 */
public class JSON implements DatabaseProvider {

    private Gson gson;
    private File guildsFile;
    private Type guildsType;

    private GuildHandler guildHandler;
    private File dataFolder;

    public JSON(GuildHandler guildHandler, File dataFolder) {
        this.guildHandler = guildHandler;
        this.dataFolder = dataFolder;
    }


    /**
     * Initializing the JSON data storage system will allow the plugin to work and update as needed
     */
    @Override
    public void initialize() {

        File folder = new File(dataFolder, "data/");
        guildsFile = new File(folder, "guilds.json");
        guildsType = new TypeToken<Map<String, Guild>>() {
        }.getType();

        gson = new GsonBuilder().registerTypeAdapter(guildsType, new GuildMapDeserializer()).setPrettyPrinting().create();

        //noinspection ResultOfMethodCallIgnored
        folder.mkdirs();

        if (!guildsFile.exists()) {
            try {
                if (!guildsFile.createNewFile()) {
                    throw new IOException("Error");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    /**
     * This method creates a guild into the JSON file
     * @param guild the guild being created
     */
    @Override
    public void createGuild(Guild guild) {
        HashMap<String, Guild> guilds = getGuilds() == null ? new HashMap<>() : getGuilds();
        guilds.put(guild.getName(), guild);
        write(guildsFile, guilds, guildsType);
        Guilds.getGuilds().getGuildHandler().addGuild(guild);
    }

    /**
     * This method removes a guild from the JSON file
     * @param guild the guild being removed
     */
    @Override
    public void removeGuild(Guild guild) {
        Map<String, Guild> guilds = getGuilds();
        if (guilds == null || !guilds.keySet().contains(guild.getName())) return;
        guilds.remove(guild.getName());
        write(guildsFile, guilds, guildsType);
    }

    @Override
    public void addAlly(Guild guild, Guild targetGuild) {

    }

    @Override
    public void removeAlly(Guild guild, Guild targetGuild) {

    }

    /**
     * This method is called on the start of the server to load all the guilds into local memory so that it can handle as needed
     * @param callback all the guilds currently loaded on the server
     */
    @Override
    public void getGuilds(Callback<HashMap<String, Guild>, Exception> callback) {
        Guilds.newChain().asyncFirst(() -> {
            JsonReader reader;
            try {
                reader = new JsonReader(new FileReader(guildsFile));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                return null;
            }

            return gson.fromJson(reader, guildsType);
        }).syncLast(guilds -> callback.call((Map<String, Guild>) guilds, null)).execute();
    }

    /**
     * This method is super important. It updates data in different places for each guild as needed
     * @param guild the guild being updated
     */
    @Override
    public void updateGuild(Guild guild) {
        Map<String, Guild> guilds = getGuilds();
        guilds.put(guild.getName(), guild);
        write(guildsFile, guilds, guildsType);
    }

    @Override
    public void updateGuild() {
        Map<String, Guild> guilds = getGuilds();
        write(guildsFile, guilds, guildsType);
    }

    /**
     * This method will write new data
     * @param file the file being written to
     * @param toWrite the content being updated
     * @param typeOfSrc the GSON type that is being used
     */
    private void write(File file, Object toWrite, Type typeOfSrc) {
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(toWrite, typeOfSrc, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The Map of all guilds on the server
     * @return hashmap of all guilds
     */
    private Map<String, Guild> getGuilds() {
        return guildHandler.getGuilds();
    }

}
