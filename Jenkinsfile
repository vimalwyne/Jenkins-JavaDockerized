pipeline {
  agent any
  environment {
    IMAGE = 'vimal1234jude/containerized-java'
    DOCKER_CREDS = credentials('dockerhub') // Make sure this ID exists in Jenkins
  }
  stages {
    stage('Checkout') {
      steps {
        git branch: 'main', url: 'https://github.com/vimalwyne/Jenkins-JavaDockerized.git'
      }
    }
    stage('Build JAR') {
      steps {
        sh 'mvn clean package -DskipTests'
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
          docker.build(IMAGE)
        }
      }
    }
    stage('Push to DockerHub') {
      steps {
        script {
          withCredentials([usernamePassword(credentialsId: 'docker-id', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
            sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin'
            sh 'docker push $IMAGE:latest'
            sh 'docker tag $IMAGE:latest $IMAGE:latest'
          }
        }
      }
    }
    stage('Run Container') {
      steps {
        sh 'docker rm -f javaapp || true'
        sh 'docker run -d -p 7500:8080 --name javaapp $IMAGE:latest'
      }
    }
  }
  post {
    always {
      sh 'docker logout'
    }
  }
}
