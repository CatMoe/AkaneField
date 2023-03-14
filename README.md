# AkaneField  
  
> **[✔] AkaneField `3.0.0` 已发布!**  
> **此次更新包含了大量更改 您应该备份旧的配置文件 然后重新配置它.**  
> **Made by CatMoe@FallenCrystal with ❤**  
> **[!] `3.0.1` 之后的版本需要最新的[Protocolize2](https://github.com/Exceptionflug/protocolize) 和 [BungeePluginManagerPlus](https://github.com/Rothes/BungeePluginManagerPlus)**  
> **在此处下载 [Protocolize2](https://ci.exceptionflug.de/job/Protocolize2/) 以及其附属 [LegacyData](https://ci.exceptionflug.de/job/Protocolize-Legacy-Data/)  和[BPMP](https://www.spigotmc.org/resources/bungeepluginmanagerplus.98855)**  
> **[!] 请随时报告使用`IPTables & IPSet` 时遇到的问题 谢谢!**  
> **[?] `/af reload` 命令在`3.x`之后的版本中被删除. 请您重启服务器来生效配置文件.**  

**Performance · Customize · Compatible**  
**不支持除BungeeCord之外的所有平台!**  
> 花了一点时间添加了[bStats](https://bstats.org/plugin/bungeecord/AkaneField/17909/)支持w= 在提交[@88ca4ab](https://github.com/CatMoe/AkaneField/commit/88ca4abdaad3c0fa2cbb7f7b0adf323544b25119)或3.0.1之后的版本支持.  
> 请大家允许bStats! 我只是想看看有多少人在使用这个小破插件w; !  
![bStats](https://bstats.org/signatures/bungeecord/AkaneField.svg)  
> 我们正在考虑重做readme并添加wiki和多语言配置文件w!  
> 如果您有兴趣 请尝试创建一个分叉并拉取请求! w=  
> 您可以将想创建的readme或者wiki(统一使用markdown) 在sample-readme里 然后创建拉取请求w'  
  
我们~~永远不会添加其它代理的支持~~ `这取决于樱花星海考虑什么样的代理`.  
如果您正在使用Velocity 请使用[EpicGuard](https://github.com/4drian3d/EpicGuard)这样的东西作为替代品  
> 由于 樱花星海 服务器代理核心为BungeeCord 我没有必要添加其它支持 望周知  
> 我只是给其它人可以使用这个东西的权利 我没有必要接受全部功能请求  
> 话虽如此 我们欢迎您通过分叉来添加功能. 但是您必须遵守GPL3许可证.  
  
此项目本是为 [SakuraStarOcean](https://www.miaomoe.net/) 制作. 但是我想让其他人也避免自己的服务器遭到攻击而造成影响.  
如果您觉得此项目帮助到您 请考虑在 [爱发电](https://afdian.net/a/catmoe-studios) 上支持我们!  
> SakuraStarOcean 服务器IP: mc.miaomoe.net [NameMC](https://namemc.com/server/mc.miaomoe.net)
  
随时接受任何fork和推送请求 只要不故意破坏其性能或功能.  
**从源代码编译**  

在开始前. 您需要准备 `maven` `git` 和 `JDK17`

1. 克隆此储存库 `git clone https://github.com/CatMoe/AkaneField`
2. 设置命令根目录至文件夹 `cd AkaneField`
3. 使用maven构建. 并且清理上一次构建的信息 `mvn clean install`
4. 完成后 从 `.\target\` 中取出来即可  
Jar不带默认配置 因此您需要自己创建一个文件夹 用于存放配置  
**安装**  
  
**Releases**  
请注意: ~~为防止任何人伸手就来 之后可能会删除Release包~~`已删除` 请自行学会如何使用maven构建项目.  
正确的Releases包应该有一下两个文件:  

- `AkaneField`文件夹 包含`blacklist` `whitelist` `config` `messages`  
- 软件包本身 `AkaneField-version.jar`  

将两者都拖入到 `BungeeCord\plugins\` 里 然后启动服务器  
根据您的情况调整配置 启用Firewall等 `Linux Central IPTables & IPSet`  
启动服务器.  

**自行编译**  
从储存库里复制config文件夹.  
将构建成功的软件包和 `config` 文件夹拖入 `BungeeCord\plugins\` 里  
将 `config` 文件夹重命名为 `AkaneField`  
根据情况调整配置  
启动服务器  
  
**学分**  
[UltimateAntiBot-Recoded](https://github.com/Kr1S-D/UltimateAntibotRecoded) - ProxyCheck  
[FlameCord](https://github.com/2lstudios-mc/FlameCord) - Repeat Register & Login Check.  
