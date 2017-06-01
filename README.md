# aliyun-oss-clj: 阿里云OSS的Clojure SDK封装

* 阿里云生成STS临时的id和key访问阿里云OSS

## Usage

* project.clj & youns.clj
```clojure
;; project.clj
[aliyun-oss-clj "0.1.1-SNAPSHOT"]
;; your ns
(ns test (:require [aliyun-oss-clj.core :as sts]))
(sts/assumeRole accessKeyId accessKeySecret roleArn roleSessionName policy protocolType)
```
* 生成STS临时的访问身份信息 
```clojure
aliyun-oss-clj.core=>  (assumeRole accessKeyId accessKeySecret roleArn roleSessionName policy protocolType)
```
* 将上一步生成STS临时的访问身份信息, 填入对应的oss的调用参数中 (Clojure的示例)

```clojure

aliyun-oss-clj.core=> (in-ns 'aliyun-oss-clj.oss)
#object[clojure.lang.Namespace 0x3d2ac063 "aliyun-oss-clj.oss"]

aliyun-oss-clj.oss=> oss-client
#object[aliyun_oss_clj.oss$oss_client 0x4c31c14a "aliyun_oss_clj.oss$oss_client@4c31c14a"]

aliyun-oss-clj.oss=> (def ossClient (oss-client endpoint "STS.GkRjtzZEaaaaaaaaaaaaaa" "5oqY7nPCUFWiYbbbbbbbbbbbbb" "CAIShQJ1q6Ft5B2cccccccccccccccccc5R2WaeU2i6yIUo1h4Cuix1xtE5GA=="))
#'aliyun-oss-clj.oss/ossClient

aliyun-oss-clj.oss=> (have-bucket-name? ossClient bucketName)
true

aliyun-oss-clj.oss=> (get-bucket-info ossClient bucketName)
{:location "oss-cn-shanghai", :creation-date #inst "2017-05-31T06:54:01.000-00:00", :owner #object[com.aliyun.oss.model.Owner 0x9805249 "Owner [name=1056988755051517,id=1056988755051517]"]}

aliyun-oss-clj.oss=> (upload-file ossClient bucketName "project.clj")
#object[com.aliyun.oss.model.PutObjectResult 0x9caac8c "com.aliyun.oss.model.PutObjectResult@9caac8c"]

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
* 将上一步生成STS临时的访问身份信息, 填入对应的oss的调用参数中 (js网页的示例)

```html


<h1> JavaScript-SDK 快速开始-浏览器 </h1>
<script src="http://gosspublic.alicdn.com/aliyun-oss-sdk-4.4.4.min.js"></script>

<script type="text/javascript">
  var client = new OSS.Wrapper({
    region: 'oss-cn-shanghai',
    accessKeyId: 'STS.GkRjtzZEaaaaaaaaaaaaaa',
    accessKeySecret: '5oqY7nPCUFWiYbbbbbbbbbbbbb',
    stsToken: 'CAIShQJ1q6Ft5B2cccccccccccccccccc5R2WaeU2i6yIUo1h4Cuix1xtE5GA==',
    bucket: 'stevechanoss'
  });
  client.list({
    'max-keys': 10
  }).then(function (result) {
    console.log(result);
  }).catch(function (err) {
    console.log(err);
  });
</script>

```
*网页调用需要先到阿里云的OSS控制台设置: 允许跨域设置*
![](./js跨域设置.jpeg)
网页调用效果:
![](./js调用效果.jpeg)

## License

Copyright © 2017 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
