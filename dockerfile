# 第一阶段：构建并提取分层
FROM openjdk:17-slim AS builder
WORKDIR /java
ARG JAR_FILE=main-module/target/toolserve-3.0.0.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# 第二阶段：构建最终镜像
FROM openjdk:17-slim
WORKDIR /java
COPY --from=builder /java/dependencies/ ./
COPY --from=builder /java/spring-boot-loader/ ./
COPY --from=builder /java/snapshot-dependencies/ ./
COPY --from=builder /java/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
EXPOSE 8081
