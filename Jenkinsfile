pipeline {
    agent any

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

        stage('Deploy') {
           steps {
        sh '''
        cd /opt/app/pickleballDashboardManagementBE
        git fetch --all
        git reset --hard origin/main
        chmod +x mvnw
        ./mvnw clean package -DskipTests
        docker-compose down
        docker-compose up -d --build
        '''
           }
        }
    }
}
