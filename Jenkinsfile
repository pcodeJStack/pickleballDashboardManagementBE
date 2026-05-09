pipeline {
    agent any

    stages {

        stage('Deploy') {
            steps {
                sh '''
                cd /opt/app/pickleballDashboardManagementBE

                git pull

                chmod +x mvnw

                ./mvnw clean package -DskipTests

                docker-compose down

                docker-compose build --no-cache

                docker-compose up -d
                '''
            }
        }
    }
}
