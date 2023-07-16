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

        stage("Code coverage") {     
	        steps {          
			    bat "./gradlew jacocoTestReport"          
			    
			    bat "./gradlew jacocoTestCoverageVerification"     

			}
        }
    }
}
