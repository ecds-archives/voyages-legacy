This file contains the basic instructions for deploying the tast project.

For excruciating detail see the following links:
https://techknowhow.library.emory.edu/content/first-time-deployment-instructions
OR
https://techknowhow.library.emory.edu/content/instructions-build-and-deploy-voyages-project

Sensitive information is keep there:
https://spiderman.library.emory.edu/syswiki/index.php?title=Slave_Voyages_Info

Basic deploy
1.	Copy current war file to /opt/tomcat/oldwars
2.	Redirect site to static page or put tomcat in maintenance mode
3.	Backup code and database as necessary
4.	Shutdown tomcat
5.	Run any SQL scripts if necessary
6.	Clear the cache by deleting everything in /opt/tomcat/work directory
7.	Delete webapps/tast.war and webapps/tast
8.	Move new war file into webapps directory
9.	Restart tomcat




