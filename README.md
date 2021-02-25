# Gamer Bot

### Default Prefix: >>

### Complete Misc.
- Switched to Log4j
- Image returner based on given parameter (Works for any subreddit)
- Added MariaDB integration.
  - Get and store Guild ID's and Text Channel ID's (more to come)

### Complete Commands:
- Beans Listener/Command
  - ```>>beans```
- Dice Rolling Command
  - ```>>roll [dice] [rolls]```
- Ping Command
  - ```>>ping```
- Purge Command
  - ```>>purge [number]```
- Remind Me Command
  - ```>>remindme [task] => [time].[AM/PM].[zone]```
- Role Command
  - ```>>role [add/remove/help] [role] [user]```
- Timer Command
  - ```>>timer [task] => [value].[hours/minutes/seconds]```
- Shutdown Command
  - ```>>shutdown```
- Meme Command
  - ```>>meme```
- Magic 8 Ball Command
  - ```>>magic8ball [question]```
- Stocks/Stonks Command
  - ```>>stocks/>>stonks [symbol] <symbol> <symbol> <symbol> <symbol>```

### Complete Audio
- Join
  - ```>>join```
- Leave
  - ```>>leave```
- Pause
  - ```>>pause```
- Play
  - ```>>play / >>play [YouTube URL]/[YouTube Search]```
- Skip
  - ```>>skip```

### Complete Events:
- Guild Member Join Event
- Bot join Guild Event
  - Adds Text Channels to SQL
- Text Channel Create/Delete

### Plans/Todo:
- ~~Customized prefixes per server~~ (Discord update coming soon for / (slash) commands)
- ~~Either MariaDB/MongoDB integration for per server customizations~~
- Auto Role functionality - Users add their own specific roles and/or react roles
- Always more Documentation/Javadoc
- Get weather by zip code command

### Spring Boot
I am working on an additional extension of this project [Gamer Bot Spring](https://github.com/compact-disc/GamerBot-Spring) that is a Spring Boot integration of this project. This will still be maintained standalone but implemented in the Spring Boot version.
