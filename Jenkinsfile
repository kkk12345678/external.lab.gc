pipeline {
    agent any

    stages {
        stage  ('Scan using Gradle') {
            steps {
                withSonarQubeEnv(installationName: 'SonarQubeScanner', credentialsId: 'SonarQubeToken') {
                    sh "./gradlew sonarqube"
                }
            }
        }
    }
}