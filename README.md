# AkaneField  

> **[!] 警告: 有关支持 `IPTables & IPSet` 的技术尚未完全成熟. 还有很多的地方未做改善!**  
> **[!] 警告: 您可能在被多IP攻击时察觉到卡顿 如果攻击属于可以承受的范围 建议不使用防火墙!**  

**パフォーマンス · 次世代 · 最も攻撃解決する**  
**不支持除BungeeCord之外的所有平台!**  
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
