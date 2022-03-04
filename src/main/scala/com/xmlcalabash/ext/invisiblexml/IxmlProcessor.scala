package com.xmlcalabash.ext.invisiblexml

import com.jafpl.steps.PortCardinality
import com.xmlcalabash.exceptions.XProcException
import com.xmlcalabash.model.util.{ValueParser, XProcConstants}
import com.xmlcalabash.runtime.{XProcMetadata, XmlPortSpecification}
import com.xmlcalabash.steps.DefaultXmlStep
import com.xmlcalabash.util.{MediaType, MinimalStaticContext}
import net.sf.saxon.s9api._
import org.nineml.coffeefilter.InvisibleXml

class IxmlProcessor extends DefaultXmlStep {
  private var grammar = Option.empty[XdmItem]
  private var grammarMeta = Option.empty[XProcMetadata]
  private var source = Option.empty[XdmItem]
  private var sourceMeta = Option.empty[XProcMetadata]
  private var parameters = Map.empty[QName, XdmValue]

  override def inputSpec: XmlPortSpecification = new XmlPortSpecification(
    Map("grammar" -> PortCardinality.ZERO_OR_MORE, "source" -> PortCardinality.EXACTLY_ONE),
    Map("grammar" -> List("application/xml", "text/plain"), "source" -> List("text/plain"))
  )

  override def outputSpec: XmlPortSpecification = XmlPortSpecification.XMLRESULT

  /*
  <p:declare-step type="cx:invisible-xml" use-when="false()">
     <p:input port="grammar" sequence="true" content-types="any"/>
     <p:input port="source" primary="true" content-types="any -xml -html"/>
     <p:output port="result" content-types="any"/>
     <p:option name="parameters" as="map(xs:QName, item()*)?"/>
     <p:option name="fail-on-error" as="xs:boolean" select="true()"/>
</p:declare-step>

   */

  override def receive(port: String, item: Any, metadata: XProcMetadata): Unit = {
    if (port == "source") {
      source = Some(item.asInstanceOf[XdmItem])
      sourceMeta = Some(metadata)
    } else if (port == "grammar") {
      grammar = Some(item.asInstanceOf[XdmItem])
      grammarMeta = Some(metadata);
    }
  }

  override def run(context: MinimalStaticContext): Unit = {
    super.run(context)

    val pmap = mapBinding(XProcConstants._parameters)
    if (pmap.size() > 0) {
      parameters = ValueParser.parseParameters(pmap, context)
    }
    val failOnError = booleanBinding(XProcConstants._fail_on_error).getOrElse(false)

    val invisibleXml = new InvisibleXml()
    var parser = if (grammar.isDefined) {
      if (grammarMeta.get.contentType.textContentType) {
        val text = grammar.get.toString;
        invisibleXml.getParserFromIxml(text)
      } else {
        throw new IllegalArgumentException("Only text/plain .ixml supported at the moment")
      }
    } else {
      invisibleXml.getParser();
    }

    if (sourceMeta.get.contentType.textContentType) {
      val input = source.get.toString;
      val doc = parser.parse(input);

      if (!doc.succeeded() && failOnError) {
        throw new XProcException(XProcException.stepErrorCode(205));
      }

      val builder = config.processor.newDocumentBuilder();
      val bch = builder.newBuildingContentHandler();
      doc.getTree(bch);
      val tree = bch.getDocumentNode;
      consumer.receive("result", tree, new XProcMetadata(MediaType.XML))
    } else {
      throw new IllegalArgumentException("Only text/plain sources supported at the moment")
    }
  }
}
