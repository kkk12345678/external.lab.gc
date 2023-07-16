pipeline {
    agent any

    stages {

        stage('Scan with SonarQube') {
            steps {
                withSonarQubeEnv(installationName: 'sonar', credentialsId: 'SonarQubeToken') {
                    bat "./gradlew sonar"
                }
            }
        }
    }
}
