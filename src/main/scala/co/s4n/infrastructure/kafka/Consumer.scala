package co.s4n.infrastructure.kafka

import java.util.concurrent._
import java.util.{ Collections, Properties }

import com.typesafe.scalalogging.LazyLogging
import org.apache.kafka.clients.consumer.{ ConsumerConfig, KafkaConsumer }

import scala.collection.JavaConverters._

object Consumer extends LazyLogging {

  def createConsumerConfig(): Properties = {
    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaProducerExample")
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000")
    props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000")
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props
  }

  def run(): Unit = {
    val consumer = new KafkaConsumer[String, String](createConsumerConfig())
    consumer.subscribe(Collections.singletonList("UsersTopic"))

    Executors.newSingleThreadExecutor.execute(() => {
      while (true) {
        val records = consumer.poll(1000)

        for (record <- records.iterator().asScala) {
          logger.info("///// **** Message Received **** /////")
          logger.info("(" + record.key() + ", " + record.value() + ") at offset " + record.offset())
          logger.info(" ****  **** ")
        }
      }
    })
  }

}
