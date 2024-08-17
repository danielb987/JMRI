<?xml version="1.0" encoding="iso-8859-1"?>

<!-- Stylesheet to convert a JMRI panel file into an HTML page              -->
<!-- Used by default when the panel file is displayed in a web browser      -->
<!-- This version corresponds to the 2.9.6 schema update                    -->

<!-- This file is part of JMRI.  Copyright 2007-2011, 2016, 2018, 2023.     -->

<xsl:stylesheet	version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<!-- Define the copyright year for the output page
     In batch work via running Ant, this is defined
     via the build.xml file. We build it by concatenation
     because XPath will evaluate '1997 - 2017' to '20'.
-->
<xsl:param name="JmriCopyrightYear" select="concat('1997','-','2024')" />

<!-- Need to instruct the XSLT processor to use HTML output rules.
     See http://www.w3.org/TR/xslt#output for more details
-->
<xsl:output method="html" encoding="ISO-8859-1"/>


<!-- Define variables for translation -->
<xsl:variable name="lcletters">abcdefghijklmnopqrstuvwxyz</xsl:variable>
<xsl:variable name="ucletters">ABCDEFGHIJKLMNOPQRSTUVWXYZ</xsl:variable>

<!-- This first template matches our root element in the input file.
     This will trigger the generation of the HTML skeleton document.
     In between we let the processor recursively process any contained
     elements, which is what the apply-templates instruction does.
     We also pick some stuff out explicitly in the head section using
     value-of instructions.
-->
<xsl:template match='layout-config'>

<html>
	<head>
		<title>JMRI panel file</title>
	</head>

	<body>
		<h2>JMRI panel file</h2>

                <xsl:apply-templates/>

        <p></p>
<hr/>
This page was produced by <a href="https://www.jmri.org">JMRI</a>.
<p/>Copyright &#169; <xsl:value-of select="$JmriCopyrightYear" /> JMRI Community.
<p/>JMRI, DecoderPro, PanelPro, DispatcherPro and associated logos are our trademarks.
<p/><a href="https://www.jmri.org/Copyright.html">Additional information on copyright, trademarks and licenses is linked here.</a>
	</body>
</html>

</xsl:template>

<!-- Index through turnouts elements -->
<!-- each one becomes a table -->
<xsl:template match="layout-config/turnouts">
<h3>Turnouts</h3>
    <table border="1">
    <tr>
      <th>System Name</th>
      <th>User Name</th>
      <th>Fdbk?</th>
      <th>Inv?</th>
      <th>Lckd?</th>
