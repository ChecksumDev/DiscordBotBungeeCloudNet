package de.staticred.discordbot;

import de.staticred.discordbot.bungeecommands.MCVerifyCommandExecutor;
import de.staticred.discordbot.bungeeevents.JoinEvent;
import de.staticred.discordbot.bungeeevents.LeaveEvent;
import de.staticred.discordbot.db.DataBaseConnection;
import de.staticred.discordbot.db.VerifyDAO;
import de.staticred.discordbot.discordevents.GuildJoinEvent;
import de.staticred.discordbot.discordevents.GuildLeftEvent;
import de.staticred.discordbot.discordevents.MessageEvent;
import de.staticred.discordbot.files.ConfigFileManager;
import de.staticred.discordbot.files.VerifyFileManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class Main extends Plugin {

    public static  Main INSTANCE;
    public static HashMap<ProxiedPlayer, Member> playerMemberHashMap = new HashMap<>();
    public static HashMap<ProxiedPlayer, TextChannel> playerChannelHashMap = new HashMap<>();
    public boolean useSQL;
    public boolean syncNickname;


    public  static JDA jda;

    @Override
    public void onEnable() {

        INSTANCE = this;


        ConfigFileManager.INSTANCE.loadFile();
        VerifyFileManager.INSTANCE.loadFile();

        if(ConfigFileManager.INSTANCE.useSQL()) {
            loadDataBase();
        }

        useSQL = ConfigFileManager.INSTANCE.useSQL();
        String token = ConfigFileManager.INSTANCE.getString("bot-token");
        loadBungeeEvents();


        syncNickname = ConfigFileManager.INSTANCE.nameSync();

        String activity = ConfigFileManager.INSTANCE.getString("discordBotActivityType");
        String type = ConfigFileManager.INSTANCE.getString("discordBotActivity");
        String link = ConfigFileManager.INSTANCE.getString("streamingLink");
        Activity activity1;

        if(activity.equalsIgnoreCase("listening")) {
            activity1 = Activity.listening(type);
        }else if(activity.equalsIgnoreCase("playing")) {
            activity1 = Activity.playing(type);
        }else if(activity.equalsIgnoreCase("streaming")) {
            activity1 = Activity.streaming(type,link);
        }else if(activity.equalsIgnoreCase("watching")) {
            activity1 = Activity.watching(type);
        }else {
            activity1 = Activity.playing(type);
        }



        String command = ConfigFileManager.INSTANCE.getString("verifycommand");

        loadBungeeCommands(command);

        try {
            initBot(token,activity1);
        } catch (LoginException e) {
            e.printStackTrace();
            System.out.println("[DiscordVerify] Bot can´t connect!");
            return;
        }

    }

    @Override
    public void onDisable() {
        DataBaseConnection.INSTANCE.closeConnection();
        ConfigFileManager.INSTANCE.saveFile();
        VerifyFileManager.INSTANCE.saveFile();
    }

    public void loadBungeeCommands(String command) {
        getProxy().getPluginManager().registerCommand(this,new MCVerifyCommandExecutor(command));
    }

    public void loadBungeeEvents() {
        getProxy().getPluginManager().registerListener(this,new JoinEvent());
        getProxy().getPluginManager().registerListener(this,new LeaveEvent());
    }

    public void loadDataBase() {
        DataBaseConnection con = DataBaseConnection.INSTANCE;
        con.connect();
        System.out.println("[DiscordVerify] Connect test success!");
        try {
            con.executeUpdate("CREATE TABLE IF NOT EXISTS verify(UUID VARCHAR(36) PRIMARY KEY, Name VARCHAR(16), Rank VARCHAR(20),Grouppower INT(5), Verified BOOLEAN, DiscordID VARCHAR(100))");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("[DiscordVerify] Connect test failed!");
        }
        con.closeConnection();
    }

    public String getStringFromConfig(String string, boolean prefix) {
        if(prefix)
            return ConfigFileManager.INSTANCE.getString("prefix").replaceAll("&","§") + ConfigFileManager.INSTANCE.getString(string).replaceAll("&","§");
        return ConfigFileManager.INSTANCE.getString(string).replaceAll("&","§");
    }

    public void initBot(String token, Activity activity) throws LoginException {
        jda = new JDABuilder(token).build();
        jda.getPresence().setPresence(activity,true);
        jda.addEventListener(new MessageEvent());
        jda.addEventListener(new GuildJoinEvent());
        jda.addEventListener(new GuildLeftEvent());
        System.out.println("[DiscordVerify] Bot Started!");
    }


    public static Main getInstance() {
        return INSTANCE;
    }


    public void updateRoles(Member m, int groupPower) {

        List<Integer> vipJP = ConfigFileManager.INSTANCE.getGroupPowersForGroup("vipJP");
        List<Integer> youtuberJP = ConfigFileManager.INSTANCE.getGroupPowersForGroup("youtuberJP");
        List<Integer> staffJP = ConfigFileManager.INSTANCE.getGroupPowersForGroup("staffJP");
        List<Integer> friendJP = ConfigFileManager.INSTANCE.getGroupPowersForGroup("friendJP");
        List<Integer> discordstaffJP = ConfigFileManager.INSTANCE.getGroupPowersForGroup("discord-staffJP");
        List<Integer> adminJP = ConfigFileManager.INSTANCE.getGroupPowersForGroup("adminJP");



        boolean ids = ConfigFileManager.INSTANCE.useTokens();


        try {
            if(VerifyDAO.INSTANCE.isDiscordIDInUse(m.getId())) {
                if(ids) {
                    m.getGuild().addRoleToMember(m,ConfigFileManager.INSTANCE.getRoleById("verifiedName")).queue();
                }
                m.getGuild().addRoleToMember(m,ConfigFileManager.INSTANCE.getRoleByName("verifiedName")).queue();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(int i : vipJP) {
            if(i == groupPower) {
                if(ids) {
                    m.getGuild().addRoleToMember(m,ConfigFileManager.INSTANCE.getRoleById("vipName")).queue();
                    break;
                }
                m.getGuild().addRoleToMember(m,ConfigFileManager.INSTANCE.getRoleByName("vipName")).queue();
                break;
            }
        }

        for(int i : youtuberJP) {
            if(groupPower == i) {
                if(ids) {
                    m.getGuild().addRoleToMember(m,ConfigFileManager.INSTANCE.getRoleById("youtuberName")).queue();
                    break;
                }
                m.getGuild().addRoleToMember(m,ConfigFileManager.INSTANCE.getRoleByName("youtuberName")).queue();
                break;
            }
        }

        for(int i : friendJP) {
            if(groupPower == i) {
                if(ids) {
                    m.getGuild().addRoleToMember(m,ConfigFileManager.INSTANCE.getRoleById("friendName")).queue();
                    break;
                }
                m.getGuild().addRoleToMember(m,ConfigFileManager.INSTANCE.getRoleByName("friendName")).queue();
                break;
            }
        }


        for(int i : staffJP) {
            if(groupPower == i) {
                if(ids) {
                    m.getGuild().addRoleToMember(m,ConfigFileManager.INSTANCE.getRoleById("staffName")).queue();
                    break;
                }
                m.getGuild().addRoleToMember(m,ConfigFileManager.INSTANCE.getRoleByName("staffName")).queue();
                break;
            }
        }

        for(int i : discordstaffJP) {
            if(groupPower == i) {
                if(ids) {
                    m.getGuild().addRoleToMember(m,ConfigFileManager.INSTANCE.getRoleById("discord-staffName")).queue();
                    break;
                }
                m.getGuild().addRoleToMember(m,ConfigFileManager.INSTANCE.getRoleByName("discord-staffName")).queue();
                break;
            }
        }

        for(int i : adminJP) {
            if(groupPower == i) {
                if(ids) {
                    m.getGuild().addRoleToMember(m,ConfigFileManager.INSTANCE.getRoleById("adminName")).queue();
                    break;
                }
                m.getGuild().addRoleToMember(m,ConfigFileManager.INSTANCE.getRoleByName("adminName")).queue();
                break;
            }
        }


    }

    public void removeAllRolesFromMember(Member m) {
        for(Role role : m.getRoles()) {
            m.getGuild().removeRoleFromMember(m,role).queue();
        }
    }

    public Member getMemberFromPlayer(ProxiedPlayer player) throws SQLException {
        User u;

        u = Main.jda.getUserById(Long.parseLong(VerifyDAO.INSTANCE.getDiscordID(player)));


        Member m = null;

        if (!Main.jda.getGuilds().isEmpty()) {
            for (Guild guild : Main.jda.getGuilds()) {
                if (u != null)
                    m = guild.getMember(u);
            }
        } else {
            throw new SQLException("There was an internal error! But may not found") ;
        }

        return m;
    }
}
