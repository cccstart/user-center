# 基础镜像
FROM maven:3.8.1-jdk-8-slim as builder

# 指定工作目录
WORKDIR /app

# 添加源码文件
COPY pom.xml .
COPY src ./src

# 构建 jar 包，跳过测试
RUN mvn package -DskipTests

# 启动命令
CMD ["java","-jar","/app/target/user-center-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]
