pipeline {
    agent any

    stages {
        stage  ('Scan using Gradle') {
            steps {
                withSonarQubeEnv(installationName: 'sonar', credentialsId: 'SonarQubeToken') {
                    bat "./gradlew sonar"
                }
            }
        }
    }
}

    post {
        success {
            jacoco(
                execPattern: '**/build/jacoco/*.exec',
                classPattern: '**/build/classes/java/main',
                sourcePattern: '**/src/main'
            )
        }
    }
