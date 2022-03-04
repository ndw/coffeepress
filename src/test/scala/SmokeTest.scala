import com.jafpl.messages.Message
import com.jafpl.steps.DataConsumer
import com.xmlcalabash.XMLCalabash
import com.xmlcalabash.messages.XdmNodeItemMessage
import com.xmlcalabash.model.util.XProcConstants
import com.xmlcalabash.util.{PipelineOutputConsumer, S9Api}
import net.sf.saxon.s9api.QName
import org.scalatest.flatspec.AnyFlatSpec

class SmokeTest extends AnyFlatSpec {
  private val _date = new QName("", "date");

  "Running pipe.xpl " should " produce output" in {
    val calabash = XMLCalabash.newInstance()
    val output = new Consumer()
    val result = new PipelineOutputConsumer("result", output)
    calabash.args.parse(List("src/test/resources/pipe.xpl"))
    calabash.parameter(result)
    calabash.configure()
    calabash.run()
    if (output.message.isDefined) {
      output.message.get match {
        case msg: XdmNodeItemMessage =>
          val root = S9Api.documentElement(msg.item)
          assert(root.isDefined)
          assert(root.get.getNodeName == _date)
          // FIXME: check the details
        case _ =>
          fail()
      }
    } else {
      fail()
    }
  }

  private class Consumer extends DataConsumer {
    var _message = Option.empty[Message]

    def message: Option[Message] = _message

    override def consume(port: String, message: Message): Unit = {
      _message = Some(message)
    }
  }

}
