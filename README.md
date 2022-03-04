# CoffeePress, an Invisible XML step

This is an an XML Calabash 3.x extension step that implements an Invisible XML processor.

```
<p:declare-step type="cx:invisible-xml">
     <p:input port="grammar" sequence="true" content-types="any"/>
     <p:input port="source" primary="true" content-types="any"/>
     <p:output port="result" content-types="any"/>
     <p:option name="parameters" as="map(xs:QName, item()*)?"/>    
     <p:option name="fail-on-error" as="xs:boolean" select="true()"/>
</p:declare-step>
```

This is intended to implement [the standard p:ixml step](https://spec.xproc.org/master/head/ixml/),
but it’s still under development.
