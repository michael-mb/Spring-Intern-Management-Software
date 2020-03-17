pipeline {
  agent { label 'salespoint-ci' }
  environment {
    MAVEN_OPTS = '-Xmx1G'
  }
  stages {
    stage('Build') {
      steps {
        sh './mvnw clean verify -Pci -B'
      }
    }
    stage('Analyze') {
      when {
        branch 'master'
      }
      steps {
        sh './mvnw sonar:sonar -B'
      }
    }
  }
  post {
    always {
      junit 'target/surefire-reports/*.xml'
      archiveArtifacts artifacts: 'target/*.zip,target/*.jar'
    }
  }
}
