(ns aliyun-oss-clj.core
  (:import
   [com.aliyuncs DefaultAcsClient]
   [com.aliyuncs.exceptions.ClientException]
   [com.aliyuncs.http MethodType ProtocolType]
   [com.aliyuncs.profile DefaultProfile IClientProfile]
   [com.aliyuncs.sts.model.v20150401 AssumeRoleRequest AssumeRoleResponse]))

(def config
  (:aliyun-oss-ram-config
   (read-string (slurp "config/config.clj"))))

(def REGION_CN_HANGZHOU (:region config))
(def STS_API_VERSION "2015-04-01")
(def accessKeyId (:accessKeyId config))
(def accessKeySecret (:accessKeySecret config))
(def roleArn (:rolearn config))
(def roleSessionName (:roleSessionName config))
(def protocolType ProtocolType/HTTPS)
(def policy (:policy config))

;;(assumeRole accessKeyId accessKeySecret roleArn roleSessionName policy protocolType)
;;=> #object[com.aliyuncs.sts.model.v20150401.AssumeRoleResponse 0x51902f96 "com.aliyuncs.sts.model.v20150401.AssumeRoleResponse@51902f96"]
(defn assumeRole
  [^String accessKeyId ^String accessKeySecret
   ^String roleArn ^String roleSessionName ^String policy
   ^ProtocolType protocolType]
  (let [profile (DefaultProfile/getProfile REGION_CN_HANGZHOU accessKeyId accessKeySecret)
        client (DefaultAcsClient. profile)
        request (AssumeRoleRequest.)
        _ (.setVersion request STS_API_VERSION)
        _ (.setMethod request MethodType/POST)
        _ (.setProtocol request protocolType)
        _ (.setRoleArn request roleArn)
        _ (.setRoleSessionName request roleSessionName)
        _ (.setPolicy request policy)]
    (.getAcsResponse client request)
    )
  )


