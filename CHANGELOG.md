# Changelog

All notable changes to this project will be documented in this file. See [standard-version](https://github.com/conventional-changelog/standard-version) for commit guidelines.

### [1.4.1-1](https://git.hyperchain.cn///compare/v1.4.0...v1.4.1-1) (2022-05-07)


### Bug Fixes

* **proof:** #QAGC-164, add sha1 hash function to support vidb proof ([f28011b](https://git.hyperchain.cn///commit/f28011b824b44b8b79679a052580b3ac5358b9b0)), closes [#QAGC-164](https://git.hyperchain.cn///issues/QAGC-164)
* **response:** #QAGC-163, add hasMore func for limitResponse and add invalidMsg for transaction response. ([3dfa854](https://git.hyperchain.cn///commit/3dfa854debb67be56d1fa74eb3d20bc02c8ba5d4)), closes [#QAGC-163](https://git.hyperchain.cn///issues/QAGC-163)

## [1.4.0](https://git.hyperchain.cn///compare/v1.4.0-3...v1.4.0) (2022-04-26)

## [1.4.0-3](https://git.hyperchain.cn///compare/v1.4.0-2...v1.4.0-3) (2022-04-06)


### Bug Fixes

* **transactionHash:** #QAGC-161, fix getTransaction method for hyperchain ([0870517](https://git.hyperchain.cn///commit/0870517bba5fa5960f10107b2db92ac9121cecc0))

## [1.4.0-2](https://git.hyperchain.cn///compare/v1.4.0-1...v1.4.0-2) (2022-03-29)


### Bug Fixes

* **snapshot:** change flato version snapshot() underlying call as archive_makeSnapshot4Flato ([c896e46](https://git.hyperchain.cn///commit/c896e465ccff00a09d3e2f3f7a5d6e0fc39ef6a3))

## [1.4.0-1](https://git.hyperchain.cn///compare/v1.3.0...v1.4.0-1) (2022-03-25)


### Features

* **hashChange:** suport hashChangeContract of bvm ([18ff8db](https://git.hyperchain.cn///commit/18ff8db2d168501b656c6d6322da370d0f8aa986))
* **proof:** add tx, account and state proof ([f332bce](https://git.hyperchain.cn///commit/f332bceb9720fb2e9e6359a0508e398e70f7cd93))


### Bug Fixes

* **Abi:** fix Abi's toJson method ([368961d](https://git.hyperchain.cn///commit/368961d6807428aa515b3e73b9c3670097f67ac6))
* **request:** fix NullPointEx for isUsedProvider method ([b3b6e73](https://git.hyperchain.cn///commit/b3b6e734abf061b379d7daab0281c6b86050f942))
* **TxService:** set ns for sendBatchTxs ([966868e](https://git.hyperchain.cn///commit/966868ef312d9f46eed93c78cabecf109c1235f8))

## [1.3.0](https://git.hyperchain.cn///compare/v1.3.0-3...v1.3.0) (2022-03-25)


### Bug Fixes

* **Abi:** fix Abi's toJson method ([470374e](https://git.hyperchain.cn///commit/470374ee299df5ce2c3696d265a71d898c395449))
* **doc:** fix tx and block interface doc ([677671e](https://git.hyperchain.cn///commit/677671e5f93bf6658a95281cd4867758f02cee58))
* **request:** fix NullPointEx for isUsedProvider method ([e0bed09](https://git.hyperchain.cn///commit/e0bed09bcf1b2a7c989dec2d17ae7e9f269a91c5))
* **TxService:** set ns for sendBatchTxs ([a352ad3](https://git.hyperchain.cn///commit/a352ad36edaa5266782b7fd97ab87cd2f68603d9))

## [1.3.0](https://git.hyperchain.cn///compare/v1.3.0-3...v1.3.0) (2022-03-25)


### Bug Fixes

* **Abi:** fix Abi's toJson method ([470374e](https://git.hyperchain.cn///commit/470374ee299df5ce2c3696d265a71d898c395449))
* **doc:** fix tx and block interface doc ([677671e](https://git.hyperchain.cn///commit/677671e5f93bf6658a95281cd4867758f02cee58))
* **request:** fix NullPointEx for isUsedProvider method ([e0bed09](https://git.hyperchain.cn///commit/e0bed09bcf1b2a7c989dec2d17ae7e9f269a91c5))
* **TxService:** set ns for sendBatchTxs ([a352ad3](https://git.hyperchain.cn///commit/a352ad36edaa5266782b7fd97ab87cd2f68603d9))

## [1.3.0-3](https://git.hyperchain.cn///compare/v1.2.1...v1.3.0-3) (2022-02-23)


### Bug Fixes

* **doc:** fix tx interface doc ([f2d9d61](https://git.hyperchain.cn///commit/f2d9d61f4f9d9087631b8cd7249359cd0171c894))

## [1.3.0-2](https://git.hyperchain.cn///compare/v1.3.0-1...v1.3.0-2) (2022-02-15)


### Features

* **bvm:** add cert and ca mode operation ([dc3adea](https://git.hyperchain.cn///commit/dc3adea9e8a08edab2a320b36306331de5f79d78))
* **docs:** add some missing interface doc ([f74c80c](https://git.hyperchain.cn///commit/f74c80c65e4e4051a5aab93aac4f7fc392ac7952))


### Bug Fixes

* **providerManager:** fix hand the exception of getTcert ([3d6f181](https://git.hyperchain.cn///commit/3d6f18101c81847ec8e4c9f1fcfb23ae6e3ad1d0))

## [1.3.0-1](https://git.hyperchain.cn///compare/v1.2.0...v1.3.0-1) (2022-01-26)


### Features

* support grpc mq ([3d58682](https://git.hyperchain.cn///commit/3d58682dba517de062e52db29f28d888f3d5a05e))
* **fvm:** after rebase ([19d786d](https://git.hyperchain.cn///commit/19d786d017c9f9189d39dce2056fe28aa5e2f396))
* **providerManager:** resend tx request to other node when the node can't handle tx ([8198fb5](https://git.hyperchain.cn///commit/8198fb5d2dfcdb86a852f03a1f20d357a261b867))


### Bug Fixes

* **providerManager:** fix logic of getTcert when set txversion ([58f9427](https://git.hyperchain.cn///commit/58f94278d15fad1c222c948430ae36ce37a6c895))

## [1.3.0-2](https://git.hyperchain.cn///compare/v1.3.0-1...v1.3.0-2) (2022-02-15)


### Features

* **bvm:** add cert and ca mode operation ([dc3adea](https://git.hyperchain.cn///commit/dc3adea9e8a08edab2a320b36306331de5f79d78))
* **docs:** add some missing interface doc ([f74c80c](https://git.hyperchain.cn///commit/f74c80c65e4e4051a5aab93aac4f7fc392ac7952))


### Bug Fixes

* **providerManager:** fix hand the exception of getTcert ([3d6f181](https://git.hyperchain.cn///commit/3d6f18101c81847ec8e4c9f1fcfb23ae6e3ad1d0))

## [1.2.0](https://git.hyperchain.cn///compare/v1.2.0-4...v1.2.0) (2022-01-21)

## [1.3.0-1](https://git.hyperchain.cn///compare/v1.2.0-4...v1.3.0-1) (2022-01-26)


### Features

* support grpc mq ([3d58682](https://git.hyperchain.cn///commit/3d58682dba517de062e52db29f28d888f3d5a05e))
* **fvm:** after rebase ([19d786d](https://git.hyperchain.cn///commit/19d786d017c9f9189d39dce2056fe28aa5e2f396))
* **providerManager:** resend tx request to other node when the node can't handle tx ([8198fb5](https://git.hyperchain.cn///commit/8198fb5d2dfcdb86a852f03a1f20d357a261b867))


### Bug Fixes

* **providerManager:** fix logic of getTcert when set txversion ([58f9427](https://git.hyperchain.cn///commit/58f94278d15fad1c222c948430ae36ce37a6c895))

## [1.2.0](https://git.hyperchain.cn///compare/v1.2.0-4...v1.2.0) (2022-01-21)

## [1.2.0-4](https://git.hyperchain.cn///compare/v1.2.0-3...v1.2.0-4) (2022-01-21)


### Features

* **transaction:** add check payload length ([946734d](https://git.hyperchain.cn///commit/946734d7d9a5c5a0a98826e57fe0d85032251264))


### Bug Fixes

* **TxVersion:** fix compare method for txversion ([2dc24ef](https://git.hyperchain.cn///commit/2dc24ef686f2192c1723c64106a2b849278fc7a1))
* **utils:** fix decode hvm payload, add check null for invoke directly params ([dfe49bf](https://git.hyperchain.cn///commit/dfe49bfe7631ca7daff74dd8062aa4958987f8ee))

## [1.2.0-3](https://git.hyperchain.cn///compare/v1.2.0-2...v1.2.0-3) (2022-01-20)


### Bug Fixes

* **TxVersion:** add txVersion 3.4 ([bf6a635](https://git.hyperchain.cn///commit/bf6a635ca38596044b97b14cf4a9e3ad5e3176a2))

## [1.2.0-2](https://git.hyperchain.cn///compare/v1.2.0-1...v1.2.0-2) (2022-01-20)


### Features

* update dependency version, rollback gm tcert logic ([d077994](https://git.hyperchain.cn///commit/d0779940d5838dedf47474597ad267b8e82d4589))
* **request:** resend tx if txversion is not equal with platform's ([251a829](https://git.hyperchain.cn///commit/251a829ef6bc8e2f9ff4dae2c26b70ba92f5ba24))
* **txverison:** refactor TxVersion to adapt the change of platform ([6993083](https://git.hyperchain.cn///commit/6993083934a71643dddfdf7b9666c70867f6f0da))

## [1.2.0-1](https://git.hyperchain.cn///compare/v1.1.1...v1.2.0-1) (2021-12-15)


### Features

* **mpc:** add mpc bvm interface ([fd37dc9](https://git.hyperchain.cn///commit/fd37dc914336ff65b95a671c55be276e9d1db2cf))


### Bug Fixes

* **pom.xml:** update bouncycastle to 1.67 ([e7e2ccb](https://git.hyperchain.cn///commit/e7e2ccbb1c12233f7d35b69b33bdc58f9736b054))

### [1.1.1](https://git.hyperchain.cn///compare/v1.1.0...v1.1.1) (2021-12-14)


### Bug Fixes

* **log&gmtls:** fix log4j-core version to 2.15.0 and remove gmtls ([f0dc987](https://git.hyperchain.cn///commit/f0dc987e1307c467d7c1d0684f92993778a7699e))

## [1.1.0](https://git.hyperchain.cn///compare/v1.1.0-6...v1.1.0) (2021-12-09)

## [1.1.0-6](https://git.hyperchain.cn///compare/v1.1.0-5...v1.1.0-6) (2021-12-09)

## [1.1.0-5](https://git.hyperchain.cn///compare/v1.1.0-4...v1.1.0-5) (2021-12-09)


### Features

* **txVersion:** add txVersin convert ([ae0c8d3](https://git.hyperchain.cn///commit/ae0c8d38282d1285d1c72bf91039fd206554f5c3))


### Bug Fixes

* **ArchiveResponse:** fix result of archiveResponse ([5f322f6](https://git.hyperchain.cn///commit/5f322f68952220681d2841f39cf5dfe3506bb4b5))

## [1.1.0-4](https://git.hyperchain.cn///compare/v1.1.0-3...v1.1.0-4) (2021-12-08)


### Bug Fixes

* **R1:** fix error when use fromAccountJson and verifySign methon for R1 account ([b38543f](https://git.hyperchain.cn///commit/b38543ff594f812f667dd1ed6667a7f65b0e47a0))

## [1.1.0-3](https://git.hyperchain.cn///compare/v1.1.0-2...v1.1.0-3) (2021-12-08)


### Features

* update test ([34f1299](https://git.hyperchain.cn///commit/34f12994405318a0cabe7887338975552d20f060))
* **bvm:** add setGenesisInfo and getGenesisInfo operation for hpc ([f00e9de](https://git.hyperchain.cn///commit/f00e9de1a83bd17fef6836c02d2a16a1cf35e928))
* **service:** add missing interface ([4d8ba4e](https://git.hyperchain.cn///commit/4d8ba4e70deec447dbfeced31bdfb1ab40824bfc))


### Bug Fixes

* **AccountService:** fix R1 account ([7a5d926](https://git.hyperchain.cn///commit/7a5d92644ee40acf3bb80f7add9a4d4f78a6d311))
* **invalidtx:** fix #QAGC-158, fix wrong txResponse of getInvalidTransactionsByBlockNumber ([b904a69](https://git.hyperchain.cn///commit/b904a69ab0c3ffcafafaffab5ba58d8cf5f89735)), closes [#QAGC-158](https://git.hyperchain.cn///issues/QAGC-158) [#QAGC-158](https://git.hyperchain.cn///issues/QAGC-158)
* **provider:** fix log level of grpc stream and add setconnectime for grpc provider ([2d67b65](https://git.hyperchain.cn///commit/2d67b651273989d6d0e681972d959558ed4a5337))
* **request:** fix throw exception while send request ([9b00188](https://git.hyperchain.cn///commit/9b00188f7cb4307d6e4fddd65cf2251c33b55c61))

## [1.1.0-2](https://git.hyperchain.cn///compare/v1.1.0-1...v1.1.0-2) (2021-11-29)


### Features

* support grpc service ([c296ca9](https://git.hyperchain.cn///commit/c296ca9e1293c2f2103a99c15e684485fcbec590))

## [1.1.0-1](https://git.hyperchain.cn///compare/v1.0.8...v1.1.0-1) (2021-11-26)


### Features

* add a new bean-abi invoke type to invoke hvm contract ([f73dc23](https://git.hyperchain.cn///commit/f73dc2371574d9faad669deef6474e5ad9ecc495))
* **decode:** decode proposal ([d1380d3](https://git.hyperchain.cn///commit/d1380d374f1128d0ebc625a2198d25b918c7a049))
* **doc:** update deprecated interface ([357b315](https://git.hyperchain.cn///commit/357b315ecf9cd3b30f6205f8d9fd950b0b41eb68))
* **receipt:** add get confirmed receipt interface ([291028c](https://git.hyperchain.cn///commit/291028c0908117cf8873ac6dec7bed0819c8b162))
* **set:** sort set ([37e253c](https://git.hyperchain.cn///commit/37e253c4d093a4d906196fdf8f50e12e41cf96f1))


### Bug Fixes

* **resendTx:** fix resend deploy or update hvm contract tx ([e0156c9](https://git.hyperchain.cn///commit/e0156c967d9ade68ddf954ba050f8e4eb5b04b26))
* **transaction:** fix getTransactionHash when vmtype is kvsql or bvm ([65a8595](https://git.hyperchain.cn///commit/65a85951961aca03ef2b5f0b71c5470f6a838d3c))
* **Transaction:** set transfer vm type for transfer transaction ([f65d35a](https://git.hyperchain.cn///commit/f65d35a4c1cc49968558d96283f1338041dac66b))

### [1.0.8](https://git.hyperchain.cn///compare/v1.0.8-5...v1.0.8) (2021-09-24)

### [1.0.8-5](https://git.hyperchain.cn///compare/v1.0.8-4...v1.0.8-5) (2021-09-23)


### Bug Fixes

* **hvm:** add magic when encode contract jar ([33086cf](https://git.hyperchain.cn///commit/33086cf5e2ae5d7e8563bedc060ffab7583c914a))

### [1.0.8-4](https://git.hyperchain.cn///compare/v1.0.8-3...v1.0.8-4) (2021-09-14)


### Features

* encode jar in sdk and add txVersion 3.0 ([f0be882](https://git.hyperchain.cn///commit/f0be88234aeb41bfbc01ab0c7b94ea91befabd8a))
* **did:** add did account getter ([e1605f7](https://git.hyperchain.cn///commit/e1605f766b9000c85704f7ab76e5dc842461e2e0))
* **proposal:** add a function to decode MQLog.Data to proposal ([fb536c6](https://git.hyperchain.cn///commit/fb536c6acaf3964265200966f42e0ffcd20dc28c))

### [1.0.8-3](https://git.hyperchain.cn///compare/v1.0.8-2...v1.0.8-3) (2021-09-10)


### Features

* **txVersion:** add txVersin 3.0 ([28de7b0](https://git.hyperchain.cn///commit/28de7b0c1938e454b8d5aca3fc4875dc67faed52))

### [1.0.8-2](https://git.hyperchain.cn///compare/v1.0.7...v1.0.8-2) (2021-09-01)


### Bug Fixes

* **log:** chang log4j 1.2.17 to org.apache.logging.log4j:log4j-core 2.14.1 ([93b6ca1](https://git.hyperchain.cn///commit/93b6ca10e5835160a255290fe47a1640295a4118))
* **sql:** add sql simulate ([3400ec0](https://git.hyperchain.cn///commit/3400ec05f6af1bf498420fd1e2e2df608bd0713c))
* **sql:** fix request param ([60158dc](https://git.hyperchain.cn///commit/60158dcc232a446e1b34da7fb57a0d58c0243565))
* **transaction:** fix getTransactionHash when extraID is not null ([c9e2c10](https://git.hyperchain.cn///commit/c9e2c1060b86bd1abc142a559efa1dc36eb24eaf))

### [1.0.7-1](https://git.hyperchain.cn///compare/v1.0.6...v1.0.7-1) (2021-08-06)


### Features

* add sql service ([60516c4](https://git.hyperchain.cn///commit/60516c4a8d21d8a3eeb3af23f12923f6bc36b17e))


### Bug Fixes

* **archive:** fix queryArchive, archiveNoPredict interface ([36dec35](https://git.hyperchain.cn///commit/36dec35e41c9705d814b04f39e74f7999614b294))

### [1.0.7](https://git.hyperchain.cn///compare/v1.0.6...v1.0.7) (2021-08-23)


### Features

* resend tran if the error is txversion not equal platform's ([c9a898a](https://git.hyperchain.cn///commit/c9a898af58b223db0027f741b27414eb8f9952f2))


### Bug Fixes

* **transaction:** fix getTransactionHash when extraID is not null ([2a59315](https://git.hyperchain.cn///commit/2a59315b2bb43926649c3e120c266d38b69e4086))

### [1.0.6](https://git.hyperchain.cn///compare/v1.0.6-2...v1.0.6) (2021-08-06)

### [1.0.6-2](https://git.hyperchain.cn///compare/v1.0.6-1...v1.0.6-2) (2021-08-05)


### Bug Fixes

* **response:** fix toString func of response ([b5878bb](https://git.hyperchain.cn///commit/b5878bb8b1266559b1db6f24ad52d810b5c65eb2))

### [1.0.6-1](https://git.hyperchain.cn///compare/v1.0.5...v1.0.6-1) (2021-08-04)


### Features

* **docs:** add some information to did doc ([ed1f03d](https://git.hyperchain.cn///commit/ed1f03d961d7564b607cfff7afae9111ab1a27ff))
* **gas:** add special gas receipt interface ([e37922e](https://git.hyperchain.cn///commit/e37922ecd4fe82fab594b854b4248a80682bd6a6))
* **prepare_node:** change prepare_nodes.sh ([779c3ce](https://git.hyperchain.cn///commit/779c3ce2f1caf1d516b899c475c1f36a0614eb5e))


### Bug Fixes

* **archive:** #QAGC-153, change the doc and response of archive service ([12f213d](https://git.hyperchain.cn///commit/12f213db2cd9e392d2b160252e80987a6e9e8d77)), closes [#QAGC-153](https://git.hyperchain.cn///issues/QAGC-153)
* **fallback:** #QAGC-151, fix fallback null ([3481a0d](https://git.hyperchain.cn///commit/3481a0d4a155c7cb82de6612d766d3a89748e255))
* **response:** fix toString func of DID service response ([d3446f7](https://git.hyperchain.cn///commit/d3446f75f2bc7bb46b8f312466d4b0fb69953fd6))

### [1.0.5](https://git.hyperchain.cn///compare/v1.0.5-2...v1.0.5) (2021-06-21)


### Features

* **mq:** update mq doc ([3a18286](https://git.hyperchain.cn///commit/3a18286d0ebf90bd7a408948c80f2430a580a8e2))


### Bug Fixes

* **docs:** add simulate transaction doc ([5e8226f](https://git.hyperchain.cn///commit/5e8226ffa8d6cceca6d5dcf96df96246566286da))
* **docs:** fix litesdk_document ([eeb67fd](https://git.hyperchain.cn///commit/eeb67fd8f45a62b533647fb763a9d9aee56124a0))

### [1.0.5-2](https://git.hyperchain.cn///compare/v1.0.5-1...v1.0.5-2) (2021-06-10)


### Features

* **certRevoke:** #flato-3027, #flato-3319, support cert revoke and freeze ([96fcabf](https://git.hyperchain.cn///commit/96fcabfe997c5cbfd1097dee5c9224cc591d88e7)), closes [#flato-3027](https://git.hyperchain.cn///issues/flato-3027) [#flato-3319](https://git.hyperchain.cn///issues/flato-3319)
* **r1Account:** support r1 account ([accf4b1](https://git.hyperchain.cn///commit/accf4b104a0c9a628900aa413d87370e6c0c28b0))


### Bug Fixes

* **DIDTest:** fix the did service test ([ff9d2ad](https://git.hyperchain.cn///commit/ff9d2adea3e316419aa89fd6047166cf4a44b05d))
* **getTxByHash:** #QAGC-143, fix getTxByHash can't assign node ([6f17cdf](https://git.hyperchain.cn///commit/6f17cdf9ac9e3aa3a299aea5acbbbc8dbb553fb1))
