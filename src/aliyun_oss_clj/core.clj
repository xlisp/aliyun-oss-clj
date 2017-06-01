(ns aliyun-oss-clj.core
  (:require [cheshire.core])
  (:import (com.aliyuncs DefaultAcsClient)
           (com.aliyuncs.profile DefaultProfile)
           (com.aliyuncs.sts.model.v20150401 AssumeRoleRequest AssumeRoleResponse)
           (com.aliyuncs.http MethodType ProtocolType)))

(set! *warn-on-reflection* true)

(defn create-client
  [{:keys [region access-key-id access-key-secret]}]
  (DefaultAcsClient.
    (DefaultProfile/getProfile region access-key-id access-key-secret)))

(defn get-assumed-role-creds
  [^DefaultAcsClient client
   {:keys [role-arn role-session-name policy-map
           sts-api-version method protocol]
    :or   {sts-api-version "2015-04-01"
           method          :post
           protocol        :https}}]
  (let [request (doto (AssumeRoleRequest.)
                  (.setVersion sts-api-version)
                  (.setMethod (case method
                                :get MethodType/GET
                                :post MethodType/POST))
                  (.setProtocol (case protocol
                                  :https ProtocolType/HTTPS
                                  :http ProtocolType/HTTP))
                  (.setRoleArn role-arn)
                  (.setRoleSessionName role-session-name)
                  (.setPolicy (cheshire.core/generate-string policy-map)))
        response (-> client
                     ^AssumeRoleResponse (.getAcsResponse request)
                     (.getCredentials))]
    {:expiration        (.getExpiration response)
     :access-key-id     (.getAccessKeyId response)
     :access-key-secret (.getAccessKeySecret response)
     :sts-token         (.getSecurityToken response)}))

(comment
  "Usage example"
  (let [client (create-client {:region            "cn-shanghai"
                               :access-key-id     "<access-key-id>"
                               :access-key-secret "<access-key-secret>"})]
    (get-assumed-role-creds
      client
      {:role-arn          "<role-arn>"
       :role-session-name "<role-session-name>"
       :policy-map        {:Version   "1"
                           :Statement [{:Action   ["oss:*"]
                                        :Resource ["acs:oss:*:*:*"]
                                        :Effect   "Allow"}]}})))