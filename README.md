Hey guys. I always was worring if there is a possibility to create an discord bot plugin for minecraft. And yes it is. It is my first real project on GitHub. So im sorry if i did anything wrong here. Shortly the instructions. This Plugin is only for BungeeCord 1.8 with CloudNet 2 at the moment. Later more Version like a bungee only and a bukkit based will come. It already has file-support which is pretty useless if you use it for cloud-net. But it is for the future version. 

Instuctions:

1. The Config
  The most thing should be self explaining. Like the UseSQL setting. But there are 1-2 thing i want to explain. The bot-token is where you   ´re bot token has to sit. Important: WITHOUT "" or '' ! If you don´t know what a bot-token is or how to get it look up this site:          https://discordapp.com/developers/applications/
  The next is the activity type, you can use, playing, listening, watching and streaming, but for last you will need a link. If you type     something wrong it will choose playing. Next you can set the text what he is doing. The verifycommand is only for ingame ussage. So just   like /verify or you can use what ever you want for ingame. On discord you are limited to the 4 commands. Later more. Now the important     the important. You can choose on 5 ranks, and give all an JoinPower. It musst be the same as the ingame ranks. So like if you have a VIP
  rank on your Minecraft-Server, with an joinpower of 500, it should be the same on the VipJP in the config. So just the bot can know
  which grouppower should get what rank. The useids options is most of the time false. But if you know the ids of your Discord ranks you 
  can use them aswell. If you don´t know what Rank-IDs are. You dont need to care. The next options are the names of the ranks on the
  discord server. Like if you´re verified rank is called member, type it in there. If you get something wrong, it will get you an error. 
  The next options are just messages. Up to the 'NoInquiries' option they are all DiscordMessages. The rest are ingame messages.
  
2. Commands: 

Discord:
!help -> shows all commands
!verify [name] -> verify youreself ingame, [name] = ingame name
!update -> update youre rank
!unlink -> unlink youreself.

Minecraft:
Your command: accept/decline/update/unlink
accept: accept the current inquiry.
decline: decline the current inquiry.
update: update your DiscordRank
unlink: unlink youreself.

3. How does it work

Type in the discord !verify [yourIngameName]
If you are on the Minecraft network. You will get an message. Just hit accept or decline. And you will be verified.

4. How to install
Just put it in your Proxy Plugins folder.
