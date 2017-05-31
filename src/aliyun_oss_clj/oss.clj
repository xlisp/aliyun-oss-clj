(ns aliyun-oss-clj.oss
  (:import
   [java.io
    BufferedReader
    ByteArrayInputStream
    File
    InputStream
    InputStreamReader]
   [java.util.List]
   [java.net.URLClassLoader]
   [com.aliyun.oss
    ClientException
    OSSClient
    OSSException]
   [com.aliyun.oss.model
    BucketInfo
    OSSObject
    OSSObjectSummary
    ObjectListing]))

(def config
  (:aliyun-oss-config (read-string (slurp "config/config.clj"))))

(def endpoint (:endpoint config))
(def accessKeyId (:accessKeyId config))
(def accessKeySecret (:accessKeySecret config))
(def bucketName (:bucketName config))
(def firstKey (:firstKey config))

(def ossClient (OSSClient. endpoint accessKeyId accessKeySecret))

(defn have-bucket-name?
  "判断是否含有某个bucketName"
  [bucket-name]
  (.doesBucketExist ossClient bucket-name))

;; (get-bucket-info bucketName) ;;=> {:location "oss-cn-shanghai", :creation-date #inst "2017-05-31T06:54:01.000-00:00", :owner...}
(defn get-bucket-info
  [bucket-name]
  (let [info (.getBucket (.getBucketInfo ossClient bucket-name))]
    {:location (.getLocation info)
     :creation-date (.getCreationDate info)
     :owner (.getOwner info)}
    )
  )

;; (upload-file bucketName "README.md")
(defn upload-file
  [bucket-name file-name]
  (.putObject ossClient bucket-name file-name (File. file-name))
  )
