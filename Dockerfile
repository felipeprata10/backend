# Estágio 1: Compilação e Build com Maven
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app

# Copia os arquivos de configuração e dependências primeiro para aproveitar o cache do Docker
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código fonte e realiza o empacotamento pulando testes integrados
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio 2: Ambiente de Execução Ultra-leve (JRE Alpine)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Cria usuário não-root para segurança do container em produção
RUN addgroup -S teclojagroup && adduser -S teclojauser -G teclojagroup
USER teclojauser

# Copia apenas o .jar final gerado no estágio 1
COPY --from=build /app/target/*.jar app.jar

# Configurações de ambiente de produção
ENV PORT=8080
EXPOSE 8080

# CORREÇÃO: usa shell form (sh -c) para que ${PORT} seja substituído em runtime.
# O formato exec ["java", ...] não executa pelo shell e ignora variáveis de ambiente.
ENTRYPOINT ["sh", "-c", "java -jar -Dserver.port=${PORT} -Dspring.profiles.active=prod app.jar"]
