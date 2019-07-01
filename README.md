# LITESDK

[![License: LGPL v3](https://img.shields.io/badge/License-LGPL%20v3-blue.svg)](https://www.gnu.org/licenses/lgpl-3.0)
[![Build Status](https://travis-ci.org/hyperchain/javasdk.svg?branch=master)](https://travis-ci.org/hyperchain/javasdk)
[![Coverage Status](https://coveralls.io/repos/github/hyperchain/javasdk/badge.svg?branch=master)](https://coveralls.io/github/hyperchain/javasdk?branch=master)

This is a light JavaSDK for [Hyperchain](http://www.hyperchain.cn).

To get more information you can view [docs](http://docs.hyperchain.cn), use `hvmd` and `liteSDK` to start a enjoyable journey.

## Get started

### install

maven

```
<dependency>
    <groupId>cn.hyperchain</groupId>
    <artifactId>litesdk</artifactId>
    <version>0.0.2</version>
</dependency>
```

gradle

```
compile group: 'cn.hyperchain', name: 'litesdk', version: '0.0.2'
```

### usage

#### 1. build provider manager

Provider can be expanded by user, we support default http provider.

Then create provider manager.

```java
String DEFAULT_URL = "localhost:8081";
DefaultHttpProvider defaultHttpProvider = new DefaultHttpProvider.Builder().setUrl(DEFAULT_URL).build();
ProviderManager providerManager = ProviderManager.createManager(defaultHttpProvider);
```

#### 2. build service

Create different services by specific provider manager, service will provide some function.

```java
ContractService contractService = ServiceManager.getContractService(providerManager);
AccountService accountService = ServiceManager.getAccountService(providerManager);
```

#### 3. create account

Account can be used to sign transaction.

```java
Account account = accountService.genAccount(Algo.SMRAW);
```

#### 4. build transaction

Transaction builder can create different transaction by different style of initialization.

```java
// deploy
Transaction transaction = new Transaction.HVMBuilder(account.getAddress()).deploy("hvm-jar/hvmbasic-1.0.0-student.jar").build();
transaction.sign(account);

// invoke
Transaction transaction1 = new Transaction.HVMBuilder(account.getAddress()).invoke(receiptResponse.getContractAddress(), new StudentInvoke()).build();
```

#### 5. get response

Service will return a Request, user can use Request to get specific Response by interface.

```java
ReceiptResponse receiptResponse = contractService.deploy(transaction).send().polling();
```

#### 6. decode result

Decode result to specific type.

```java
Decoder.decodeHVM(receiptResponse1.getRet(), String.class);
```

## issue

If you have any suggestions or idea, please submit issue in this project!

## doc
If you want to know more about LiteSDK, you can read manual at [here](doc/hyperchain_litesdk_document.md).