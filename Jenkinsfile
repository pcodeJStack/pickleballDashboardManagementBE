pipeline {
    agent any

    stages {

        stage('Build Maven') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Docker Compose Down') {
            steps {
                sh 'docker compose down'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker compose build --no-cache'
            }
        }

        stage('Docker Up') {
            steps {
                sh 'docker compose up -d'
            }
        }
    }
}
