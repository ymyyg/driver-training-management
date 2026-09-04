# 使用 Java 17 运行环境作为基础镜像
FROM eclipse-temurin:17-jre

# 设置容器内工作目录
WORKDIR /app

# 把打包好的 jar 复制到容器中
COPY target/driver-training-management-0.0.1-SNAPSHOT.jar app.jar

# 暴露 Spring Boot 默认端口
EXPOSE 8080

# 容器启动时执行的命令
ENTRYPOINT ["java", "-jar", "app.jar"]

