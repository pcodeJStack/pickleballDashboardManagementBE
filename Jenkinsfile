pipeline {
    agent any

    environment {
        IMAGE_NAME = "phucitdev/pickleball-backend:latest"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/pcodeJStack/pickleballDashboardManagementBE.git'
            }
        }

        stage('Build Maven') {
            steps {
                sh '''
                chmod +x mvnw
                ./mvnw clean package -DskipTests
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                docker build -t $IMAGE_NAME .
                '''
            }
        }

        stage('Push Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
                    sh '''
                    echo $PASS | docker login -u $USER --password-stdin
                    docker push $IMAGE_NAME
                    '''
                }
            }
        }

        stage('Deploy to VPS') {
            steps {
                sh '''
                ssh root@178.128.113.47 "
                docker pull phucitdev/pickleball-backend:latest &&
                docker stop backend || true &&
                docker rm backend || true &&
                docker run -d \
                --name backend \
                --network pickleball-network \
                -p 8080:8080 \
                --restart always \
                phucitdev/pickleball-backend:latest                "
                '''
            }
        }
    }
}