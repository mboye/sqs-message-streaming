package app

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.sqs.MessageAction
import akka.stream.alpakka.sqs.javadsl.SqsAckSink
import akka.stream.alpakka.sqs.scaladsl.SqsSource
import akka.stream.scaladsl.Flow
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.sqs.model.ReceiveMessageRequest
import com.amazonaws.services.sqs.{AmazonSQSAsync, AmazonSQSAsyncClientBuilder, model}

object SqsReader extends App {
  println("Starting SQS reader...")

  implicit val system = ActorSystem()

  implicit val mat = ActorMaterializer()

  val sqsUrl = "http://localhost:9324"

  println("Building SQS client...")
  implicit val sqs: AmazonSQSAsync = AmazonSQSAsyncClientBuilder
    .standard()
    .withEndpointConfiguration(new EndpointConfiguration(sqsUrl, "eu-central-1"))
    .build()

  println("Listing queues...")
  var queueUrl = ""
  sqs.listQueues().getQueueUrls.forEach({
    url => {
      println(s"Queue: ${url}")
      if (url.endsWith("primes")) {
        queueUrl = url
      }
    }
  })

  println(s"Using queue: ${queueUrl}")

  SqsSource(queueUrl).map { msg => {
    println(s"Request: ${msg.getBody}")
    (msg, MessageAction.Delete)
  }}.runWith(SqsAckSink.create(queueUrl, sqs))
}
