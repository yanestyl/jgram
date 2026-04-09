# JGram
Lightweight annotation-based Java framework for Telegram bots.
> ⚠️ Work in progress. First release coming soon.
## Vision
```java
@BotController
public class MyBot {

  @OnCommand("/start")
  public String start() {
    return "Hello! 👋";
  }
}
