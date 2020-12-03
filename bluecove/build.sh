#!/bin/sh

TAR_FILE=bluecove-gpl-2.1.0-sources.tar.gz
JAR_FILE=bluecove-2.1.0.jar
NEW_JAR_FILE=bluecove-2.1.0-SNAPSHOT.jar
USER=$(whoami)

if [ ${USER} != "root" ]; then
	echo "Must be ran as root"
	exit
fi

if [ ! -f "$(pwd)/build.sh" ]; then
	echo "Script must be present in current working directory"
	exit
fi

echo "*********************************************************************************"
echo "WARNING: This will uninstall openjdk-11-jdk and install openjdk-8-jdk instead"
echo "*********************************************************************************"

echo "Press any key to continue, or CTRL-C to cancel"
read A

apt-get update && apt-get -y remove openjdk-11-* && apt-get -y install openjdk-8-jdk ant bluetooth libbluetooth-dev
tar xzvf ${TAR_FILE}
mkdir -p bluecove/target
cp ${JAR_FILE} bluecove/target/${NEW_JAR_FILE}
cd bluecove-gpl-2.1.0 && ant all && cp target/*.so /usr/lib && ldconfig
hciconfig hci0 piscan
