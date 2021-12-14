# Changelog

All notable changes to this project will be documented in this file. See [standard-version](https://github.com/conventional-changelog/standard-version) for commit guidelines.

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
