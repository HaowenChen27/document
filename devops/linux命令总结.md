# 1. find 命令

```
find [PATH] [option] [action]

与时间有关的参数：

-mtime n : n为数字，意思为在n天之前的“一天内”被更改过的文件；
-mtime +n : 列出在n天之前（不含n天本身）被更改过的文件名；
-mtime -n : 列出在n天之内（含n天本身）被更改过的文件名；
-newer file : 列出比file还要新的文件名

例如：

find /root -mtime 0 # 在当前目录下查找今天之内有改动的文件

与用户或用户组名有关的参数：

-user name : 列出文件所有者为name的文件
-group name : 列出文件所属用户组为name的文件
-uid n : 列出文件所有者为用户ID为n的文件
-gid n : 列出文件所属用户组为用户组ID为n的文件

例如：

find /home/hadoop -user hadoop # 在目录/home/hadoop中找出所有者为hadoop的文件

与文件权限及名称有关的参数：

-name filename ：找出文件名为filename的文件
-size [+-]SIZE ：找出比SIZE还要大（+）或小（-）的文件
-tpye TYPE ：查找文件的类型为TYPE的文件，TYPE的值主要有：一般文件（f)、设备文件（b、c）、
             目录（d）、连接文件（l）、socket（s）、FIFO管道文件（p）；
-perm mode ：查找文件权限刚好等于mode的文件，mode用数字表示，如0755；
-perm -mode ：查找文件权限必须要全部包括mode权限的文件，mode用数字表示
-perm +mode ：查找文件权限包含任一mode的权限的文件，mode用数字表示

例如：

find / -name passwd # 查找文件名为passwd的文件
find . -perm 0755 # 查找当前目录中文件权限的0755的文件
find . -size +12k # 查找当前目录中大于12KB的文件，注意c表示byte
```





# 2. ls 命令

```
-a ：全部的档案，连同隐藏档( 开头为 . 的档案) 一起列出来～ 
-A ：全部的档案，连同隐藏档，但不包括 . 与 .. 这两个目录，一起列出来～ 
-d ：仅列出目录本身，而不是列出目录内的档案数据 
-f ：直接列出结果，而不进行排序 (ls 预设会以档名排序！) 
-F ：根据档案、目录等信息，给予附加数据结构，例如： 
*：代表可执行档； /：代表目录； =：代表 socket 档案； |：代表 FIFO 档案； 
-h ：将档案容量以人类较易读的方式(例如 GB, KB 等等)列出来； 
-i ：列出 inode 位置，而非列出档案属性； 
-l ：长数据串行出，包含档案的属性等等数据； 
-n ：列出 UID 与 GID 而非使用者与群组的名称 (UID与GID会在账号管理提到！) 
-r ：将排序结果反向输出，例如：原本档名由小到大，反向则为由大到小； 
-R ：连同子目录内容一起列出来； 
-S ：以档案容量大小排序！ 
-t ：依时间排序 
--color=never ：不要依据档案特性给予颜色显示； 
--color=always ：显示颜色 
--color=auto ：让系统自行依据设定来判断是否给予颜色 
--full-time ：以完整时间模式 (包含年、月、日、时、分) 输出 
--time={atime,ctime} ：输出 access 时间或 改变权限属性时间 (ctime) 
而非内容变更时间 (modification time)     例如：ls [-aAdfFhilRS] 目录名称 ls [--color={none,auto,always}] 目录名称 ls [--full-time] 目录名称
```





# 3.cd 命令

```
cd /root/Docements # 切换到目录/root/Docements
cd ./path          # 切换到当前目录下的path目录中，“.”表示当前目录  
cd ../path         # 切换到上层目录中的path目录中，“..”表示上一层目录
```



# 4. tree命令，显示树形的层级目录结构，非原生命令，需要安装tree



# 5.cp 命令，作用复制

```
-a ：将文件的特性一起复制
-p ：连同文件的属性一起复制，而非使用默认方式，与-a相似，常用于备份
-i ：若目标文件已经存在时，在覆盖时会先询问操作的进行
-r ：递归持续复制，用于目录的复制行为
-u ：目标文件与源文件有差异时才会复制
```



# 6.rm命令作用为删除

```
-f ：就是force的意思，忽略不存在的文件，不会出现警告消息
-i ：互动模式，在删除前会询问用户是否操作
-r ：递归删除，最常用于目录删除，它是一个非常危险的参数
```



# 7. mv命令作用为移动文件

```
-f ：force强制的意思，如果目标文件已经存在，不会询问而直接覆盖
-i ：若目标文件已经存在，就会询问是否覆盖
-u ：若目标文件已经存在，且比目标文件新，才会更新
```



# 8. pwd命令，作用为查看”当前工作目录“的完整路径

```
pwd -P # 显示出实际路径，而非使用连接（link）路径；pwd显示的是连接路径
```



# 9. tar命令，用于压缩解压

```
-c ：新建打包文件
-t ：查看打包文件的内容含有哪些文件名
-x ：解打包或解压缩的功能，可以搭配-C（大写）指定解压的目录，注意-c,-t,-x不能同时出现在同一条命令中
-j ：通过bzip2的支持进行压缩/解压缩
-z ：通过gzip的支持进行压缩/解压缩
-v ：在压缩/解压缩过程中，将正在处理的文件名显示出来
-f filename ：filename为要处理的文件
-C dir ：指定压缩/解压缩的目录dir

### 缩略版
压缩：tar -jcv -f filename.tar.bz2 要被处理的文件或目录名称
查询：tar -jtv -f filename.tar.bz2
解压：tar -jxv -f filename.tar.bz2 -C 欲解压缩的目录
```





# 10. mkdir命令创建目录



```
mkdir [选项]... 目录... 
 -m, --mode=模式，设定权限<模式> (类似 chmod)，而不是 rwxrwxrwx 减 umask
 -p, --parents  可以是一个路径名称。此时若路径中的某些目录尚不存在,加上此选项后,系统将自动建立好那些尚不存在的目录,即一次可以建立多个目录; 
 -v, --verbose  每次创建新目录都显示信息
```



# 11. rmdir 命令删除目录



```
rmdir [选项]... 目录...
-p 递归删除目录dirname，当子目录删除后其父目录为空时，也一同被删除。如果整个路径被删除或者由于某种原因保留部分路径，则系统在标准输出上显示相应的信息。 
-v --verbose  显示指令执行过程 
```



# 12. gzip 命令压缩文件或文件夹为 .gz文件



```
 gzip[参数][文件或者目录]
-a or --ascii 　使用ASCII文字模式。 
-c or --stdout or --to-stdout 　把压缩后的文件输出到标准输出设备，不去更动原始文件。 
-d or --decompress or ----uncompress 　解开压缩文件。 
-f or --force 　强行压缩文件。不理会文件名称 or 硬连接是否存在以及该文件是否为符号连接。 
-h or --help 　在线帮助。 
-l or --list 　列出压缩文件的相关信息。 
-L or --license 　显示版本与版权信息。 
-n or --no-name 　压缩文件时，不保存原来的文件名称及时间戳记。 
-N or --name 　压缩文件时，保存原来的文件名称及时间戳记。 
-q or --quiet 　不显示警告信息。 
-r or --recursive 　递归处理，将指定目录下的所有文件及子目录一并处理。 
-S<压缩字尾字符串> or ----suffix<压缩字尾字符串> 　更改压缩字尾字符串。 
-t or --test 　测试压缩文件是否正确无误。 
-v or --verbose 　显示指令执行过程。 
-V or --version 　显示版本信息。 
-num 用指定的数字num调整压缩的速度，-1 or --fast表示最快压缩方法（低压缩比），-9 or --best表示最慢压缩方法（高压缩比）。系统缺省值为6
```



# 13.  ps 命令显示运行的进程，还会显示进程的一些信息如pid, cpu和内存使用情况等



```
-A ：所有的进程均显示出来
-a ：不与terminal有关的所有进程
-u ：有效用户的相关进程
-x ：一般与a参数一起使用，可列出较完整的信息
-l ：较长，较详细地将PID的信息列出
```



# 14. kill 命令用于终止进程



```
kill -signal PID

1：SIGHUP，启动被终止的进程
2：SIGINT，相当于输入ctrl+c，中断一个程序的进行
9：SIGKILL，强制中断一个进程的进行
15：SIGTERM，以正常的结束进程方式来终止进程
17：SIGSTOP，相当于输入ctrl+z，暂停一个进程的进行
```



# 15. killall 命令和参数



```
killall [-iIe] [command name]

-i ：交互式的意思，若需要删除时，会询问用户
-e ：表示后面接的command name要一致，但command name不能超过15个字符
-I ：命令名称忽略大小写
# 例如：
killall -SIGHUP syslogd # 重新启动syslogd
```



# 16. crontab命令是启动linux定时任务的服务



```
service cron start # 启动cronjob
service cron stop # 停止cronjob
service cron restart #  重启cronjob
crontab -e # 编辑cronjob任务
```



# 17.  free 命令用于显示Linux系统中空闲的、已用的物理内存及swap内存,及被内核使用的buffer

```
free [参数]
-b 　以Byte为单位显示内存使用情况。 
-k 　以KB为单位显示内存使用情况。 
-m 　以MB为单位显示内存使用情况。
-g   以GB为单位显示内存使用情况。 
-o 　不显示缓冲区调节列。 
-s<间隔秒数> 　持续观察内存使用状况。 
-t 　显示内存总和列。 
-V 　显示版本信息
```



# 18. top 命令是Linux下常用的性能分析工具，能够实时显示系统中各个进程的资源占用状况，类似于Windows的任务管理器

```
top [参数]
-b 批处理
-c 显示完整的治命令
-I 忽略失效过程
-s 保密模式
-S 累积模式
-i<时间> 设置间隔时间
-u<用户名> 指定用户名
-p<进程号> 指定进程
-n<次数> 循环显示的次数
```



# 19. chmod命令

```
chmod [-R] xyz 文件或目录
-R：进行递归的持续更改，即连同子目录下的所有文件都会更改

#同时，chmod还可以使用u（user）、g（group）、o（other）、a（all）和+（加入）、-（删除）、=（设置）跟rwx搭配来对文件的权限进行更改，编号是各种权限的数字代码，示例：
chmod 0755 file # 把file的文件权限改变为-rxwr-xr-x
chmod g+w file # 向file的文件权限中加入用户组可写权限
```



# 20. chown命令改变文件所有者



```
chown [para]... [owner][:[group]] file...

 -c 显示更改的部分的信息

　-f 忽略错误信息

　-h 修复符号链接

　-R 处理指定目录以及其子目录下的所有文件

　-v 显示详细的处理信息

　-deference 作用于符号链接的指向，而不是链接文件本身
```



# 22. useradd 命令建立用户账号

```
useradd [-mMnr][-c <备注>][-d <登入目录>][-e <有效期限>][-f <缓冲天数>][-g <群组>][-G <群组>][-s ][-u ][用户帐号] 

    -c #<备注> 　加上备注文字。备注文字会保存在passwd的备注栏位中。　
　　-d #<登入目录> 　指定用户登入时的启始目录。
　　-D #　变更预设值．
　　-e # <有效期限> 　指定帐号的有效期限。
　　-f # <缓冲天数> 　指定在密码过期后多少天即关闭该帐号。
　　-g # <群组> 　指定用户所属的群组。
　　-G # <群组> 　指定用户所属的附加群组。
　　-m # 自动建立用户的登入目录。
　　-M # 不要自动建立用户的登入目录。
　　-n # 取消建立以用户名称为名的群组．
　　-r # 建立系统帐号。
　　-s # 指定用户登入后所使用的shell。
　　-u # 指定用户ID。
```



# 23. usermod 修改用户信息



```
usermod -h
usermod [options] LOGIN

-c #后面接账号的说明，即/etc/passwd第五栏的说明栏，可以加入一些账号的说明
 -d #后面接账号的家目录，即修改/etc/passwd的第六栏
 -e #后面接日期，格式是YYYY-MM-DD也就是在/etc/shadow内的第八栏
 -f #后面接天数，修改shadow的第七栏
 -g #后面接主群组，修改/etc/passwd的第四个字段，即是GID的字段
 -G #后面接附加群组，修改这个使用者能够支持的群组，修改的是/etc/group
 -a #与 -G 合用，可增加附加群组的支持而非设定
 -l #后面接账号名称。修改账号名称，/etc/passwd的第一栏
 -s #后面接Shell的文件，例如/bin/bash或/bin/csh等等
 -u #后面接 UID 数字，修改用户的UID /etc/passwd第三栏
 -L #暂时将用户的密码冻结，让他无法登入。其实就是在/etc/shadow的密码栏前面加上了“!”
 -U #将/etc/shadow 密码栏的“!”去掉
```

 

# 24. userdel 删除用户



```
userdel [options] LOGIN
-f # 强制删除，包括用户的一切相关内容，这个参数是危险的参数，不建议大家使用。详细说明看MAN
-r # 删除用户的家目录和用户的邮件池
```



# 25. groupadd　命令用于将新组加入系统



```
groupadd [－g gid] [－o]] [－r] [－f] groupname

－g gid：指定组ID号。
－o：允许组ID号，不必惟一。
－r：加入组ID号，低于499系统账号。
－f：加入已经有的组时，发展程序退出。
```



# 26. groupdel　命令删除组

```
＃　用于删除不再需要的组，如果指定的组中包含用户，则必须先删除组里面的用户>以后，才能删除组
groupdel [options] GROUP
```



# 27. sudo 用来以其他身份来执行命令，预设的身份为root



```
sudo(选项)(参数)

-b：在后台执行指令；
-h：显示帮助；
-H：将HOME环境变量设为新身份的HOME环境变量；
-k：结束密码的有效期限，也就是下次再执行sudo时便需要输入密码；。
-l：列出目前用户可执行与无法执行的指令；
-p：改变询问密码的提示符号；
-s：执行指定的shell；
-u<用户>：以指定的用户作为新的身份。若不加上此参数，则预设以root作为新的身份；
-v：延长密码有效期限5分钟；
-V ：显示版本信息。
```



# 28. passwd 设置用户的密码



```
passwd [OPTION...] <accountName>
-k, --keep-tokens       keep non-expired authentication tokens
-d, --delete            delete the password for the named account (root only)
-l, --lock              lock the named account (root only)
-u, --unlock            unlock the named account (root only)
-f, --force             force operation
-x, --maximum=DAYS      maximum password lifetime (root only)
-n, --minimum=DAYS      minimum password lifetime (root only)
-w, --warning=DAYS      number of days warning users receives before password expiration (root only)
-i, --inactive=DAYS     number of days after password expiration when an account becomes disabled (root only)
-S, --status            report password status on the named account (root)
```



# 29. groups 显示用户所属组



```
groups # 默认显示当前用户的组

groups hadoop2 # 显示hadoop2用户的组
```



# 30. vi/vim 是使用vi编辑器的命令



# 31. cat　用途是连接文件或标准输入并打印。这个命令常用来显示文件内容，或者将几个文件连接起来显示，或者从标准输入读取内容并显示，它常与重定向符号配合使用。



```
 cat [选项] [文件]...
-A, --show-all           等价于 -vET
-b, --number-nonblank    对非空输出行编号
-e                       等价于 -vE
-E, --show-ends          在每行结束处显示 $
-n, --number     对输出的所有行编号,由1开始对所有输出的行数编号
-s, --squeeze-blank  有连续两行以上的空白行，就代换为一行的空白行 
-t                       与 -vT 等价
-T, --show-tabs          将跳格字符显示为 ^I
-u                       (被忽略)
-v, --show-nonprinting   使用 ^ 和 M- 引用，除了 LFD 和 TAB 之外
```



# 32. more 命令和cat的功能一样都是查看文件里的内容，但有所不同的是more可以按页来查看文件的内容，还支持直接跳转行等功能



```
more [-dlfpcsu ] [-num ] [+/ pattern] [+ linenum] [file ... ] 
命令参数：
+n 从笫n行开始显示
-n 定义屏幕大小为n行
+/pattern 在每个档案显示前搜寻该字串（pattern），然后从该字串前两行之后开始显示 
-c 从顶部清屏，然后显示
-d 提示“Press space to continue，’q’ to quit（按空格键继续，按q键退出）”，禁用响铃功能
-l 忽略Ctrl+l（换页）字符
-p 通过清除窗口而不是滚屏来对文件进行换页，与-c选项相似
-s 把连续的多个空行显示为一行
-u 把文件内容中的下画线去掉
常用操作命令：
Enter 向下n行，需要定义。默认为1行
Ctrl+F 向下滚动一屏
空格键 向下滚动一屏
Ctrl+B 返回上一屏
= 输出当前行的行号
：f 输出文件名和当前行的行号
V 调用vi编辑器
!命令 调用Shell，并执行命令 
q 退出more
```



# 33. less　命令用法比起 more 更加的有弹性。在 more 的时候，我们并没有办法向前面翻， 只能往后面看，但若使用了 less 时，就可以使用 [pageup] [pagedown] 等按键的功能来往前往后翻看文件，更容易用来查看一个文件的内容！除此之外，在 less 里头可以拥有更多的搜索功能，不止可以向下搜，也可以向上搜。



```
 less [参数]  文件 
-b <缓冲区大小> 设置缓冲区的大小
-e 当文件显示结束后，自动离开
-f 强迫打开特殊文件，例如外围设备代号、目录和二进制文件
-g 只标志最后搜索的关键词
-i 忽略搜索时的大小写
-m 显示类似more命令的百分比
-N 显示每行的行号
-o <文件名> 将less 输出的内容在指定文件中保存起来
-Q 不使用警告音
-s 显示连续空行为一行
-S 行过长时间将超出部分舍弃
-x <数字> 将“tab”键显示为规定的数字空格
/字符串：向下搜索“字符串”的功能
?字符串：向上搜索“字符串”的功能
n：重复前一个搜索（与 / 或 ? 有关）
N：反向重复前一个搜索（与 / 或 ? 有关）
b 向后翻一页
d 向后翻半页
h 显示帮助界面
Q 退出less 命令
u 向前滚动半页
y 向前滚动一行
空格键 滚动一行
回车键 滚动一页
[pagedown]： 向下翻动一页
[pageup]： 向上翻动一页
```





# 34. tail 命令

```
#从指定点开始将文件写到标准输出.使用tail命令的-f选项可以方便的查阅正在改变的日志文件,tail -f filename会把filename里最尾部的内容显示在屏幕上,并且不但刷新,使你看到最新的文件内容. 
tail[必要参数][选择参数][文件]   

-f 循环读取
-q 不显示处理信息
-v 显示详细的处理信息
-c<数目> 显示的字节数
-n<行数> 显示行数
--pid=PID 与-f合用,表示在进程ID,PID死掉之后结束. 
-q, --quiet, --silent 从不输出给出文件名的首部 
-s, --sleep-interval=S 与-f合用,表示在每次反复的间隔休眠S秒
```



# 35. head 命令用于显示档案的开头至标准输出中，默认head命令打印其相应文件的开头10行

```
head [参数]... [文件]...  
-q 隐藏文件名
-v 显示文件名
-c<字节> 显示字节数
-n<行数> 显示的行数
```



# 36. diff 命令用于比较两个文件或目录的不同



```
 diff[参数][文件1或目录1][文件2或目录2]

# diff命令能比较单个文件或者目录内容。如果指定比较的是文件，则只有当输入为文本文件时才有效。以逐行的方式，比较文本文件的异同处。如果指定比较的是目录的的时候，diff 命令会比较两个目录下名字相同的
# 文本文件。列出不同的二进制文件、公共子目录和只在一个目录出现的文件。

-a or --text 　#diff预设只会逐行比较文本文件。
-b or --ignore-space-change 　#不检查空格字符的不同。
-B or --ignore-blank-lines 　#不检查空白行。
-c 　#显示全部内文，并标出不同之处。
-C or --context 　#与执行"-c-"指令相同。
-d or --minimal 　#使用不同的演算法，以较小的单位来做比较。
-D or ifdef 　#此参数的输出格式可用于前置处理器巨集。
-e or --ed 　#此参数的输出格式可用于ed的script文件。
-f or -forward-ed 　#输出的格式类似ed的script文件，但按照原来文件的顺序来显示不同处。
-H or --speed-large-files 　#比较大文件时，可加快速度。
-l or --ignore-matching-lines 　#若两个文件在某几行有所不同，而这几行同时都包含了选项中指定的字符 or 字符串，则不显示这两个文件的差异。
-i or --ignore-case 　#不检查大小写的不同。
-l or --paginate 　#将结果交由pr程序来分页。
-n or --rcs 　#将比较结果以RCS的格式来显示。
-N or --new-file 　#在比较目录时，若文件A仅出现在某个目录中，预设会显示：Only in目录：文件A若使用-N参数，则diff会将文件A与一个空白的文件比较。
-p 　#若比较的文件为C语言的程序码文件时，显示差异所在的函数名称。
-P or --unidirectional-new-file 　#与-N类似，但只有当第二个目录包含了一个第一个目录所没有的文件时，才会将这个文件与空白的文件做比较。
-q or --brief 　#仅显示有无差异，不显示详细的信息。
-r or --recursive 　#比较子目录中的文件。
-s or --report-identical-files 　#若没有发现任何差异，仍然显示信息。
-S or --starting-file 　#在比较目录时，从指定的文件开始比较。
-t or --expand-tabs 　#在输出时，将tab字符展开。
-T or --initial-tab 　#在每行前面加上tab字符以便对齐。
-u,-U or --unified= 　#以合并的方式来显示文件内容的不同。
-v or --version 　#显示版本信息。
-w or --ignore-all-space 　#忽略全部的空格字符。
-W or --width 　#在使用-y参数时，指定栏宽。
-x or --exclude 　#不比较选项中所指定的文件 or 目录。
-X or --exclude-from 　#您可以将文件 or 目录类型存成文本文件，然后在=中指定此文本文件。
-y or --side-by-side 　#以并列的方式显示文件的异同之处。
```



# 37. ping 用于确定主机与外部连接状态



```
ping [参数] [主机名或IP地址]
-d 使用Socket的SO_DEBUG功能。
-f  极限检测。大量且快速地送网络封包给一台机器，看它的回应。
-n 只输出数值。
-q 不显示任何传送封包的信息，只显示最后的结果。
-r 忽略普通的Routing Table，直接将数据包送到远端主机上。通常是查看本机的网络接口是否有问题。
-R 记录路由过程。
-v 详细显示指令的执行过程。
<p>-c 数目：在发送指定数目的包后停止。
-i 秒数：设定间隔几秒送一个网络封包给一台机器，预设值是一秒送一次。
-I 网络界面：使用指定的网络界面送出数据包。
-l 前置载入：设置在送出要求信息之前，先行发出的数据包。
-p 范本样式：设置填满数据包的范本样式。
-s 字节数：指定发送的数据字节数，预设值是56，加上8字节的ICMP头，一共是64ICMP数据字节。
-t 存活数值：设置存活数值TTL的大小。
```



# 38. ssh 命令用于远程登录上Linux主机

```
ssh [-l login_name] [-p port] [user@]hostname
```



# 39. scp 命令是secure copy的简写，用于在Linux下进行远程拷贝文件的命令，和它类似的命令有cp，不过cp只是在本机进行拷贝不能跨服务器，而且scp传输是加密的



```
scp [参数] [原路径] [目标路径]

-1 强制scp命令使用协议ssh1 
-2 强制scp命令使用协议ssh2 
-4 强制scp命令只使用IPv4寻址 
-6 强制scp命令只使用IPv6寻址 
-B 使用批处理模式（传输过程中不询问传输口令或短语） 
-C 允许压缩。（将-C标志传递给ssh，从而打开压缩功能） 
-p 保留原文件的修改时间，访问时间和访问权限。 
-q 不显示传输进度条。 
-r 递归复制整个目录。 
-v 详细方式显示输出。scp和ssh(1)会显示出整个过程的调试信息。这些信息用于调试连接，验证和配置问题。 
-c cipher 以cipher将数据传输进行加密，这个选项将直接传递给ssh。 
-F ssh_config 指定一个替代的ssh配置文件，此参数直接传递给ssh。 
-i identity_file 从指定文件中读取传输时使用的密钥文件，此参数直接传递给ssh。 
-l limit 限定用户所能使用的带宽，以Kbit/s为单位。 
-o ssh_option 如果习惯于使用ssh_config(5)中的参数传递方式， 
-P port 注意是大写的P, port是指定数据传输用到的端口号 
-S program 指定加密传输时所使用的程序。此程序必须能够理解ssh(1)的选项。
```



# 40. telnet 命令用来远程登录操作

```
 telnet[参数][主机]

-8 允许使用8位字符资料，包括输入与输出。
-a 尝试自动登入远端系统。
-b<主机别名> 使用别名指定远端主机名称。
-c 不读取用户专属目录里的.telnetrc文件。
-d 启动排错模式。
-e<脱离字符> 设置脱离字符。
-E 滤除脱离字符。
-f 此参数的效果和指定"-F"参数相同。
-F 使用Kerberos V5认证时，加上此参数可把本地主机的认证数据上传到远端主机。
-k<域名> 使用Kerberos认证时，加上此参数让远端主机采用指定的领域名，而非该主机的域名。
-K 不自动登入远端主机。
-l<用户名称> 指定要登入远端主机的用户名称。
-L 允许输出8位字符资料。
-n<记录文件> 指定文件记录相关信息。
-r 使用类似rlogin指令的用户界面。
-S<服务类型> 设置telnet连线所需的IP TOS信息。
-x 假设主机有支持数据加密的功能，就使用它。
-X<认证形态> 关闭指定的认证形态。
```



# 41. wget 是从远程下载的工具

```
wget [参数] [URL地址]

启动参数：
-V, –version 显示wget的版本后退出
-h, –help 打印语法帮助
-b, –background 启动后转入后台执行
-e, –execute=COMMAND 执行`.wgetrc’格式的命令，wgetrc格式参见/etc/wgetrc或~/.wgetrc

记录和输入文件参数：
-o, –output-file=FILE 把记录写到FILE文件中
-a, –append-output=FILE 把记录追加到FILE文件中
-d, –debug 打印调试输出
-q, –quiet 安静模式(没有输出)
-v, –verbose 冗长模式(这是缺省设置)
-nv, –non-verbose 关掉冗长模式，但不是安静模式
-i, –input-file=FILE 下载在FILE文件中出现的URLs
-F, –force-html 把输入文件当作HTML格式文件对待
-B, –base=URL 将URL作为在-F -i参数指定的文件中出现的相对链接的前缀
–sslcertfile=FILE 可选客户端证书
–sslcertkey=KEYFILE 可选客户端证书的KEYFILE
–egd-file=FILE 指定EGD socket的文件名

下载参数：
–bind-address=ADDRESS 指定本地使用地址(主机名或IP，当本地有多个IP或名字时使用)
-t, –tries=NUMBER 设定最大尝试链接次数(0 表示无限制).
-O –output-document=FILE 把文档写到FILE文件中
-nc, –no-clobber 不要覆盖存在的文件或使用.#前缀
-c, –continue 接着下载没下载完的文件
–progress=TYPE 设定进程条标记
-N, –timestamping 不要重新下载文件除非比本地文件新
-S, –server-response 打印服务器的回应
–spider 不下载任何东西
-T, –timeout=SECONDS 设定响应超时的秒数
-w, –wait=SECONDS 两次尝试之间间隔SECONDS秒
–waitretry=SECONDS 在重新链接之间等待1…SECONDS秒
–random-wait 在下载之间等待0…2*WAIT秒
-Y, –proxy=on/off 打开或关闭代理
-Q, –quota=NUMBER 设置下载的容量限制
–limit-rate=RATE 限定下载输率

目录参数：
-nd –no-directories 不创建目录
-x, –force-directories 强制创建目录
-nH, –no-host-directories 不创建主机目录
-P, –directory-prefix=PREFIX 将文件保存到目录 PREFIX/…
–cut-dirs=NUMBER 忽略 NUMBER层远程目录

HTTP 选项参数：
–http-user=USER 设定HTTP用户名为 USER.
–http-passwd=PASS 设定http密码为 PASS
-C, –cache=on/off 允许/不允许服务器端的数据缓存 (一般情况下允许)
-E, –html-extension 将所有text/html文档以.html扩展名保存
–ignore-length 忽略 `Content-Length’头域
–header=STRING 在headers中插入字符串 STRING
–proxy-user=USER 设定代理的用户名为 USER
–proxy-passwd=PASS 设定代理的密码为 PASS
–referer=URL 在HTTP请求中包含 `Referer: URL’头
-s, –save-headers 保存HTTP头到文件
-U, –user-agent=AGENT 设定代理的名称为 AGENT而不是 Wget/VERSION
–no-http-keep-alive 关闭 HTTP活动链接 (永远链接)
–cookies=off 不使用 cookies
–load-cookies=FILE 在开始会话前从文件 FILE中加载cookie
–save-cookies=FILE 在会话结束后将 cookies保存到 FILE文件中

FTP 选项参数：
-nr, –dont-remove-listing 不移走 `.listing’文件
-g, –glob=on/off 打开或关闭文件名的 globbing机制
–passive-ftp 使用被动传输模式 (缺省值).
–active-ftp 使用主动传输模式
–retr-symlinks 在递归的时候，将链接指向文件(而不是目录)

递归下载参数：
-r, –recursive 递归下载－－慎用!
-l, –level=NUMBER 最大递归深度 (inf 或 0 代表无穷)
–delete-after 在现在完毕后局部删除文件
-k, –convert-links 转换非相对链接为相对链接
-K, –backup-converted 在转换文件X之前，将之备份为 X.orig
-m, –mirror 等价于 -r -N -l inf -nr
-p, –page-requisites 下载显示HTML文件的所有图片

递归下载中的包含和不包含(accept/reject)：
-A, –accept=LIST 分号分隔的被接受扩展名的列表
-R, –reject=LIST 分号分隔的不被接受的扩展名的列表
-D, –domains=LIST 分号分隔的被接受域的列表
–exclude-domains=LIST 分号分隔的不被接受的域的列表
–follow-ftp 跟踪HTML文档中的FTP链接
–follow-tags=LIST 分号分隔的被跟踪的HTML标签的列表
-G, –ignore-tags=LIST 分号分隔的被忽略的HTML标签的列表
-H, –span-hosts 当递归时转到外部主机
-L, –relative 仅仅跟踪相对链接
-I, –include-directories=LIST 允许目录的列表
-X, –exclude-directories=LIST 不被包含目录的列表
-np, –no-parent 不要追溯到父目录
wget -S –spider url 不下载只显示过程
```



# 42. ifconfig　命令用来查看和配置网络设备。当网络环境发生改变时可通过此命令对网络进行相应的配置



```
 ifconfig [网络设备] [参数]

up 启动指定网络设备/网卡。
down 关闭指定网络设备/网卡。该参数可以有效地阻止通过指定接口的IP信息流，如果想永久地关闭一个接口，我们还需要从核心路由表中将该接口的路由信息全部删除。
arp 设置指定网卡是否支持ARP协议。
-promisc 设置是否支持网卡的promiscuous模式，如果选择此参数，网卡将接收网络中发给它所有的数据包
-allmulti 设置是否支持多播模式，如果选择此参数，网卡将接收网络中所有的多播数据包
-a 显示全部接口信息
-s 显示摘要信息（类似于 netstat -i）
add 给指定网卡配置IPv6地址
del 删除指定网卡的IPv6地址
<硬件地址> 配置网卡最大的传输单元
mtu<字节数> 设置网卡的最大传输单元 (bytes)
netmask<子网掩码> 设置网卡的子网掩码。掩码可以是有前缀0x的32位十六进制数，也可以是用点分开的4个十进制数。如果不打算将网络分成子网，可以不管这一选项；如果要使用子网，那么请记住，网络中每一个系统必须有相同子网掩码。
tunel 建立隧道
dstaddr 设定一个远端地址，建立点对点通信
-broadcast<地址> 为指定网卡设置广播协议
-pointtopoint<地址> 为网卡设置点对点通讯协议
multicast 为网卡设置组播标志
address 为网卡设置IPv4地址
txqueuelen<长度> 为网卡设置传输列队的长度
```



# 43. route

```
 route [-f] [-p] [Command [Destination] [mask Netmask] [Gateway] [metric Metric]] [if Interface]] 
-c  # 显示更多信息
-n  # 不解析名字
-v  # 显示详细的处理信息
-F  # 显示发送信息
-C # 显示路由缓存
-f  # 清除所有网关入口的路由表。 
-p # 与 add 命令一起使用时使路由具有永久性。
```



# 44. whereis 命令

```
# whereis命令只能用于程序名的搜索，而且只搜索二进制文件（参数-b）、man说明文件（参数-m）和源代码文件（参数-s）。如果省略参数，则返回所有信息。和find相比，whereis查找的速度非常快，这是因为linux系统会将 系统内的所有文件都记录在一个数据库文件中，当使用whereis和locate时，会从数据库中查找数据，而不是像find命令那样，通过遍历硬盘来查找，效率自然会很高。 但是该数据库文件并不是实时更新，默认情况下时一星期更新一次，因此，我们在用whereis和locate 查找文件时，有时会找到已经被删除的数据，或者刚刚建立文件，却无法查找到，原因就是因为数据库文件没有被更新。 

 whereis [-bmsu] [BMS 目录名 -f ] 文件名
 
 -b 定位可执行文件。
　-m 定位帮助文件。
　-s 定位源代码文件。
　-u 搜索默认路径下除可执行文件、源代码文件、帮助文件以外的其它文件。
　-B 指定搜索可执行文件的路径。
　-M 指定搜索帮助文件的路径。
　-S 指定搜索源代码文件的路径。
```

　

# 45. locate　命令

```
＃　locate命令可以在搜寻数据库时快速找到档案，数据库由updatedb程序来更新，updatedb是由cron daemon周期性建立的，locate命令在搜寻数据库时比由整个由硬盘资料来搜寻资料来得快，但较差劲的是locate所找到的档案若是最近才建立或 刚更名的，可能会找不到，在内定值中，updatedb每天会跑一次，可以由修改crontab来更新设定值。(etc/crontab)

Locate [选择参数] [样式]
-e   将排除在寻找的范围之外。
-1  如果 是 1．则启动安全模式。在安全模式下，使用者不会看到权限无法看到 的档案。这会始速度减慢，因为 locate 必须至实际的档案系统中取得档案的 权限资料。
-f   将特定的档案系统排除在外，例如我们没有到理要把 proc 档案系统中的档案 放在资料库中。
-q  安静模式，不会显示任何错误讯息。
-n 至多显示 n个输出。
-r 使用正规运算式 做寻找的条件。
-o 指定资料库存的名称。
-d 指定资料库的路径
-h 显示辅助讯息
-V 显示程式的版本讯息
```



# 46. which 会在PATH变量指定的路径中，搜索某个系统命令的位置，并且返回第一个搜索结果



```
which 可执行文件名称 
-n 　指定文件名长度，指定的长度必须大于或等于所有文件中最长的文件名。
-p 　与-n参数相同，但此处的包括了文件的路径。
-w 　指定输出时栏位的宽度。
-V 　显示版本信息
```



# 47. grep命令

命令常用于分析一行的信息，若当中有我们所需要的信息，就将该行显示出来，该命令通常与管道命令一起使用，用于对一些命令的输出进行筛选加工等等，比如可以加在ps, tail, cat后面

```
grep [-acinv] [--color=auto] '查找字符串' filename
```



# 48. clear 命令用于清除终端屏幕上现有的文字内容，将之上滚



# 9. date 命令用于显示或设定时间

```
date [参数]... [+格式]

必要参数:
%H 小时(以00-23来表示)。 
%I 小时(以01-12来表示)。 
%K 小时(以0-23来表示)。 
%l 小时(以0-12来表示)。 
%M 分钟(以00-59来表示)。 
%P AM或PM。 
%r 时间(含时分秒，小时以12小时AM/PM来表示)。 
%s 总秒数。起算时间为1970-01-01 00:00:00 UTC。 
%S 秒(以本地的惯用法来表示)。 
%T 时间(含时分秒，小时以24小时制来表示)。 
%X 时间(以本地的惯用法来表示)。 
%Z 市区。 
%a 星期的缩写。 
%A 星期的完整名称。 

%b 月份英文名的缩写。 
%B 月份的完整英文名称。 
%c 日期与时间。只输入date指令也会显示同样的结果。 
%d 日期(以01-31来表示)。 
%D 日期(含年月日)。 
%j 该年中的第几天。 
%m 月份(以01-12来表示)。 
%U 该年中的周数。 
%w 该周的天数，0代表周日，1代表周一，异词类推。 
%x 日期(以本地的惯用法来表示)。 
%y 年份(以00-99来表示)。 
%Y 年份(以四位数来表示)。 
%n 在显示时，插入新的一行。 
%t 在显示时，插入tab。 
MM 月份(必要) 
DD 日期(必要) 
hh 小时(必要) 
mm 分钟(必要)
ss 秒(选择性) 

选择参数:
-d<字符串> 　显示字符串所指的日期与时间。字符串前后必须加上双引号。 
-s<字符串> 　根据字符串来设置日期与时间。字符串前后必须加上双引号。 
-u 　显示GMT。
```



# 50. ln 命令是为某一个文件在另外一个位置建立一个同步的链接

```
Linux文件系统中，有所谓的链接(link)，我们可以将其视为档案的别名，而链接又可分为两种 : 硬链接(hard link)与软链接(symbolic link)，硬链接的意思是一个档案可以有多个名称，而软链接的方式则是产生一个特殊的档案，该档案的内容是指向另一个档案的位置。硬链接是存在同一个文件系统中，而软链接却可以跨越不同的文件系统。

软链接：
1.软链接，以路径的形式存在。类似于Windows操作系统中的快捷方式
2.软链接可以 跨文件系统 ，硬链接不可以
3.软链接可以对一个不存在的文件名进行链接
4.软链接可以对目录进行链接

硬链接:
1.硬链接，以文件副本的形式存在。但不占用实际空间。
2.不允许给目录创建硬链接
3.硬链接只有在同一个文件系统中才能创建

ln [参数][源文件或目录][目标文件或目录]

必要参数:
-b 删除，覆盖以前建立的链接
-d 允许超级用户制作目录的硬链接
-f 强制执行
-i 交互模式，文件存在则提示用户是否覆盖
-n 把符号链接视为一般目录
-s 软链接(符号链接)
-v 显示详细的处理过程

选择参数:
-S “-S<字尾备份字符串> ”或 “--suffix=<字尾备份字符串>”
-V “-V<备份方式>”或“--version-control=<备份方式>”
```

