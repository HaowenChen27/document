//启动

systemctl start docker 

//查看docker状态

systemctl status docker

// 查看版本

docker version

// 关闭docker

systemctl stop docker

// 卸载docker

yum -y remove docker-ce

rm -rf /var/lib/docker



//配置阿里云加速器

```
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://7ht31pj9.mirror.aliyuncs.com"]
}
EOF
sudo systemctl daemon-reload
sudo systemctl restart docker
```



//显示Docker系统信息，包括镜像和容器数

docker info

//帮助命令

docker help

#### **docker images :** 列出本地镜像

### 语法

```
docker images [OPTIONS] [REPOSITORY[:TAG]]
```

OPTIONS说明：

- **-a :**列出本地所有的镜像（含中间映像层，默认情况下，过滤掉中间映像层）；

  

- **--digests :**显示镜像的摘要信息；

  

- **-f :**显示满足条件的镜像；

  

- **--format :**指定返回值的模板文件；

  

- **--no-trunc :**显示完整的镜像信息；

  

- **-q :**只显示镜像ID。

#### **docker search :** 从Docker Hub查找镜像

### 语法

```
docker search [OPTIONS] TERM
```

OPTIONS说明：

- **--automated :**只列出 automated build类型的镜像；
- **--no-trunc :**显示完整的镜像描述；
- **-s :**列出收藏数不小于指定值的镜像。



#### **docker pull :** 从镜像仓库中拉取或者更新指定镜像

### 语法

```
docker pull [OPTIONS] NAME[:TAG|@DIGEST]
```

OPTIONS说明：

- **-a :**拉取所有 tagged 镜像

  

- **--disable-content-trust :**忽略镜像的校验,默认开启



#### **docker rmi :** 删除本地一个或多少镜像

### 语法

```
docker rmi [OPTIONS] IMAGE [IMAGE...]
```

OPTIONS说明：

- **-f :**强制删除；

  

- **--no-prune :**不移除该镜像的过程镜像，默认移除；

// 删除全部

docker rmi -f $(docker images -qa)



#### **docker run ：**创建一个新的容器并运行一个命令

### 语法

```
docker run [OPTIONS] IMAGE [COMMAND] [ARG...]
```

OPTIONS说明：

- **-a stdin:** 指定标准输入输出内容类型，可选 STDIN/STDOUT/STDERR 三项；
- **-d:** 后台运行容器，并返回容器ID；
- **-i:** 以交互模式运行容器，通常与 -t 同时使用；
- **-P:** 随机端口映射，容器内部端口**随机**映射到主机的端口
- **-p:** 指定端口映射，格式为：**主机(宿主)端口:容器端口**
- **-t:** 为容器重新分配一个伪输入终端，通常与 -i 同时使用；
- **--name="nginx-lb":** 为容器指定一个名称；
- **--dns 8.8.8.8:** 指定容器使用的DNS服务器，默认和宿主一致；
- **--dns-search example.com:** 指定容器DNS搜索域名，默认和宿主一致；
- **-h "mars":** 指定容器的hostname；
- **-e username="ritchie":** 设置环境变量；
- **--env-file=[]:** 从指定文件读入环境变量；
- **--cpuset="0-2" or --cpuset="0,1,2":** 绑定容器到指定CPU运行；
- **-m :**设置容器使用内存最大值；
- **--net="bridge":** 指定容器的网络连接类型，支持 bridge/host/none/container: 四种类型；
- **--link=[]:** 添加链接到另一个容器；
- **--expose=[]:** 开放一个端口或一组端口；
- **--volume , -v:** 绑定一个卷