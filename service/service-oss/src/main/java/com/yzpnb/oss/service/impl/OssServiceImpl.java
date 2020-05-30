package com.yzpnb.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.yzpnb.oss.service.OssService;
import com.yzpnb.oss.utils.ConstandApplicationUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    /**上传头像，返回上传后文件的路径*/
    @Override
    public String uploadFileAvatar(MultipartFile file) {/**记住，MultipartFile很重要*/
        /**1、获取我们工具类中的值*/
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = ConstandApplicationUtils.ENDPOINT;
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = ConstandApplicationUtils.KEY_ID;
        String accessKeySecret = ConstandApplicationUtils.KEY_SECRET;
        String bucketName = ConstandApplicationUtils.BUCKET_NAME;
        /**
         * UUID.randomUUID用于随机生成一个uuid值，他们用-连接，比如w443-s456
         * toString将其转换为字符串
         * replace("-","") 将字符串中的-换为空白
         * */
        String uuid= UUID.randomUUID().toString().replace("-","");

        /**
         * 上传文件时，会根据我们文件名创建文件夹，例如2020/5/13/123.jpg
         * 那么它会根据路径创建2020文件夹然后创建5文件夹，然后创建13文件夹
         * 最后将123.jpg放在13文件夹中
         * 所以我们只需要将年月日拼接到路径中即可
         * 我们这里不使用原生的new Data()的方式
         * 我们使用前面引入过的joda-time工具类
         * */
        String dateTime=new DateTime().toString("yyyy/MM/dd");//自动将日期按照我指定的格式装换为字符串
        // <yourObjectName>上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
        /**将uuid拼接到文件名后面*/
        String objectName = dateTime+"/"+uuid+file.getOriginalFilename();


        /**2、获取OSS的实例*/
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        /**3、获取输入流然后上传*/
        //获取InputStream输入流
        InputStream inputStream=null;
        try {
            inputStream=file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 上传内容到指定的存储空间（bucketName）并保存为指定的文件名称（objectName）。
        ossClient.putObject(bucketName, objectName, inputStream);

        /**4、关闭OSS*/
        // 关闭OSSClient。
        ossClient.shutdown();

        /**获取图片url，阿里云存储url生成规则为https://仓库名.地域节点/文件名*/
        return "https://"+bucketName+"."+endpoint+"/"+objectName;
    }
}
