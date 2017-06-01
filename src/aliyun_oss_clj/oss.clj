(ns aliyun-oss-clj.oss
  (:require [clojure.java.io :as io]
            [clojure.string :as str])
  (:import (com.aliyun.oss OSSClient)
           (com.aliyun.oss.model PutObjectResult)
           (java.io File InputStream)))

(set! *warn-on-reflection* true)

(defn create-client
  [{:keys [^String endpoint
           ^String access-key-id
           ^String access-key-secret
           ^String sts-token]}]
  (if sts-token
    (OSSClient. endpoint access-key-id access-key-secret sts-token)
    (OSSClient. endpoint access-key-id access-key-secret)))

(defn has-bucket-name?
  [^OSSClient client ^String bucket-name]
  (.doesBucketExist client bucket-name))

(defn get-bucket-info
  [^OSSClient client ^String bucket-name]
  (let [info (.getBucket (.getBucketInfo client bucket-name))]
    {:location      (.getLocation info)
     :creation-date (.getCreationDate info)
     :owner         (let [owner (.getOwner info)]
                      {:name (.getDisplayName owner)
                       :id   (.getId owner)})}))

(defn upload-file
  [^OSSClient client ^String bucket-name ^String key content]
  {:etag (.getETag ^PutObjectResult
                   (.putObject client
                               bucket-name
                               key
                               ^InputStream (io/input-stream content)))})

(comment
  (let [client (create-client {:endpoint          "http://oss-cn-shanghai.aliyuncs.com"
                               :access-key-id     "<access-key-id>"
                               :access-key-secret "<access-key-secret>"
                               :sts-token         "<sts-token>"})
        bucket-name "<bucket-name>"]
    [(has-bucket-name? client bucket-name)
     (get-bucket-info client bucket-name)
     (upload-file client bucket-name "test1.txt" (.getBytes "hello, world"))]))