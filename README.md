# GSM

短信猫部署手册

1.	跑起GSM项目需要向jdk下放两个文件分别是：（文件位置：光盘里面的数据\正版\java-V3.4.6\lib）

rxtxSerial.dll复制到 jdk1.6\jre\bin目录下。
javax.comm.properties 复制到 jdk1.6\jre\lib目录下

2.	安装短信猫的驱动程序。

3.	部署gsm项目 （通过内部test测试gsm运行正常）

4.	其他项目调用gsm向外暴露的wsdl接口既可。

5.	接口类：ISmsService中有两个方法。

方法一： 利用阻塞队列一次性发送多条短信，并返回发送结果，发送成功的手机号在前，失败的手机号在后，以‘；’号隔开Sms_Sendbatch_web(String telNum, String smsText);

方法二： 利用阻塞队列一次性发送多条短信，无返回结果 Sms_Sendbatch_web_NoResult(String telNum, String smsText);



