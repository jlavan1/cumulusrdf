#!/bin/bash

SAVEDIR=$PWD
WORKDIR=$( cd $(dirname $0) ; pwd -P)
cd $WORKDIR

. /home/yzyan/rdf/cumulus/cumulusrdf/tools/nodes.conf

i=0
len=${#allnodes[@]}
echo $len

nmon="UpdateMatrix"
nmondir=$nmon$1$2

echo $nmondir

rm -rf $nmondir
mkdir $nmondir

while [ $i -lt $len ]
do
    myip=${allnodes[$i]}

    	ssh $myip "rm -rf /home/yzyan/alllogs/$nmondir"
    	ssh $myip "mkdir /home/yzyan/alllogs/$nmondir"
	
	ssh $myip "nmon -F /home/yzyan/alllogs/$nmondir/log$i.nmon -s1 -c360000"
   	let i++
done

java -Dlog.dir=/home/yzyan/rdf/cumulus/cumulusrdf/logs -cp ../cumulusrdf-standalone/target/cumulusrdf-standalone-1.1.0-SNAPSHOT.jar:../cumulusrdf-core/target/cumulusrdf-core-1.1.0-SNAPSHOT.jar:../cumulusrdf-framework/target/cumulusrdf-framework-1.1.0-SNAPSHOT.jar:../cumulusrdf-web-module/target/cumulusrdf-web-module-1.1.0-SNAPSHOT/WEB-INF/lib/* edu.kit.aifb.cumulus.cli.UpdateMatrix $1 $2


i=0;
while [ $i -lt $len ]
do
	myip=${allnodes[$i]}
	ssh $myip "killall -9 nmon"
	scp -r $myip:/home/yzyan/alllogs/$nmondir/*.nmon ./$nmondir/
	let i++
done

cd $SAVEDIR
