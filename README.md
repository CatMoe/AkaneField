# AkaneField  

简单的反机器人. 仅适用于BungeeCord

# 从源代码编译  

在开始前. 您需要准备 `maven` `git` 和 `JDK17`

1. 克隆此储存库 `git clone https://github.com/CatMoe/AkaneField`
2. 设置命令根目录至文件夹 `cd AkaneField`
3. 使用maven构建. 并且清理上一次构建的信息 `mvn clean install`
4. 完成后 从 `.\target\` 中取出来即可

# 安装

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
