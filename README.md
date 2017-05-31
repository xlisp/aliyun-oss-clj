# aliyun-oss-clj: 阿里云Clojure SDK的封装

* 阿里云生成STS临时的id和key访问阿里云OSS

## Usage

* project.clj & youns.clj
```clojure
;; project.clj
[aliyun-oss-clj "0.1.0-SNAPSHOT"]
;; your ns
(ns test (:require [aliyun-oss-clj.core :as sts]))
(sts/assumeRole accessKeyId accessKeySecret roleArn roleSessionName policy protocolType)
```
* 生成STS临时的访问身份信息 
```clojure
aliyun-oss-clj.core=>  (assumeRole accessKeyId accessKeySecret roleArn roleSessionName policy protocolType)
```
* 将上一步生成STS临时的访问身份信息, 填入对应的oss的调用参数中 (Node的示例)
```node
var oss = require('ali-oss');
var co = require('co');
var store = oss({
    accessKeyId: 'STS.GkRjtzZEaaaaaaaaaaaaaa',  //临时STS生成的accessKeyId
    accessKeySecret: '5oqY7nPCUFWiYbbbbbbbbbbbbb',  //临时STS生成的accessKeySecret 
    bucket: 'stevechanoss',
    region: 'oss-cn-shanghai',
    stsToken: 'CAIShQJ1q6Ft5B2cccccccccccccccccc5R2WaeU2i6yIUo1h4Cuix1xtE5GA==' // 临时STS生成的 stsToken
});

co(function* () {
    var result = yield store.listBuckets();
    console.log(result);
    var result = yield store.listBuckets({
        prefix: 'prefix'
    });
    console.log(result);
}
  ).catch(function (err) {
      console.log(err);
  });

```

## License

Copyright © 2017 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
