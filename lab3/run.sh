#!/bin/bash

echo "Por favor, ingrese el numero de workers para spark:"
read workers 

echo "SPARK_WORKER_CORES=1
SPARK_WORKER_INSTANCES=$workers
SPARK_WORKER_MEMORY=4g" > "$SPARK_HOME/conf/spark-env.sh"

if [ "$SPARK_MASTER_PORT" = "" ]; then
  SPARK_MASTER_PORT=7077
fi

if [ "$SPARK_MASTER_HOST" = "" ]; then
  case `uname` in
      (SunOS)
          SPARK_MASTER_HOST="`/usr/sbin/check-hostname | awk '{print $NF}'`"
          ;;
      (*)
          SPARK_MASTER_HOST="`hostname -f`"
          ;;
  esac
fi

if [ "$SPARK_MASTER_WEBUI_PORT" = "" ]; then
  SPARK_MASTER_WEBUI_PORT=8080
fi

SPARK_HOSTPORT="spark://$SPARK_MASTER_HOST:$SPARK_MASTER_PORT"
echo $SPARK_HOSTPORT

$SPARK_HOME/sbin/stop-worker.sh
$SPARK_HOME/sbin/stop-master.sh

$SPARK_HOME/sbin/start-master.sh
$SPARK_HOME/sbin/start-worker.sh $SPARK_HOSTPORT

$SPARK_HOME/bin/spark-submit --master $SPARK_HOSTPORT target/group_37_lab3_2024-1.0-SNAPSHOT-jar-with-dependencies.jar -- "$@" 2>/dev/null 

$SPARK_HOME/sbin/stop-worker.sh
$SPARK_HOME/sbin/stop-master.sh
