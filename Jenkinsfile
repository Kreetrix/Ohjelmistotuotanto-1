pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Kreetrix/Ohjelmistotuotanto-1'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean install -DskipTests'
            }
        }

        stage('Test') {
                    steps {
                        withCredentials([file(credentialsId: 'my-env-file', variable: '.env	')]) {
                            bat "copy %.env	% src\\test\\resources\\.env"
                            bat "mvn test"
                        }
                    }
                }

        stage('Code Coverage') {
            steps {
                bat 'mvn jacoco:report'
            }
        }

        stage('Publish Test Results') {
            steps {
                junit '**/target/surefire-reports/*.xml'
            }
        }
    }
}
