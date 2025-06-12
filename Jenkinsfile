pipeline {
  agent any

  environment {
    IMAGE = 'bhuvanraj123/containerized-java'
    DOCKER_CREDS = credentials('bhuvanraj123:dckr_pat_dB9sX2Xla_3GxMPDPxPDKCbzFmE') // Add DockerHub credentials in Jenkins
  }

  stages {
    stage('Checkout') {
  steps {
    git branch: 'main', url: 'https://github.com/bhuvan-raj/Jenkins-JavaDockerized.git'
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

