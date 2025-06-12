pipeline {
  agent any

  environment {
    IMAGE = 'yourdockerhub/java-docker-jenkins'
    DOCKER_CREDS = credentials('dockerhub') // Add DockerHub credentials in Jenkins
  }

  stages {
    stage('Checkout') {
      steps {
        git 'https://github.com/yourusername/java-docker-jenkins.git' // update if hosted
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
          EXPOSE 8080
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
          docker.withRegistry('', DOCKER_CREDS) {
            docker.image(IMAGE).push('latest')
          }
        }
      }
    }

    stage('Run Container') {
      steps {
        sh 'docker rm -f javaapp || true'
        sh 'docker run -d -p 8080:8080 --name javaapp $IMAGE'
      }
    }
  }
}

