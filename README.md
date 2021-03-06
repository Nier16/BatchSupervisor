# Bienvenue

En tant qu'utilisateur, vous avez accès à différents outils selon la configuration du projet.

** Intégration continue **

Jenkins est un outil logiciel d’intégration continu. Il permet de tester et de rapporter les changements effectués en temps réels.
Vous pouvez néanmoins choisir de désactiver l'intégration continue si vous souhaitez ne pas l'utiliser sur le projet.

Il faut tout simplement décocher la case *Active*.

Vous y avez accès via :
> Settings -> Integrations -> Jenkins CI

Dans le cas où l'intégration continue est utilisée, vous pouvez consulter votre job via : 

> [Votre projet sous Cloudbees](http://ice/job/A1618/job/xib/)


** SONARQUBE **

Sonarqube, est un logiciel open source permettant de tester et de mesurer la qualité du code source de façon continue.
Cet outil fournit des rapports sur plusieurs mesures comme la duplication de code, le niveau de documentation, la détection de bugs potentiels ou encore l'évaluation des tests unitaires sur le code.

> [Votre projet sous Sonar](http://ice-sonar/dashboard?id=A1618:xib)
	

** PACIFIC **

PACIFIC est la mise en oeuvre d'un ensemble d'outils liés à l'intégration continue. Elle vise à simplifier l'usage de l'IC en fournissant un ensemble cohérent d'outils préconfigurés, ainsi que des exemples d'usage de ces outils. L'objectif étant de faire de l'IC un standard, tout en considérant la migration vers ces outils comme un non-évènement.

> Vous pouvez consulter la documentation Pacific pour toutes informations supplémentaires - [Documentation](https://git-prd.server.lan/A0961/PACIFIC)

Il faut savoir que deux fichiers sont générés par défaut lors de la création de votre projet :

- Jenkins File : C'est un template que l'on peut compléter selon nos besoins.
Il permet de faire le lien entre Cloudbees et Gitlab, c'est ce fichier qui est déclenché lors du lancement d'un pipeline.
[Jenkinsfile](https://git-prd.server.lan/A1618/xib/blob/master/Jenkinsfile). 
- GitIgnore : 
Par défaut, le Gitignore est complet. Vous pouvez cependant à tout moment ajouter manuellement des fichiers si besoin est, dans le [.gitignore](https://git-prd.server.lan/A1618/xib/blob/master/.gitignore). 

** GITFLOW **

> Documentation du Gitflow AG2R - [Gitflow](https://git-prd.server.lan/Formation/TP/blob/master/docs/workflow.md)


