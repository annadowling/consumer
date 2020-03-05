JAVA_OPTS="$JAVA_OPTS -Xms1G -Xmx4G -XX:+UseG1GC"
#JAVA_OPTS="$JAVA_OPTS -Xms1G -Xmx4G -XX:+UnlockExperimentalVMOptions -XX:+UseEpsilonGC"
export JAVA_OPTS

java $JAVA_OPTS -jar consumer.jar &