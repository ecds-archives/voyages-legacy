#!/bin/sh
bin/shutdown.sh
cp webapps/tast.war `date +oldwars/tast_%m%d%H%M.war`
rm -rf webapps/tast*
rm -rf work
sleep 5
cp src/TransAtlanticSlaveTrade/tast.war webapps
bin/startup.sh
sleep 9
bin/shutdown.sh
cp hibernate.properties webapps/tast/WEB-INF/classes
cp tast.properties webapps/tast/WEB-INF/classes
sleep 3
bin/startup.sh

