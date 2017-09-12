(defproject emp "0.0.1-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [trptcolin/versioneer "0.2.0"]
                 [io.pedestal/pedestal.service "0.5.2"]
                 [io.pedestal/pedestal.jetty "0.5.2"]
                 [ch.qos.logback/logback-classic "1.2.3"
                  :exclusions [org.slf4j/slf4j-api]]
                 [org.slf4j/jul-to-slf4j "1.7.25"]
                 [org.slf4j/jcl-over-slf4j "1.7.25"]
                 [org.slf4j/log4j-over-slf4j "1.7.25"]
                 [danlentz/clj-uuid "0.1.7"]
                 [environ "1.1.0"]
                 [clj-pdf "2.2.29"]]

  :min-lein-version "2.0.0"
  :pedantic? :abort

  :resource-paths ["config", "resources"]

  :plugins [[info.sunng/lein-bootclasspath-deps "0.2.0"]
            [lein-environ "1.1.0"]
            [com.palletops/uberimage "0.4.1"
             :exclusions [org.clojure/clojure
                          com.fasterxml.jackson.core/jackson-annotations
                          commons-logging
                          com.fasterxml.jackson.core/jackson-databind
                          com.fasterxml.jackson.core/jackson-core]]]

  :uberimage {:base-image "openjdk:8-jre-alpine"
              :instructions ["WORKDIR /" "EXPOSE 8080 1099"]
              :cmd ["/usr/bin/java"
                    "-XX:+PrintGCDetails"
                    "-XX:+PrintGCDateStamps"
                    "-XX:+PrintGCTimeStamps"
                    "-Xloggc:gc.log"
                    "-Dcom.sun.management.jmxremote.port=1099"
                    "-Dcom.sun.management.jmxremote.authenticate=false"
                    "-Dcom.sun.management.jmxremote.ssl=false"
                    "-jar" "/uberjar.jar"]
              :tag ~(str "svanosselaer/emp:"
                         (or (System/getenv "TAG")
                             "latest"))}

  :java-agents [[org.mortbay.jetty.alpn/jetty-alpn-agent "2.0.6"]]

  :profiles {:dev
             {:aliases {"test" "midje"
                        "ancient" ["with-profile" "quality" "ancient"]
                        "quality" ["with-profile" "quality" "test"]
                        "run-dev" ["trampoline"
                                   "run"
                                   "-m"
                                   "emp.server/run-dev"]}
              :plugins [[lein-midje "3.2.1"]
                        [lein-cloverage "1.0.9"
                         :exclusions [org.clojure/clojure
                                      cloverage]]]
              :managed-dependencies [[cloverage "RELEASE"
                                      :exclusions
                                      [com.fasterxml.jackson.dataformat/jackson-dataformat-smile
                                       com.fasterxml.jackson.dataformat/jackson-dataformat-cbor
                                       cheshire
                                       org.clojure/data.xml
                                       com.fasterxml.jackson.core/jackson-core]]]
              :dependencies [[org.clojure/tools.cli "0.3.5"]
                             [midje "1.8.3"]
                             [io.pedestal/pedestal.service-tools "0.5.2"]
                             [midje-junit-formatter "0.1.0-SNAPSHOT"
                              :exclusions [commons-logging]]
                             [cljito "0.2.1"]
                             [org.mockito/mockito-all "1.10.19"]
                             [guru.nidi.raml/raml-tester "0.9.1"]
                             [org.springframework/spring-test "4.3.11.RELEASE"
                              :exclusions [commons-logging]]
                             [org.springframework/spring-web "4.3.11.RELEASE"
                              :exclusions [commons-logging]]
                             [javax.ws.rs/javax.ws.rs-api "2.1"]]}
             :quality [:dev
                       {:injections [(require 'midje.config)
                                     (midje.config/change-defaults
                                       :check-after-creation
                                       false)]
                        :aliases {"test" ^:replace ["do"
                                                    ["bikeshed"]
                                                    ["eastwood"]
                                                    ["kibit"]
                                                    ["kibit" "test"]]}
                        :plugins [[lein-ancient "0.6.10"]
                                  [lein-bikeshed "0.4.1"]
                                  [jonase/eastwood "0.2.4"]
                                  [lein-kibit "0.1.5"
                                   :exclusions [org.clojure/clojure
                                                org.clojure/tools.cli
                                                org.clojure/tools.reader
                                                org.clojure/tools.namespace]]]
                        :dependencies [[org.clojure/tools.namespace
                                        "0.2.11"]]}]
             :uberjar {:aot [emp.server]}}
  :main ^{:skip-aot true} emp.server)
