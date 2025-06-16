pipeline {
  agent any
  environment {
    IMAGE = 'vimal1234jude/containerized-java'
  }
  stages {
    stage('Checkout') {
      steps {
        git branch: 'main', url: 'https://github.com/vimalwyne/Jenkins-JavaDockerized.git'
      }
    }
    stage('Build JAR') {
      steps {
        bat 'mvn clean package -DskipTests'
      }
    }
    stage('Generate Dockerfile') {
      steps {
        script {
          writeFile file: 'Dockerfile', text: """
FROM eclipse-temurin:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY \${JAR_FILE} app.jar
EXPOSE 7500
ENTRYPOINT ["java", "-jar", "/app.jar"]
"""
        }
      }
    }
    stage('Build Docker Image') {
      steps {
        script {
          bat "docker build -t ${IMAGE}:latest ."
        }
      }
    }
    stage('Push to DockerHub') {
      steps {
        script {
          withCredentials([usernamePassword(credentialsId: 'dockerhub', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
            bat 'echo $DOCKER_PASSWORD$ | docker login -u $DOCKER_USERNAME$ --password-stdin'
            bat "docker push ${IMAGE}:latest"
          }
        }
      }
    }
    stage('Run Container') {
      steps {
        bat 'docker rm -f javaapp || exit 0'
        bat "docker run -d -p 7500:7500 --name javaapp ${IMAGE}:latest"
      }
    }
  }
  post {
    always {
      bat 'docker logout'
    }
  }
}
