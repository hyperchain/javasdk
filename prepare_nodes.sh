#!/bin/bash

if [[ $1 == *"-h"* ]]; then
  echo "功能 : 拉取文件系统的 区块链包，复制到testEnv,修改成4节点配置后，启动"
  echo "step 0 根据输入的参数 选择拉取的 区块链平台包： ex: hyperchain  或flato"
  echo " step 1: 拉取文件系统上的 包： http://nexus.hyperchain.cn/repository/hyper-test/binary/"
  echo "step 3: 根据区块链平台 修改配置, 将修改配置后的包复制到testEnv里"
  echo " ==============================================================="
  echo "======================使用======================================="
  echo "sh prepare_nodes.sh blockchain branch 注： branch 如果匹配不到 默认拉smoke的包"
  echo "举例说明: "
  echo "      sh prepare_nodes.sh flato  拉取 flato00.tar.gz的包(branch 不传默认为tag)"
  echo "      sh prepare_nodes.sh flato tag/v1.0.2 拉取 flato00-coverage.tar.gz的包（发版后触发）"
  echo "      sh prepare_nodes.sh flato tag 拉取 flato00.tar.gz的包"
  echo "      sh prepare_nodes.sh flato pre-release 拉取 flato00-release的包"
  echo "      sh prepare_nodes.sh flato smoke 拉取flato00-smoke的包  (如果匹配不到 默认拉smoke的包)"
  echo "      sh prepare_nodes.sh flato smoke true 拉取本地文件"
  echo "      sh prepare_nodes.sh flato smoke false somoke-package-name 拉取冒烟包"
  exit
fi

blockchain=${1}
BRANCH=${2:-tag}
echo $BRANCH
NEW_HYPERCHAIN=$1

BIN_IN_LOCAL=${3:-false}

echo "bin :"$BIN_IN_LOCAL
nodeN=4

BLOCKCHAIN=${NEW_HYPERCHAIN:-hyperchain}
gitlabSourceBranch=$4

if [[ $BRANCH == "coverage" ]]; then
  BRANCH="coverage"
elif [[ $BRANCH == *"tag"* ]]; then
  BRANCH="tag"
elif [[ $BRANCH == *"pre-release"* ]]; then
  BRANCH="release"
else
  BRANCH="smoke"
fi
package=""
echo "Get flato0.0"
if [[ $BRANCH == "coverage" ]]; then
  package="-coverage"
  nohup goc server >/tmp/goc.log 2>&1 &
elif [[ $BRANCH == "tag" ]]; then
  package=""
elif [[ $BRANCH == "release" ]]; then
  smoke_package="-release"
else
  smoke_package=${gitlabSourceBranch##*/}
  if [[ ! -n "$BRANCH" ]]; then
    echo "smoke package stay old way"
    package=-smoke
  else
    package=-${smoke_package}-test-smoke
    echo "package is :"$smoke_package
    if [[ $smoke_package == "" ]]; then
      echo "not  special flato version ;to get flato00.tar"
      package=""
    fi
  fi
fi

function prepare_flato() {

  pkill flato
  pkill cedar
  pkill hyperchain
  jps | grep JceeServer | awk '{print $1}' | xargs kill -9

  echo "prepare ${blockchain}"
  rm -rf ${WORKSPACE}/${blockchain}0.0
  rm -rf ${WORKSPACE}/certs
  mkdir -p ${WORKSPACE}/certs
  mkdir -p ${WORKSPACE}/testEnv/0.0/node1
  mkdir -p ${WORKSPACE}/testEnv/0.0/node2
  mkdir -p ${WORKSPACE}/testEnv/0.0/node3
  mkdir -p ${WORKSPACE}/testEnv/0.0/node4
  if [ ${BIN_IN_LOCAL} == "false" ]; then
    curl -O http://nexus.hyperchain.cn/repository/hyper-test/binary/${blockchain}/Centos7/${blockchain}00${package}.tar.gz
    tar zxvf ${blockchain}00${package}.tar.gz
  fi

  curl -O http://nexus.hyperchain.cn/repository/hyper-test/certs/certs.tar.gz
  tar zxvf certs.tar.gz
  rm ${blockchain}00${package}.tar.gz
  rm certs.tar.gz
  testPath="${WORKSPACE}/testEnv/0.0"
  blockchainPath="${WORKSPACE}/${blockchain}0.0"
  certsPath="${WORKSPACE}/certs"

  cd $testPath
  # Make dir node1-$nodeN
  for ((i = 1; i <= $nodeN; i++)); do
    if [ -d "node$i" ]; then
      rm -rf node$i
    fi
    mkdir node$i
  done

  # config nodes1-$nodeN
  for ((i = 1; i <= $nodeN; i++)); do
    cd $testPath/node$i/
    cp -rf ${blockchainPath}/* .
    #copy certs
    rm -rf namespaces/global/certs/CA
    rm -rf namespaces/global/certs/certs
    cp -r ${certsPath}/node${i}/* namespaces/global/certs/

    cd configuration

    # modify ports on dynamical.toml
    sed -ig "s/= 8081/= 808$i/" dynamic.toml
    sed -ig "s/= 9001/= 900$i/" dynamic.toml
    sed -ig "s/= 10001/= 1000$i/" dynamic.toml
    sed -ig "s/= 50081/= 5008$i/" dynamic.toml
    sed -ig "s/= 50051/= 5005$i/" dynamic.toml
    sed -ig "s/= 50011/= 5001$i/" dynamic.toml
    sed -ig "s/max_content_length = \"100kb\"/max_content_length = \"2mb\"/" global.toml
    sed -ig "s/maxSendMessageSize = \"50mb\"/maxSendMessageSize = \"5000mb\"/" global.toml
    sed -ig "s/maxRecvMessageSize = \"50mb\"/maxRecvMessageSize = \"5000mb\"/" global.toml
    # modify addrs on dynamic.toml
    sed -ig "/self/s/node1/node$i/g" dynamic.toml
    sed -ig "s/domain=\"domain1\"/domain=\"domain$i\"/" dynamic.toml
    sed -ig "/domain.*127/s/50011/5001$i/g" dynamic.toml

    # modify ports on debug.toml
    sed -ig "s/10091/1009$i/" debug.toml
    sed -ig "s/10051/1005$i/" debug.toml

    # modify nodes on ns_dynamic.toml
    cd global
    sed -ig "s/hostname    = \"node1\"   #/hostname    = \"node$i\"   #/" ns_dynamic.toml
    sed -ig "/^.*certs\/distributed/c ecert  = \"certs/certs\"" ns_static.toml

  done
  echo "src path : ${blockchainPath}"
  echo "dec path : ${testPath}"
  echo "Start the platform by executing the following command:"
  echo `ps -ef|grep goc\ server|grep -v grep`
  startNodes ${blockchain}
}

function startNodes() {
  cd ${testPath}
  for ((i = 1; i <= 4; i++)); do
    cd node${i}
    chmod +x ${1}
    CommitNum=$(./${1} --codeVersion)
    echo $CommitNum
    ./${1} > node${i}.log 2>&1 &
    cd ..
  done
  for ((j = 1; j <= 120; j++)); do
    sleep 1
    result=$(curl localhost:8081 --data '{"jsonrpc":"2.0","method": "node_getNodeStates", "namespace": "global", "id": 1}')
    if [[ "$result" =~ "NORMAL" ]]; then
      echo 'Nodes started successfully!'
      echo "Spent ${j} seconds"
      break
    else
      if [[ "${j}" != 120 ]]; then
        echo "Nodes not started"
      else
        echo "Nodes startup failed within 120 seconds"
        exit 1
      fi
    fi
  done
#  echo "checkout nodes status again :"
#  echo "curl localhost:8081 --data '{\"jsonrpc\":\"2.0\",\"method\": \"node_getNodeStates\", \"namespace\": \"global\", \"id\": 1}'"
#  echo "cat nodes logs :"
#  echo "tail -f ${testPath}/node1/node1.log"
#  echo "tail -f ${testPath}/node2/node2.log"
#  echo "tail -f ${testPath}/node3/node3.log"
#  echo "tail -f ${testPath}/node4/node4.log"
}


case ${blockchain} in
"flato")

  prepare_flato flato
  ;;

"hyperchain")
  echo "hyperchain"
  prepare_hyperchain hyperchain
  ;;

"cedar")
  echo "cedar"
  prepare_flato cedar
  ;;
"help")
  echo "sh prepare_nodes.sh cedar --prepare cedar env"
  echo ""
  echo "sh prepare_nodes.sh hyperchian -- prepare hyperchain env"
  echo ""
  echo "sh prepare_nodes.sh flato --prepare flato env"
  ;;
*)
  echo "cat script methods  by executing the following command:"
  echo "sh prepare_nodes.sh help"
  ;;

esac

