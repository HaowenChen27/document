```
### 连接服务器
sftp root@119.45.190.53
```

输入密码登录sftp，使用help或？查看命令

| 命令     | 使用操作                                                     | 描述                           |
| -------- | ------------------------------------------------------------ | ------------------------------ |
| help     | help                                                         | 查看帮助                       |
| ?        | ?                                                            | 查看帮助                       |
| bye      | bye                                                          | 退出SFTP                       |
| exit     | exit                                                         | 退出SFTP                       |
| quit     | quit                                                         | 退出SFTP                       |
| !        | !                                                            | 退出SFTP                       |
| version  | version                                                      | 查看SFTP版本                   |
| ls       | ls [-1aflnrSt] [path]                                        | 显示远程目录列表               |
| cd       | cd path                                                      | 进入远程目录path               |
| pwd      | pwd                                                          | 显示远程工作目录               |
| mkdir    | mkdir path                                                   | 创建远程目录                   |
| put      | put [-P] local-path [remote-path] [remote-path] 不写，则默认为当前远程目录 | 上传文件                       |
| get      | get [-P] remote-path [local-path] [local-path]不写，则默认为当前的本地目录,不支持目录下载 | 下载文件                       |
| rmdir    | rmdir path                                                   | 删除远程目录                   |
| rm       | rm path                                                      | 删除远程文件                   |
| lls      | lls [-1aflnrSt] [path]                                       | 显示本地目录列表               |
| lcd      | lcd path                                                     | 进入本地目录path               |
| lpwd     | lpwd                                                         | 显示本地工作目录               |
| lmkdir   | mkdir path                                                   | 创建本地目录                   |
| df       | df [-hi] [path]                                              | 显示当前目录的磁盘统计         |
| chgrp    | chgrp grp path                                               | 将文件path的组更改为grp        |
| chmod    | chmod mode path                                              | 将文件path的权限更改为mode     |
| chown    | chown own path                                               | 将文件path的所有者更改为own    |
| ln       | ln oldpath newpath                                           | 创建远程软连接                 |
| lumask   | lumask umask                                                 | 指定在建立文件时预设的权限掩码 |
| progress | process                                                      | 进度表切换显示                 |
| symlink  | symlink oldpath newpath                                      | 创建远程软连接                 |
| !command | !command                                                     | 本地窗口执行命令               |