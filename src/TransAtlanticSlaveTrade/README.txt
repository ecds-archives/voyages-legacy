This file contains the basic instructions for deploying the tast project.

For excruciating detail see the following links:
https://techknowhow.library.emory.edu/slave-voyages-database/first-time-deployment-instructions
OR
https://techknowhow.library.emory.edu/slave-voyages-database/instructions-build-and-deploy-voyages-project

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

For instructions on how to import / refresh data into the voyages table see the following URL
https://techknowhow.library.emory.edu/slave-voyages-database/import-refresh-data-voyages-table 




