pipeline {
  agent any
  stages {
    stage("verify tooling") {
      steps {
        sh '''
          docker version
          docker info
          docker-compose version 
          curl --version
          jq --version
        '''
      }
    }
        stage('Change fileName on the pom file') {
      steps {
        sh 'sed -i "s_war_jar_g" pom.xml'
        sh 'sed -i "s_</build>_<finalName>hcodata</finalName></build>_g" pom.xml'
      }
    }
          stage('Build the Jar file') {
      steps {
        sh 'mvn clean'
        sh 'mvn install -DskipTests'
      }
    }
          stage('Build docker image') {
      steps {
        sh 'docker build -t hcodata.jar .'
      }
    }
          stage('Copy docker image') {
      steps {
        sh 'docker cp ./target/hcodata.jar e88686ad48cf:./'
      }
    }
    stage('Restart container') {
      steps {
        sh 'docker restart e88686ad48cf'
        sh 'docker ps -f name=hcodata_API_1'
      }
    }
  }
}