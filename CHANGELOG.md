# v2.8.2
```diff
~ Fix IllegalArgumentException on tab completion clean up
```

# v2.8.1
```diff
~ Quick fix
```

# v2.8.0
```diff
+ Add emoji tab completion (redownloading now refreshes emojis for players)
~ Fix ping completion not being removed with player quits
```

# v2.7.2
```diff
~ minor fixes on markdown trigger matching in both parsers
```

# v2.7.1
```diff
+ Emoji downloading from https://emzi0767.mzgit.io/discord-emoji/
+ Emoji subcommand
+ Emoji replacement, perms, config
~ Plugin is now built for java 17
```
```yml
# new config section!
emoji:
  enabled: true
  whitelist: [] # leave empty to turn off, use primary names (see /ds emoji show)
  colonOnly: false # set to true to stop replacement of text like "=D"
```

# v2.7.0
```diff
+ Paper chat event adaptation
```

# v2.6.1
```diff
+ New permission for pinging
+ "ping.enabled" config option
```

# v2.6.0
```diff
+ @-ping chat completion (1.20+)
+ @-ping chat 
+ Parsers now respect _ and * needing to touch space and text
~ Rewrote discord parser to always match the furthest away triggers
```


# v2.5.1
```diff
- Removed test completion listener
```

# v2.5
```diff
+ /ds test is now appended with a version of the test message with the triggers included
+ Parsers now respect existing colors
```

# v2.4
```diff
~ Renamed plugin subcommand "switch" to parser
+ The plugin subcommmand "parser" can now output the current parser if no argument is given
~ Modified the formatting of the output of the plugin subcommand "test"
```

# v2.3
```diff
- Removed debug message on tab completion
```