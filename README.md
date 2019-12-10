# Nousin SpringCloudProject

<p>
<!-- <a href="#公众号"><img src="http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/badge/%E5%85%AC%E4%BC%97%E5%8F%B7-macrozheng-blue.svg" alt="公众号"></a> -->
</p>

## 简介

Spring Cloud 相关技术一站式应用。一套涵盖大部分核心组件使用的Spring Cloud项目，包括Spring Cloud Alibaba及分布式事务[Seata](https://seata.io/zh-cn/)的应用，基于Spring Cloud Hoxton版本以及SpringBoot 2.2.2.RELEASE版本。

## 项目结构

``` lua
Spring Cloud Content
├── gateway -- gateway作为网关的测试服务
├── order-service --订单服务提供商

```

##参照
生成项目：https://start.spring.io/#!type=maven-project&language=java&platformVersion=2.2.2.RELEASE&packaging=jar&jvmVersion=1.8&groupId=com.nousin.springcloud.server&artifactId=account&name=account&description=Server%20Provider%20project%20for%20Spring%20Cloud%20based%20on%20Spring%20Boot&packageName=com.nousin.springcloud.server.account&dependencies=actuator,lombok,web,cloud-ribbon,cloud-hystrix,cloud-feign
