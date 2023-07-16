pipeline {
    agent any

    stage ('Scan using Gradle') {
        steps {
            withSonarQubeEnv(installationName: 'sonar', credentialsId: 'SonarQubeToken') {
                sh "./gradlew sonarqube \
                  -Dsonar.projectKey=${serviceName} \
                  -Dsonar.host.url=${env.SONAR_HOST_URL} \
                  -Dsonar.login=${env.SONAR_AUTH_TOKEN} \
                  -Dsonar.projectName=${serviceName} \
                  -Dsonar.projectVersion=${BUILD_NUMBER}"
            }
        }
    }
}