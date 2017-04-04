package co.s4n.infrastructure.reactive_kafka

import akka.actor.{ ActorRef, ActorSystem }
import akka.kafka.ConsumerMessage.{ CommittableMessage, CommittableOffsetBatch }
import akka.kafka._
import akka.kafka.scaladsl.{ Consumer, Producer }
import akka.stream.scaladsl.{ Flow, Keep, Sink, Source }
import akka.stream.ActorMaterializer
import akka.{ Done, NotUsed }
import org.apache.kafka.clients.consumer.{ ConsumerConfig, ConsumerRecord }
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.{ ByteArrayDeserializer, ByteArraySerializer, StringDeserializer, StringSerializer }
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{ Failure, Success }
import java.util.concurrent.atomic.AtomicLong

/**
 * Created by seven4n on 3/04/17.
 */
object ReactiveConsumer {

  val system = ActorSystem("example")
  implicit val ec = system.dispatcher
  implicit val m = ActorMaterializer.create(system)

  val consumerSettings = ConsumerSettings(system, new ByteArrayDeserializer, new StringDeserializer)
    .withBootstrapServers("localhost:9092")
    .withGroupId("group1")
    .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

  private val offset = new AtomicLong
  def loadOffset(): Future[Long] =
    Future.successful(offset.get)

  def save(record: ConsumerRecord[Array[Byte], String]): Future[Done] = {
    println(s"DB.save: ${record.value}")
    offset.set(record.offset)
    Future.successful(Done)
  }

  def update(data: String): Future[Done] = {
    println(s"DB.update: $data")
    Future.successful(Done)
  }

  def blabla(): Unit = {
    loadOffset().foreach { fromOffset =>
      val partition = 0
      val subscription = Subscriptions.assignmentWithOffset(
        new TopicPartition("UsersTopic", partition) -> fromOffset
      )
      val done =
        Consumer.plainSource(consumerSettings, subscription)
          .mapAsync(1)(save)
          .runWith(Sink.ignore)
    }
  }

}
