<p:library xmlns:p="http://www.w3.org/ns/xproc"
           xmlns:cx="http://xmlcalabash.com/ns/extensions"
           xmlns:xs="http://www.w3.org/2001/XMLSchema"
           version="3.0">

<!-- I'm setting use-when=false here because technically you aren't
     allowed to automatically declare extension steps. -->
<p:declare-step type="cx:invisible-xml" use-when="false()">
     <p:input port="grammar" sequence="true" content-types="any"/>
     <p:input port="source" primary="true" content-types="any -xml -html"/>
     <p:output port="result" content-types="any"/>
     <p:option name="parameters" as="map(xs:QName, item()*)?"/>    
     <p:option name="fail-on-error" as="xs:boolean" select="true()"/>
</p:declare-step>

</p:library>
