# PT-OCR

## 简介

> 使用JavaFx开发的本地截图工具。
> 截图框选后进行OCR识别，OCR引擎使用RapidOCR，默认使用ONNX V4模型。
> PS: 为了使用高精度版本的OCR模型，这里从原github项目上Fork了新项目，需要拉取到本地后，先增加Server版本的高精度Model，然后自行打包依赖。

> 项目地址：``https://github.com/WZJ-Study/RapidOcr-Java``

> 原项目中未添加高精度的OCR模型的说明：
> # models
> Due to file size limitations, the server model is not added by default.
> If you need the server model, please download it from here and add it to the current directory, then repackage.
> - https://huggingface.co/SWHL/RapidOCR/tree/main/PP-OCRv4


## 占用本地端口
8888

## OCR采集结果

1. 通过Http接口 ``GET http://localhost:8888/ocr/data`` 可以在本机浏览器中查询实时采集结果；
2. 采集结果保存到本地的Sqlite数据库；
3. 采集结果Json文件和截图保存到本地目录下；
4. 通过配置Http回调钩子URL，可以将采集结果推送到远程服务器；
> 这里要求POST接口, 以RequestBody接收JSON文本，返回字符串。
> JSON文件数据结构示例：
> ```json
> {
>     "ipAddr": "172.28.137.29", 
>     "hostName": "PC-OFFICE-WZJ", 
>     "data": [
>         {
>             "name": "字段aaa", 
>             "position": "(73;226;159;162)", 
>             "type": "文本", 
>             "value": "OCR识别结果文本111",
>             "collectTime": "2025-03-22 16:38:43"
>         },
>         {
>             "name": "字段bbb", 
>             "position": "(12;345;456;568)", 
>             "type": "文本", 
>             "value": "OCR识别结果文本222",
>             "collectTime": "2025-03-22 16:38:43"
>         }
>     ],
>     "sendTime": "2025-03-22 16:38:43"
> }
> ```


# 项目打包exe说明
1. 首先 ``mvn clean``，然后 ``mvn package``，这样在target目录下会生成一个zip包，例如：``PT-OCR-0.0.1-windows.zip``
2. 以上生成的zip包是不含Java运行时环境的，如果机器上安装有JDK11，那么可以直接使用；
3. 否则，需要先把zip压缩包解压，然后把JDK11的目录整个放入到解压后的根目录下，如下图所示：
![3-1.jdk11目录.png](readme/3-1.jdk11%E7%9B%AE%E5%BD%95.png)
里面就是完整的jdk11
![3-2.jdk11目录内.png](readme/3-2.jdk11%E7%9B%AE%E5%BD%95%E5%86%85.png)
4. 然后，修改配置文件 ``PT.OCR.ini``，如下图所示：
![4.修改ini配置文件.png](readme/4.%E4%BF%AE%E6%94%B9ini%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6.png)
5. 将 ``vm.location`` 配置中的 ``%JAVA_HOME%`` 修改为当前目录下的JDK11目录``.\jdk11``。
![5.ini配置文件修改内容.png](readme/5.ini%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6%E4%BF%AE%E6%94%B9%E5%86%85%E5%AE%B9.png)
6. 最后，把整个目录重新打包为zip包``PT-OCR-0.0.1-windows-with-jdk.zip``，这样发布后就可以免安装本地Java运行时环境，直接使用本地jdk11允许了。
