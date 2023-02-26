# AkaneField  

高性能 · 次世代 · 攻撃対策  
仅适用于BungeeCord 如果您正在使用Velocity这样的东西  
请使用EpicGuard或者其它反机器人 项目完全基于BungeeCord设计 请不要问何时添加Velocity或其它代理支持.  
如果您正在使用Velocity 请使用[EpicGuard](https://github.com/4drian3d/EpicGuard)这样的东西作为替代品  
  
此项目本是为 `SakuraStarOcean` 制作. 但是我想让其它人也避免机器人的攻击.  
如果您觉得此项目帮助到您 请在 [爱发电](https://afdian.net/a/catmoe-studios) 上支持我们!  
  
随时接受任何fork和推送请求 只要不故意破坏其性能或功能.  
**从源代码编译**  

在开始前. 您需要准备 `maven` `git` 和 `JDK17`

1. 克隆此储存库 `git clone https://github.com/CatMoe/AkaneField`
2. 设置命令根目录至文件夹 `cd AkaneField`
3. 使用maven构建. 并且清理上一次构建的信息 `mvn clean install`
4. 完成后 从 `.\target\` 中取出来即可  
**安装**

Jar不带默认配置 因此您需要自己创建一个文件夹 用于存放配置

**Releases**  
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
