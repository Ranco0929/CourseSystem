# 线上批改系统 服务器端

build并运行服务器:
    ./mvnw spring-boot:run
或者使用idea导入项目

源文件路径
    /src
        |
        -/main/java/org/guge/course/
            |
            -/controller
            |    不同页面的Mapping
            -/entity
            |    各表的实体
            -/repository
            |    自动实现的基础增删改查
            -/service
            |   事务操作
            -/result
            |   基础返回结果Result和Result生成器
            -/util
                工具 目前只有List <-> JSON转换器
