@Library('hawaii')
import com.ag2rlm.pacific.Hawaii

// Declarative pipeline
pipeline {
    //definition de l'agent
    agent {label "linux"}

    tools {
        jdk "JDK sun 1.8"
        maven "Maven 3.2.5"
    }

    // declaration des options (connexion gitlab, configuration...)
    options {
        disableConcurrentBuilds()
        gitLabConnection('gitlab-jenkins')
        gitlabBuilds(builds: ['test', 'coverage', 'quality', 'build', 'upload', 'deploy'])
    }

    triggers {
        gitlab(
          triggerOnPush: true,
          triggerOnMergeRequest: true,
          skipWorkInProgressMergeRequest: true,
          triggerOpenMergeRequestOnPush: 'both',
          acceptMergeRequestOnSuccess: false,
          branchFilterType: "RegexBasedFilter",
          targetBranchRegex: "master|release/.*|feature/.*"
        )
    }

    stages {

        stage('test') {
                steps {
                    updateGitlabCommitStatus name: 'test', state: 'success'
                }
        }

        stage('coverage') {
                steps {
                    updateGitlabCommitStatus name: 'coverage', state: 'success'
                }
        }


        stage('quality') {
            steps {
                updateGitlabCommitStatus name: 'quality', state: 'success'
            }
        }

        stage('build') {
            steps {
                updateGitlabCommitStatus name: 'build', state: 'success'
            }
        }

        stage('upload'){
            steps{
                updateGitlabCommitStatus name: 'upload', state: 'success'
            }
        }

        stage('deploy'){
            steps{
                updateGitlabCommitStatus name: 'deploy', state: 'success'
            }
        }

    }
}
