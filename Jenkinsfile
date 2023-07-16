pipeline {
    agent any

    stages {
        stage  ('Scan using Gradle') {
            steps {
                withSonarQubeEnv(installationName: 'sonar', credentialsId: 'SonarQubeToken') {
                    sh "./gradlew sonarqube"
                }
            }
        }
    }
}